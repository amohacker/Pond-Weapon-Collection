package net.pondsmp.pondweapons.capabilities;

import java.util.ArrayList;

public interface IPowers {

    public ArrayList<String> getPowers();
    public boolean hasPower(String power);
    public void removePower(String power);
    public void addPower(String power);
    public void setPowers(ArrayList<String> powers);

}
