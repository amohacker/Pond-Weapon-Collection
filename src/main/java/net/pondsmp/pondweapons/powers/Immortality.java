package net.pondsmp.pondweapons.powers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CClientStatusPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.pondsmp.pondweapons.PondWeaponMod;
import net.pondsmp.pondweapons.capabilities.IPowers;
import net.pondsmp.pondweapons.capabilities.PowersCapability;
import net.pondsmp.pondweapons.network.ImmortalityParticlesToClient;
import net.pondsmp.pondweapons.network.ImmortalityRespawnToClient;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

public class Immortality {
    private static HashMap<PlayerEntity, Float> last_damages = new HashMap<PlayerEntity, Float>();
    private static HashMap<PlayerEntity, BlockPos> spawn_pos = new HashMap<PlayerEntity, BlockPos>();
    //Date, BlockPos, Integer radius, BasicParticleType
    private static ArrayList<List> effects = new ArrayList<>();

    private static void addEffect(Date date, BlockPos pos, Integer radius, BasicParticleType type) {
        List list = new ArrayList();
        list.add(date);
        list.add(pos);
        list.add(radius);
        list.add(type);
        effects.add(list);
    }


    private static void respawnPlayer(ServerPlayerEntity player) {
        ImmortalityRespawnToClient msg = new ImmortalityRespawnToClient(true);
        PondWeaponMod.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    private static void respawnParticles(World world, BlockPos pos, Date date) {
        ImmortalityParticlesToClient msg = new ImmortalityParticlesToClient(pos, date);
        Supplier<PacketDistributor.TargetPoint> targetPoint = PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 50, world.getDimensionKey());
        PondWeaponMod.simpleChannel.send(PacketDistributor.NEAR.with(() -> targetPoint.get()), msg);
    }

    public static void playerdied(){
        Minecraft.getInstance().displayGuiScreen((Screen) null);
    }

    private static double spawnrandomizer(double in){
        double rand = Math.random();
        rand = rand * 10.0;
        rand += 1;
        rand = rand/2;
        rand = rand-2.5;
        return in + rand;
    }

    private static double respawnInSeconds(PlayerEntity player) {
        double time = (20 - last_damages.get(player)) - (player.deathTime / 20);
        if (time<0) return 0;
        return time;
    }

    //uses the midpoint circle algorithm to spawn particles in a circle
    public static void drawSphere(ClientWorld world, BlockPos pos, int r, BasicParticleType particle) {
        System.out.println("Drawing Sphere: ");
        int x = r;
        int y = 0;
        ParticleSpawner s = new ParticleSpawner(particle, world, true, r, pos, pos.getY());
        //spawning initial point on axis after translation
        drawCircle(world, pos, particle, x+r, y + pos.getY());
        //if radius is 0 only a single point is spawned
        if (r>0) {
            drawCircle(world, pos, particle, x + r, -y + pos.getY());
//            s.spawn(x + pos.getY(), x + pos.getY());
//            s.spawn(-z + r, x + pos.getY());
        }

        int P = 1 -r;
        while (x > y) {
            y++;

            // Mid-point is inside or on the perimiter
            if (P <= 0)
                P = P + 2 * y + 1;
                // Mid-point is outside the perimiter
            else {
                x--;
                P = P + 2 * y - 2 * x + 1;
            }
            // All the perimeter points have already been printed
            if (x < y)
                break;

            // Printing the generated point and its reflection in the other octants after translation
            drawCircle(world, pos, particle, x + r, y+pos.getY());
//            s.spawn(-x + r, z + pos.getY());
//            s.spawn(x + r, -z + pos.getY());
//            s.spawn(-x + r, -z + pos.getY());

            //If generated point is on the line x=z then the perimeter points have already been printed
            if (x != y) {
                drawCircle(world, pos, particle, y + r, x + pos.getY());
//                drawCircle(world, pos, particle, -z + r, x + pos.getY());
//                drawCircle(world, pos, particle, z + r, -x + pos.getY());
//                drawCircle(world, pos, particle, -z + r, -x + pos.getY());
            }
        }
    }

    public static void drawCircle(ClientWorld world, BlockPos pos, int r, BasicParticleType particle) {
    drawCircle(world, pos, particle, r, pos.getY());
    }


        //uses the midpoint circle algorithm to spawn particles in a circle
    public static void drawCircle(ClientWorld world, BlockPos pos, BasicParticleType particle, int r, double y) {
        System.out.println("Drawing circle:");
        int x = r;
        int z = 0;
        ParticleSpawner s = new ParticleSpawner(particle, world, true, r, pos, y);
        //spawning initial point on axis after translation
        s.spawn(x+pos.getX(), z + pos.getZ());
        //if radius is 0 only a single point is spawned
        if (r>0) {
            s.spawn(x + pos.getX(), -z + pos.getZ());
            s.spawn(x + pos.getZ(), x + pos.getZ());
            s.spawn(-z + pos.getX(), x + pos.getZ());
        }

        int P = 1 -r;
        while (x > z) {
            z++;

            // Mid-point is inside or on the perimiter
            if (P <= 0)
                P = P + 2 * z + 1;
            // Mid-point is outside the perimiter
            else {
                x--;
                P = P + 2 * z - 2 * x + 1;
            }
            // All the perimeter points have already been printed
            if (x < z)
                break;

            // Printing the generated point and its reflection in the other octants after translation
            s.spawn(x + pos.getX(), z+pos.getZ());
            s.spawn(-x + pos.getX(), z + pos.getZ());
            s.spawn(x + pos.getX(), -z + pos.getZ());
            s.spawn(-x + pos.getX(), -z + pos.getZ());

            //If generated point is on the line x=z then the perimeter points have already been printed
            if (x != z) {
                s.spawn(z + pos.getX(), x + pos.getZ());
                s.spawn(-z + pos.getX(), x + pos.getZ());
                s.spawn(z + pos.getX(), -x + pos.getZ());
                s.spawn(-z + pos.getX(), -x + pos.getZ());
            }
        }
    }

