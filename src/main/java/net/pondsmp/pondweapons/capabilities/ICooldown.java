package net.pondsmp.pondweapons.capabilities;

public interface ICooldown {

    public boolean hasCooldown();
    public void setCooldown(int ticks);
    public int getCooldown();
    public void subtractCooldown(int ticks);
    public boolean tick();
}
