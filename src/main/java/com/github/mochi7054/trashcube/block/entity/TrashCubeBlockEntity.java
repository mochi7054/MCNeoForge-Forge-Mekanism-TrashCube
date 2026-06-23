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
import mekanism.api.chemical.gas.BasicGasTank;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.BasicInfusionTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.pigment.BasicPigmentTank;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.slurry.BasicSlurryTank;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.GasTankHelper;
import mekanism.common.capabilities.holder.chemical.IGasTankHolder;
import mekanism.common.capabilities.holder.chemical.InfusionTankHelper;
import mekanism.common.capabilities.holder.chemical.IInfusionTankHolder;
import mekanism.common.capabilities.holder.chemical.PigmentTankHelper;
import mekanism.common.capabilities.holder.chemical.IPigmentTankHolder;
import mekanism.common.capabilities.holder.chemical.SlurryTankHelper;
import mekanism.common.capabilities.holder.chemical.ISlurryTankHolder;
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
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        trashSlot = BasicInventorySlot.at(listener, 80, 35);
        builder.addSlot(trashSlot);
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
    protected IGasTankHolder getInitialGasTanks(IContentsListener listener) {
        GasTankHelper builder = GasTankHelper.forSideWithConfig(this);
        gasTank = BasicGasTank.create(1000000L, listener);
        builder.addTank(gasTank);
        return builder.build();
    }

    @NotNull
    @Override
    protected IInfusionTankHolder getInitialInfusionTanks(IContentsListener listener) {
        InfusionTankHelper builder = InfusionTankHelper.forSideWithConfig(this);
        infusionTank = BasicInfusionTank.create(1000000L, listener);
        builder.addTank(infusionTank);
        return builder.build();
    }

    @NotNull
    @Override
    protected IPigmentTankHolder getInitialPigmentTanks(IContentsListener listener) {
        PigmentTankHelper builder = PigmentTankHelper.forSideWithConfig(this);
        pigmentTank = BasicPigmentTank.create(1000000L, listener);
        builder.addTank(pigmentTank);
        return builder.build();
    }

    @NotNull
    @Override
    protected ISlurryTankHolder getInitialSlurryTanks(IContentsListener listener) {
        SlurryTankHelper builder = SlurryTankHelper.forSideWithConfig(this);
        slurryTank = BasicSlurryTank.create(1000000L, listener);
        builder.addTank(slurryTank);
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
    protected boolean onUpdateServer() {
        boolean sendUpdate = super.onUpdateServer();

        if (!trashSlot.isEmpty()) {
            trashSlot.setStack(ItemStack.EMPTY);
            sendUpdate = true;
        }
        if (!fluidTank.isEmpty()) {
            fluidTank.setStack(FluidStack.EMPTY);
            sendUpdate = true;
        }
        if (!gasTank.isEmpty()) {
            gasTank.setEmpty();
            sendUpdate = true;
        }
        if (!infusionTank.isEmpty()) {
            infusionTank.setEmpty();
            sendUpdate = true;
        }
        if (!pigmentTank.isEmpty()) {
            pigmentTank.setEmpty();
            sendUpdate = true;
        }
        if (!slurryTank.isEmpty()) {
            slurryTank.setEmpty();
            sendUpdate = true;
        }
        if (!energyContainer.isEmpty()) {
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