    private static class ParticleSpawner {
        private int radius;
        private int x;
        private double y;
        private int z;
        private BasicParticleType type;
        private ClientWorld world;
        private boolean force;
        private BlockPos pos;
        public ParticleSpawner(BasicParticleType type, ClientWorld world, Boolean force, int radius, BlockPos pos, double y) {
            this.type = type;
            this.radius = radius;
            this.y = y;
            this.world = world;
            this.force = force;
            this.pos = pos;
        }
        public void spawn(int x, int z){
            double xspeed;
            double yspeed;
            double zspeed;
            xspeed = x - pos.getX();
            yspeed = y - pos.getY();
            zspeed = z - pos.getZ();
            System.out.println("x= " + x + ", y= " + y + ", z= " + z);
            world.addParticle(type, force, x, y, z, xspeed, yspeed, zspeed);
        }
    }

    @Mod.EventBusSubscriber(modid = PondWeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {

        @SubscribeEvent
        public static void playerDeathEvent(LivingDeathEvent event) {
            if (event.getEntity() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote) {
                try {
                    IPowers powers = event.getEntityLiving().getCapability(PowersCapability.POWERS_CAPABILITY).orElseThrow(() -> {
                        return new Exception("POWERS_CAPABILITY not found on entity " + event.getEntityLiving().getEntityString());
                    });
                    if (powers.hasPower("immortality")) {
                        PlayerEntity player = (PlayerEntity) event.getEntity();
                        BlockPos pos = player.getPosition();
                        double x = spawnrandomizer(pos.getX());
                        double z = spawnrandomizer(pos.getZ());
                        double y = (player.getEntityWorld().getHeight(Heightmap.Type.WORLD_SURFACE,(int) x, (int) z));
                        pos = new BlockPos(x, y, z);
                        Date date = new Date();
                        date.setTime(new Date().getTime() + (long) (respawnInSeconds(player)*1000));
                        spawn_pos.put(player, pos);
                        respawnParticles(player.getEntityWorld(), pos, date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @SubscribeEvent
        public static void livingHurtEvent(LivingHurtEvent event) {
            if (event.getEntityLiving() instanceof PlayerEntity){
                event.getEntityLiving().getCapability(PowersCapability.POWERS_CAPABILITY).ifPresent(new NonNullConsumer<IPowers>() {
                    @Override
                    public void accept(@Nonnull IPowers iPowers) {
                        if (iPowers.hasPower("immortality")) {
                            last_damages.put((PlayerEntity) event.getEntityLiving(), event.getAmount());
                        }}});
            }
        }


        @SubscribeEvent
        public static void tickEvent(TickEvent.PlayerTickEvent event){
            if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
                event.player.getCapability(PowersCapability.POWERS_CAPABILITY).ifPresent(new NonNullConsumer<IPowers>() {
                    @Override
                    public void accept(@Nonnull IPowers iPowers) {
                        if(!event.player.isAlive() && iPowers.hasPower("immortality")){
                            try {
                                ServerPlayerEntity player = (ServerPlayerEntity) event.player;
                                player.sendStatusMessage(new StringTextComponent("Reviving in " + String.valueOf(20 - last_damages.get(player) - player.deathTime / 20) + " seconds."), true);
                                if (respawnInSeconds(player)==0) {
                                    last_damages.remove(player);
                                    player.markPlayerActive();
                                    player.deathTime = 0;
                                    respawnPlayer((ServerPlayerEntity) event.player);
                                    new ServerPlayNetHandler(player.server, player.connection.getNetworkManager(), player).processClientStatus(new CClientStatusPacket(CClientStatusPacket.State.PERFORM_RESPAWN));

                                    ServerWorld world = player.getServerWorld();

                                    BlockPos pos = spawn_pos.get(player);

                                    float yaw = player.getYaw(0);
                                    float pitch = player.getPitch(0);

                                    player.teleport(world, pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);

                                    player.setInvulnerable(false);
                                    player.hurtResistantTime = 0;
//                                    respawnPlayer((ServerPlayerEntity) event.player);

                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void particleTickEvent(TickEvent.PlayerTickEvent event){
            if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START) {
                if (!effects.isEmpty()) {
                    for (List effect : effects){
                        Date date = (Date) effect.get(0);
                        if (date.before(new Date())) {
                            effects.remove(effect);
                        } else {
                            BlockPos pos = (BlockPos) effect.get(1);
                            int radius = (int) effect.get(2);
                            BasicParticleType type = (BasicParticleType) effect.get(3);
                            addEffect(date,pos,radius,type);
                        }
                    }
                }
            }
        }
    }
}