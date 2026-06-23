package com.github.mochi7054.trashcube.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import org.jetbrains.annotations.NotNull;

public class VoidChemicalHandler implements IChemicalHandler {
    @Override
    public int getChemicalTanks() {
        return 1;
    }

    @Override
    public @NotNull ChemicalStack getChemicalInTank(int tank) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, @NotNull ChemicalStack stack) {
        // Do nothing
    }

    @Override
    public long getChemicalTankCapacity(int tank) {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, @NotNull ChemicalStack stack) {
        return true;
    }

    @Override
    public @NotNull ChemicalStack insertChemical(int tank, @NotNull ChemicalStack stack, Action action) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public @NotNull ChemicalStack extractChemical(int tank, long amount, Action action) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public @NotNull ChemicalStack insertChemical(@NotNull ChemicalStack stack, Action action) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public @NotNull ChemicalStack extractChemical(long amount, Action action) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public @NotNull ChemicalStack extractChemical(@NotNull ChemicalStack stack, Action action) {
        return ChemicalStack.EMPTY;
    }
}
