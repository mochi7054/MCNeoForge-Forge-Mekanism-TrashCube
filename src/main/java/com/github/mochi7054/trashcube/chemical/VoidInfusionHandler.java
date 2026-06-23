package com.github.mochi7054.trashcube.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.infuse.IInfusionHandler;

public class VoidInfusionHandler implements IInfusionHandler {
    public static final VoidInfusionHandler INSTANCE = new VoidInfusionHandler();

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public InfusionStack getChemicalInTank(int tank) {
        return InfusionStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, InfusionStack stack) {
        // Do nothing
    }

    @Override
    public long getTankCapacity(int tank) {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, InfusionStack stack) {
        return true;
    }

    @Override
    public InfusionStack insertChemical(int tank, InfusionStack stack, Action action) {
        return InfusionStack.EMPTY;
    }

    @Override
    public InfusionStack extractChemical(int tank, long amount, Action action) {
        return InfusionStack.EMPTY;
    }

    @Override
    public InfusionStack insertChemical(InfusionStack stack, Action action) {
        return InfusionStack.EMPTY;
    }

    @Override
    public InfusionStack extractChemical(long amount, Action action) {
        return InfusionStack.EMPTY;
    }

    @Override
    public InfusionStack extractChemical(InfusionStack stack, Action action) {
        return InfusionStack.EMPTY;
    }
}
