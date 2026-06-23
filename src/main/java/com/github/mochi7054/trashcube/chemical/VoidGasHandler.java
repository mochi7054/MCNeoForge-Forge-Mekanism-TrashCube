package com.github.mochi7054.trashcube.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;

public class VoidGasHandler implements IGasHandler {
    public static final VoidGasHandler INSTANCE = new VoidGasHandler();

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public GasStack getChemicalInTank(int tank) {
        return GasStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, GasStack stack) {
        // Do nothing
    }

    @Override
    public long getTankCapacity(int tank) {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, GasStack stack) {
        return true;
    }

    @Override
    public GasStack insertChemical(int tank, GasStack stack, Action action) {
        return GasStack.EMPTY;
    }

    @Override
    public GasStack extractChemical(int tank, long amount, Action action) {
        return GasStack.EMPTY;
    }

    @Override
    public GasStack insertChemical(GasStack stack, Action action) {
        return GasStack.EMPTY;
    }

    @Override
    public GasStack extractChemical(long amount, Action action) {
        return GasStack.EMPTY;
    }

    @Override
    public GasStack extractChemical(GasStack stack, Action action) {
        return GasStack.EMPTY;
    }
}
