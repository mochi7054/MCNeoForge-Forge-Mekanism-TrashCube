package com.github.mochi7054.trashcube.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.chemical.slurry.ISlurryHandler;

public class VoidSlurryHandler implements ISlurryHandler {
    public static final VoidSlurryHandler INSTANCE = new VoidSlurryHandler();

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public SlurryStack getChemicalInTank(int tank) {
        return SlurryStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, SlurryStack stack) {
        // Do nothing
    }

    @Override
    public long getTankCapacity(int tank) {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, SlurryStack stack) {
        return true;
    }

    @Override
    public SlurryStack insertChemical(int tank, SlurryStack stack, Action action) {
        return SlurryStack.EMPTY;
    }

    @Override
    public SlurryStack extractChemical(int tank, long amount, Action action) {
        return SlurryStack.EMPTY;
    }

    @Override
    public SlurryStack insertChemical(SlurryStack stack, Action action) {
        return SlurryStack.EMPTY;
    }

    @Override
    public SlurryStack extractChemical(long amount, Action action) {
        return SlurryStack.EMPTY;
    }

    @Override
    public SlurryStack extractChemical(SlurryStack stack, Action action) {
        return SlurryStack.EMPTY;
    }
}
