package com.github.mochi7054.trashcube.block.entity;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.chemical.VoidGasHandler;
import com.github.mochi7054.trashcube.chemical.VoidInfusionHandler;
import com.github.mochi7054.trashcube.chemical.VoidPigmentHandler;
import com.github.mochi7054.trashcube.chemical.VoidSlurryHandler;
import com.github.mochi7054.trashcube.energy.VoidEnergyHandler;
import com.github.mochi7054.trashcube.fluid.VoidFluidHandler;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import com.github.mochi7054.trashcube.inventory.VoidItemHandler;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrashCubeBlockEntity extends TileEntityConfigurableMachine implements MenuProvider {

    private BasicInventorySlot trashSlot;
    private BasicFluidTank fluidTank;
    private IGasTank gasTank;
    private IInfusionTank infusionTank;
    private IPigmentTank pigmentTank;
    private ISlurryTank slurryTank;
    private MachineEnergyContainer<TrashCubeBlockEntity> energyContainer;

    private final LazyOptional<IItemHandler> itemCapability = LazyOptional.of(() -> VoidItemHandler.INSTANCE);
    private final LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> VoidFluidHandler.INSTANCE);
    private final LazyOptional<IStrictEnergyHandler> energyCapability = LazyOptional.of(() -> VoidEnergyHandler.INSTANCE);
    private final LazyOptional<IGasHandler> gasCapability = LazyOptional.of(() -> VoidGasHandler.INSTANCE);
    private final LazyOptional<IInfusionHandler> infusionCapability = LazyOptional.of(() -> VoidInfusionHandler.INSTANCE);
    private final LazyOptional<IPigmentHandler> pigmentCapability = LazyOptional.of(() -> VoidPigmentHandler.INSTANCE);
    private final LazyOptional<ISlurryHandler> slurryCapability = LazyOptional.of(() -> VoidSlurryHandler.INSTANCE);

    public TrashCubeBlockEntity(BlockPos pos, BlockState state) {
        super(TrashCube.BLOCK, pos, state);

        configComponent.setupInputConfig(TransmissionType.ITEM, trashSlot);
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        configComponent.setupInputConfig(TransmissionType.INFUSION, infusionTank);
        configComponent.setupInputConfig(TransmissionType.PIGMENT, pigmentTank);
        configComponent.setupInputConfig(TransmissionType.SLURRY, slurryTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
    }

    public BasicInventorySlot getTrashSlot() {
        return trashSlot;
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
        trashSlot = BasicInventorySlot.at(listener, 80, 35);
        builder.addSlot(trashSlot);
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
    protected void onUpdateServer() {
        super.onUpdateServer();

        if (!trashSlot.isEmpty()) {
            trashSlot.setStack(ItemStack.EMPTY);
        }
        if (!fluidTank.isEmpty()) {
            fluidTank.setStack(FluidStack.EMPTY);
        }
        if (!gasTank.isEmpty()) {
            gasTank.setEmpty();
        }
        if (!infusionTank.isEmpty()) {
            infusionTank.setEmpty();
        }
        if (!pigmentTank.isEmpty()) {
            pigmentTank.setEmpty();
        }
        if (!slurryTank.isEmpty()) {
            slurryTank.setEmpty();
        }
        if (!energyContainer.isEmpty()) {
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemCapability.cast();
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCapability.cast();
        }
        if (capability == mekanism.common.capabilities.Capabilities.STRICT_ENERGY) {
            return energyCapability.cast();
        }
        if (capability == mekanism.common.capabilities.Capabilities.GAS_HANDLER) {
            return gasCapability.cast();
        }
        if (capability == mekanism.common.capabilities.Capabilities.INFUSION_HANDLER) {
            return infusionCapability.cast();
        }
        if (capability == mekanism.common.capabilities.Capabilities.PIGMENT_HANDLER) {
            return pigmentCapability.cast();
        }
        if (capability == mekanism.common.capabilities.Capabilities.SLURRY_HANDLER) {
            return slurryCapability.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCapability.invalidate();
        fluidCapability.invalidate();
        energyCapability.invalidate();
        gasCapability.invalidate();
        infusionCapability.invalidate();
        pigmentCapability.invalidate();
        slurryCapability.invalidate();
    }
}
