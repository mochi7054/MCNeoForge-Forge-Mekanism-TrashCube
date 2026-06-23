package com.github.mochi7054.trashcube.block.entity;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TrashCubeBlockEntity extends TileEntityConfigurableMachine implements MenuProvider {

    private BasicInventorySlot[] trashSlots;
    private BasicFluidTank fluidTank;
    private IChemicalTank chemicalTank;
    private MachineEnergyContainer<TrashCubeBlockEntity> energyContainer;

    public TrashCubeBlockEntity(BlockPos pos, BlockState state) {
        super(state.getBlockHolder(), pos, state);

        ejectorComponent = new TileComponentEjector(this);

        // Setup side configs for Item, Fluid, Chemical, Energy
        // setupInputConfig only registers INPUT and NONE states
        configComponent.setupInputConfig(TransmissionType.ITEM, Arrays.asList(trashSlots));
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, chemicalTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
    }

    public BasicInventorySlot[] getTrashSlots() {
        return trashSlots;
    }

    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    public IChemicalTank getChemicalTank() {
        return chemicalTank;
    }

    public MachineEnergyContainer<TrashCubeBlockEntity> getEnergyContainer() {
        return energyContainer;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        // Place the 3x3 slots grid starting at x=62, y=17 (centered in standard 176x166 GUI)
        trashSlots = new BasicInventorySlot[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                trashSlots[index] = BasicInventorySlot.at(listener, 62 + col * 18, 17 + row * 18);
                builder.addSlot(trashSlots[index]);
            }
        }
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);
        fluidTank = BasicFluidTank.create(1000000, listener);
        builder.addTank(fluidTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        chemicalTank = BasicChemicalTank.create(1000000L, listener);
        builder.addTank(chemicalTank);
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        energyContainer = MachineEnergyContainer.input(this, listener);
        builder.addContainer(energyContainer);
        return builder.build();
    }

    @Override
    public void onContentsChanged() {
        super.onContentsChanged();

        if (trashSlots != null) {
            for (BasicInventorySlot slot : trashSlots) {
                if (slot != null && !slot.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                }
            }
        }
        if (fluidTank != null && !fluidTank.isEmpty()) {
            fluidTank.setEmpty();
        }
        if (chemicalTank != null && !chemicalTank.isEmpty()) {
            chemicalTank.setEmpty();
        }
        if (energyContainer != null && !energyContainer.isEmpty()) {
            energyContainer.setEmpty();
        }
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdate = super.onUpdateServer();

        // Instantly delete any contents inside the slot and tanks
        if (trashSlots != null) {
            for (BasicInventorySlot slot : trashSlots) {
                if (slot != null && !slot.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                    sendUpdate = true;
                }
            }
        }
        if (fluidTank != null && !fluidTank.isEmpty()) {
            fluidTank.setStack(FluidStack.EMPTY);
            sendUpdate = true;
        }
        if (chemicalTank != null && !chemicalTank.isEmpty()) {
            chemicalTank.setEmpty();
            sendUpdate = true;
        }
        if (energyContainer != null && !energyContainer.isEmpty()) {
            energyContainer.setEmpty();
            sendUpdate = true;
        }

        return sendUpdate;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.mekanismtrashcube.trash_cube");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inv, @NotNull Player player) {
        return new TrashCubeMenu(containerId, inv, this);
    }

    public mekanism.common.registration.impl.ContainerTypeRegistryObject<TrashCubeMenu> getContainerType() {
        return TrashCube.CONTAINER_TYPE;
    }
}
