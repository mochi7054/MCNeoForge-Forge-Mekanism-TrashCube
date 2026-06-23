package com.github.mochi7054.trashcube.energy;

import mekanism.api.Action;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.math.FloatingLong;

public class VoidEnergyHandler implements IStrictEnergyHandler {
    public static final VoidEnergyHandler INSTANCE = new VoidEnergyHandler();

    @Override
    public int getEnergyContainerCount() {
        return 1;
    }

    @Override
    public FloatingLong getEnergy(int container) {
        return FloatingLong.ZERO;
    }

    @Override
    public void setEnergy(int container, FloatingLong energy) {
        // Do nothing
    }

    @Override
    public FloatingLong getMaxEnergy(int container) {
        return FloatingLong.MAX_VALUE;
    }

    @Override
    public FloatingLong getNeededEnergy(int container) {
        return FloatingLong.MAX_VALUE;
    }

    @Override
    public FloatingLong insertEnergy(int container, FloatingLong amount, Action action) {
        return FloatingLong.ZERO; // Return FloatingLong.ZERO because 0 leftover energy is returned (meaning all was consumed)
    }

    @Override
    public FloatingLong extractEnergy(int container, FloatingLong amount, Action action) {
        return FloatingLong.ZERO;
    }

    @Override
    public FloatingLong insertEnergy(FloatingLong amount, Action action) {
        return FloatingLong.ZERO;
    }

    @Override
    public FloatingLong extractEnergy(FloatingLong amount, Action action) {
        return FloatingLong.ZERO;
    }
}
