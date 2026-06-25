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
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
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
import mekanism.api.Upgrade;
import mekanism.api.Action;
import mekanism.api.AutomationType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TrashCubeBlockEntity extends TileEntityMekanism implements MenuProvider, ISideConfiguration {

    public mekanism.common.tile.component.TileComponentConfig configComponent;
    public mekanism.common.tile.component.TileComponentEjector ejectorComponent;

    private BasicInventorySlot[] trashSlots;
    private BasicFluidTank fluidTank;
    private IGasTank gasTank;
    private IInfusionTank infusionTank;
    private IPigmentTank pigmentTank;
    private ISlurryTank slurryTank;
    private MachineEnergyContainer<TrashCubeBlockEntity> energyContainer;

    private void initConfigComponent() {
        configComponent = new mekanism.common.tile.component.TileComponentConfig(this,
            mekanism.common.lib.transmitter.TransmissionType.ITEM,
            mekanism.common.lib.transmitter.TransmissionType.FLUID,
            mekanism.common.lib.transmitter.TransmissionType.GAS,
            mekanism.common.lib.transmitter.TransmissionType.INFUSION,
            mekanism.common.lib.transmitter.TransmissionType.PIGMENT,
            mekanism.common.lib.transmitter.TransmissionType.SLURRY,
            mekanism.common.lib.transmitter.TransmissionType.ENERGY
        );
    }

    public TrashCubeBlockEntity(BlockPos pos, BlockState state) {
        super(TrashCube.BLOCK, pos, state);

        if (configComponent == null) {
            initConfigComponent();
        }
        ejectorComponent = new mekanism.common.tile.component.TileComponentEjector(this);

        mekanism.common.tile.component.config.ConfigInfo itemConfig = configComponent.getConfig(mekanism.common.lib.transmitter.TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(mekanism.common.tile.component.config.DataType.INPUT, mekanism.common.tile.component.TileComponentConfig.createInfo(mekanism.common.lib.transmitter.TransmissionType.ITEM, true, false, Arrays.asList(trashSlots)));
            itemConfig.fill(mekanism.common.tile.component.config.DataType.INPUT);
            itemConfig.setCanEject(false);
        }
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.GAS, gasTank);
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.INFUSION, infusionTank);
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.PIGMENT, pigmentTank);
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.SLURRY, slurryTank);
        configComponent.setupInputConfig(mekanism.common.lib.transmitter.TransmissionType.ENERGY, energyContainer);
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
        if (configComponent == null) {
            initConfigComponent();
        }
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        trashSlots = new BasicInventorySlot[1];
        trashSlots[0] = new TrashInventorySlot(listener, 80, 35);
        builder.addSlot(trashSlots[0]);
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
        gasTank = ChemicalTankBuilder.GAS.createWithValidator(1000000L, new mekanism.api.chemical.attribute.ChemicalAttributeValidator() {
            @Override
            public boolean validate(mekanism.api.chemical.attribute.ChemicalAttribute attribute) {
                if (attribute instanceof mekanism.api.chemical.gas.attribute.GasAttributes.Radiation) {
                    return hasRadioactiveUpgrade();
                }
                return !attribute.needsValidation();
            }
        }, listener);
        builder.addTank(gasTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper.forSideInfusionWithConfig(this::getDirection, this::getConfig);
        infusionTank = ChemicalTankBuilder.INFUSION.createWithValidator(1000000L, new mekanism.api.chemical.attribute.ChemicalAttributeValidator() {
            @Override
            public boolean validate(mekanism.api.chemical.attribute.ChemicalAttribute attribute) {
                if (attribute instanceof mekanism.api.chemical.gas.attribute.GasAttributes.Radiation) {
                    return hasRadioactiveUpgrade();
                }
                return !attribute.needsValidation();
            }
        }, listener);
        builder.addTank(infusionTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper.forSidePigmentWithConfig(this::getDirection, this::getConfig);
        pigmentTank = ChemicalTankBuilder.PIGMENT.createWithValidator(1000000L, new mekanism.api.chemical.attribute.ChemicalAttributeValidator() {
            @Override
            public boolean validate(mekanism.api.chemical.attribute.ChemicalAttribute attribute) {
                if (attribute instanceof mekanism.api.chemical.gas.attribute.GasAttributes.Radiation) {
                    return hasRadioactiveUpgrade();
                }
                return !attribute.needsValidation();
            }
        }, listener);
        builder.addTank(pigmentTank);
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper.forSideSlurryWithConfig(this::getDirection, this::getConfig);
        slurryTank = ChemicalTankBuilder.SLURRY.createWithValidator(1000000L, new mekanism.api.chemical.attribute.ChemicalAttributeValidator() {
            @Override
            public boolean validate(mekanism.api.chemical.attribute.ChemicalAttribute attribute) {
                if (attribute instanceof mekanism.api.chemical.gas.attribute.GasAttributes.Radiation) {
                    return hasRadioactiveUpgrade();
                }
                return !attribute.needsValidation();
            }
        }, listener);
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
        if (ejectorComponent != null) {
            ejectorComponent.tickServer();
        }

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

    @Override
    public mekanism.common.tile.component.TileComponentConfig getConfig() {
        return configComponent;
    }

    @Override
    public mekanism.common.tile.component.TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public net.minecraft.nbt.CompoundTag getConfigurationData(net.minecraft.world.entity.player.Player player) {
        net.minecraft.nbt.CompoundTag tag = super.getConfigurationData(player);
        if (configComponent != null) {
            configComponent.write(tag);
        }
        if (ejectorComponent != null) {
            ejectorComponent.write(tag);
        }
        return tag;
    }

    @Override
    public void setConfigurationData(net.minecraft.world.entity.player.Player player, net.minecraft.nbt.CompoundTag tag) {
        super.setConfigurationData(player, tag);
        if (configComponent != null) {
            configComponent.read(tag);
        }
        if (ejectorComponent != null) {
            ejectorComponent.read(tag);
        }
    }

    @Override
    public Set<mekanism.api.Upgrade> getSupportedUpgrade() {
        return Set.of(TrashCube.RADIOACTIVE_UPGRADE_TYPE);
    }

    public boolean hasRadioactiveUpgrade() {
        if (supportsUpgrades() && getComponent() != null) {
            return getComponent().getUpgrades(TrashCube.RADIOACTIVE_UPGRADE_TYPE) > 0;
        }
        return false;
    }

    public static class TrashInventorySlot extends BasicInventorySlot {
        public TrashInventorySlot(IContentsListener listener, int x, int y) {
            super(
                (java.util.function.Predicate<ItemStack>) stack -> true,
                (java.util.function.Predicate<ItemStack>) stack -> true,
                (java.util.function.Predicate<ItemStack>) stack -> true,
                listener,
                x,
                y
            );
        }

        @Override
        public int getLimit(ItemStack stack) {
            return Integer.MAX_VALUE;
        }
    }
}

