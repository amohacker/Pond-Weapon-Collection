package net.pondsmp.pondweapons.capabilities;

import java.security.cert.TrustAnchor;

public class Cooldown implements ICooldown{
    private int cooldown = 0;

    @Override
    public boolean hasCooldown() {
        if (this.cooldown <= 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setCooldown(int ticks) {
        this.cooldown = ticks;
    }

    @Override
    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public void subtractCooldown(int ticks) {
        this.cooldown -= ticks;
    }

    @Override
    public boolean tick() {
        if (cooldown != 0){
            cooldown -= 1;
            if (cooldown == 0) {
                return true;
            }
        }
        return false;
    }
}
