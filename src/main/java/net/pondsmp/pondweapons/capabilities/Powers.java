package net.pondsmp.pondweapons.capabilities;

import java.util.ArrayList;

public class Powers implements IPowers{
    private ArrayList<String> powers = new ArrayList<String>();

    @Override
    public ArrayList<String> getPowers() {
        return powers;
    }

    @Override
    public boolean hasPower(String power) {
        return powers.contains(power);
    }

    @Override
    public void removePower(String power) {
        this.powers.remove(power);
    }

    @Override
    public void addPower(String power) {
        this.powers.add(power);
    }

    @Override
    public void setPowers(ArrayList<String> powers) {
        this.powers = powers;
    }
}
