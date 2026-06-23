package com.github.mochi7054.trashcube.block.entity;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
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
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TrashCubeBlockEntity extends TileEntityConfigurableMachine implements MenuProvider {

    private BasicInventorySlot[] trashSlots;
    private BasicFluidTank fluidTank;
    private IGasTank gasTank;
    private IInfusionTank infusionTank;
    private IPigmentTank pigmentTank;
    private ISlurryTank slurryTank;
    private MachineEnergyContainer<TrashCubeBlockEntity> energyContainer;

    public TrashCubeBlockEntity(BlockPos pos, BlockState state) {
        super(TrashCube.BLOCK, pos, state);

        configComponent = new TileComponentConfig(this,
            TransmissionType.ITEM,
            TransmissionType.FLUID,
            TransmissionType.GAS,
            TransmissionType.INFUSION,
            TransmissionType.PIGMENT,
            TransmissionType.SLURRY,
            TransmissionType.ENERGY
        );
        ejectorComponent = new TileComponentEjector(this);

        configComponent.setupInputConfig(TransmissionType.ITEM, Arrays.asList(trashSlots));
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        configComponent.setupInputConfig(TransmissionType.INFUSION, infusionTank);
        configComponent.setupInputConfig(TransmissionType.PIGMENT, pigmentTank);
        configComponent.setupInputConfig(TransmissionType.SLURRY, slurryTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
    }

    public BasicInventorySlot[] getTrashSlots() {
        return trashSlots;
    }

    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    public IGasTank getGasTank() {
        return gasTank;
    }

    public IInfusionTank getInfusionTank() {
        return infusionTank;
    }

    public IPigmentTank getPigmentTank() {
        return pigmentTank;
    }

    public ISlurryTank getSlurryTank() {
        return slurryTank;
    }

    public MachineEnergyContainer<TrashCubeBlockEntity> getEnergyContainer() {
        return energyContainer;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
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
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        fluidTank = BasicFluidTank.create(1000000, listener);
        builder.addTank(fluidTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        gasTank = ChemicalTankBuilder.GAS.create(1000000L, listener);
        builder.addTank(gasTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper.forSideInfusionWithConfig(this::getDirection, this::getConfig);
        infusionTank = ChemicalTankBuilder.INFUSION.create(1000000L, listener);
        builder.addTank(infusionTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper.forSidePigmentWithConfig(this::getDirection, this::getConfig);
        pigmentTank = ChemicalTankBuilder.PIGMENT.create(1000000L, listener);
        builder.addTank(pigmentTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper.forSideSlurryWithConfig(this::getDirection, this::getConfig);
        slurryTank = ChemicalTankBuilder.SLURRY.create(1000000L, listener);
        builder.addTank(slurryTank);
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        energyContainer = MachineEnergyContainer.input(this, listener);
        builder.addContainer(energyContainer);
        return builder.build();
    }

    @Override
    public void onContentsChanged() {
        super.onContentsChanged();

        boolean changed = false;
        if (trashSlots != null) {
            for (BasicInventorySlot slot : trashSlots) {
                if (slot != null && !slot.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                    changed = true;
                }
            }
        }
        if (fluidTank != null && !fluidTank.isEmpty()) {
            fluidTank.setEmpty();
            changed = true;
        }
        if (gasTank != null && !gasTank.isEmpty()) {
            gasTank.setEmpty();
            changed = true;
        }
        if (infusionTank != null && !infusionTank.isEmpty()) {
            infusionTank.setEmpty();
            changed = true;
        }
        if (pigmentTank != null && !pigmentTank.isEmpty()) {
            pigmentTank.setEmpty();
            changed = true;
        }
        if (slurryTank != null && !slurryTank.isEmpty()) {
            slurryTank.setEmpty();
            changed = true;
        }
        if (energyContainer != null && !energyContainer.isEmpty()) {
            energyContainer.setEmpty();
            changed = true;
        }
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();

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
        if (gasTank != null && !gasTank.isEmpty()) {
            gasTank.setEmpty();
        }
        if (infusionTank != null && !infusionTank.isEmpty()) {
            infusionTank.setEmpty();
        }
        if (pigmentTank != null && !pigmentTank.isEmpty()) {
            pigmentTank.setEmpty();
        }
        if (slurryTank != null && !slurryTank.isEmpty()) {
            slurryTank.setEmpty();
        }
        if (energyContainer != null && !energyContainer.isEmpty()) {
            energyContainer.setEmpty();
        }
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

