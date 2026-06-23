package com.github.mochi7054.trashcube.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.pigment.IPigmentHandler;

public class VoidPigmentHandler implements IPigmentHandler {
    public static final VoidPigmentHandler INSTANCE = new VoidPigmentHandler();

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public PigmentStack getChemicalInTank(int tank) {
        return PigmentStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, PigmentStack stack) {
        // Do nothing
    }

    @Override
    public long getTankCapacity(int tank) {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, PigmentStack stack) {
        return true;
    }

    @Override
    public PigmentStack insertChemical(int tank, PigmentStack stack, Action action) {
        return PigmentStack.EMPTY;
    }

    @Override
    public PigmentStack extractChemical(int tank, long amount, Action action) {
        return PigmentStack.EMPTY;
    }

    @Override
    public PigmentStack insertChemical(PigmentStack stack, Action action) {
        return PigmentStack.EMPTY;
    }

    @Override
    public PigmentStack extractChemical(long amount, Action action) {
        return PigmentStack.EMPTY;
    }

    @Override
    public PigmentStack extractChemical(PigmentStack stack, Action action) {
        return PigmentStack.EMPTY;
    }
}
