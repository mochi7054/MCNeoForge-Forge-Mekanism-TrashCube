package com.github.mochi7054.trashcube.energy;

import mekanism.api.Action;
import mekanism.api.energy.IStrictEnergyHandler;

public class VoidEnergyHandler implements IStrictEnergyHandler {
    public static final VoidEnergyHandler INSTANCE = new VoidEnergyHandler();

    @Override
    public int getEnergyContainerCount() {
        return 1;
    }

    @Override
    public long getEnergy(int container) {
        return 0L;
    }

    @Override
    public void setEnergy(int container, long energy) {
        // Do nothing
    }

    @Override
    public long getMaxEnergy(int container) {
        return Long.MAX_VALUE;
    }

    @Override
    public long getNeededEnergy(int container) {
        return Long.MAX_VALUE;
    }

    @Override
    public long insertEnergy(int container, long amount, Action action) {
        return amount; // Return amount, meaning all of it was inserted/consumed
    }

    @Override
    public long extractEnergy(int container, long amount, Action action) {
        return 0L;
    }

    @Override
    public long insertEnergy(long amount, Action action) {
        return amount;
    }

    @Override
    public long extractEnergy(long amount, Action action) {
        return 0L;
    }
}
