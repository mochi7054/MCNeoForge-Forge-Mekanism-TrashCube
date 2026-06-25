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
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.DataType;
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

public class TrashCubeBlockEntity extends TileEntityMekanism implements MenuProvider, ISideConfiguration {

    public TileComponentConfig configComponent;
    public mekanism.common.tile.component.TileComponentEjector ejectorComponent;

    private BasicInventorySlot[] trashSlots;
    private BasicFluidTank fluidTank;
    private IChemicalTank chemicalTank;
    private MachineEnergyContainer<TrashCubeBlockEntity> energyContainer;

    private void initConfigComponent() {
        configComponent = new TileComponentConfig(this, java.util.Set.of(
            TransmissionType.ITEM,
            TransmissionType.FLUID,
            TransmissionType.CHEMICAL,
            TransmissionType.ENERGY
        ));
    }

    public TrashCubeBlockEntity(BlockPos pos, BlockState state) {
        super(state.getBlockHolder(), pos, state);

        if (configComponent == null) {
            initConfigComponent();
        }
        ejectorComponent = new mekanism.common.tile.component.TileComponentEjector(this);

        // Setup side configs for Item, Fluid, Chemical, Energy
        mekanism.common.tile.component.config.ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, mekanism.common.tile.component.TileComponentConfig.createInfo(TransmissionType.ITEM, true, false, Arrays.asList(trashSlots)));
            itemConfig.setCanEject(false);
        }
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
        if (configComponent == null) {
            initConfigComponent();
        }
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        trashSlots = new BasicInventorySlot[1];
        trashSlots[0] = new TrashInventorySlot(listener, 80, 35);
        builder.addSlot(trashSlots[0]);
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
        chemicalTank = BasicChemicalTank.createWithValidator(1000000L, new mekanism.api.chemical.attribute.ChemicalAttributeValidator() {
            @Override
            public boolean validate(mekanism.api.chemical.attribute.ChemicalAttribute attribute) {
                if (attribute instanceof mekanism.api.chemical.attribute.ChemicalAttributes.Radiation) {
                    return hasRadioactiveUpgrade();
                }
                return !attribute.needsValidation();
            }
        }, listener);
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
        if (ejectorComponent != null) {
            ejectorComponent.tickServer();
        }

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

    @Override
    public TileComponentConfig getConfig() {
        return configComponent;
    }

    @Override
    public mekanism.common.tile.component.TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public net.minecraft.nbt.CompoundTag getConfigurationData(net.minecraft.core.HolderLookup.Provider provider, net.minecraft.world.entity.player.Player player) {
        net.minecraft.nbt.CompoundTag tag = super.getConfigurationData(provider, player);
        if (configComponent != null) {
            configComponent.write(tag, provider);
        }
        if (ejectorComponent != null) {
            ejectorComponent.write(tag, provider);
        }
        return tag;
    }

    @Override
    public void setConfigurationData(net.minecraft.core.HolderLookup.Provider provider, net.minecraft.world.entity.player.Player player, net.minecraft.nbt.CompoundTag tag) {
        super.setConfigurationData(provider, player, tag);
        if (configComponent != null) {
            configComponent.read(tag, provider);
        }
        if (ejectorComponent != null) {
            ejectorComponent.read(tag, provider);
        }
    }

    public boolean hasRadioactiveUpgrade() {
        if (supportsUpgrades()) {
            return getComponent().getUpgrades(mekanism.api.Upgrade.FILTER) > 0;
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
