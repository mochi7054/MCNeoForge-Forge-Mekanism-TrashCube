package com.github.mochi7054.trashcube;

import com.github.mochi7054.trashcube.block.TrashCubeBlock;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(TrashCube.MODID)
public class TrashCube {
    public static final String MODID = "mekanismtrashcube";

    public static final mekanism.common.registration.impl.BlockDeferredRegister BLOCKS = new mekanism.common.registration.impl.BlockDeferredRegister(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final mekanism.common.registration.impl.TileEntityTypeDeferredRegister BLOCK_ENTITIES = new mekanism.common.registration.impl.TileEntityTypeDeferredRegister(MODID);
    public static final mekanism.common.registration.impl.ContainerTypeDeferredRegister MENU_TYPES = new mekanism.common.registration.impl.ContainerTypeDeferredRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final mekanism.common.registration.impl.BlockRegistryObject<TrashCubeBlock, BlockItem> BLOCK =
            BLOCKS.register("trash_cube",
                    () -> new TrashCubeBlock(BlockBehaviour.Properties.of().strength(3.5F).noOcclusion()),
                    BlockItem::new);

    public static final mekanism.common.registration.impl.TileEntityTypeRegistryObject<TrashCubeBlockEntity> TILE =
            BLOCK_ENTITIES.mekBuilder(BLOCK, TrashCubeBlockEntity::new)
                    .clientTicker(mekanism.common.tile.base.TileEntityMekanism::tickClient)
                    .serverTicker(mekanism.common.tile.base.TileEntityMekanism::tickServer)
                    .build();

    public static final mekanism.common.registration.impl.ContainerTypeRegistryObject<TrashCubeMenu> CONTAINER_TYPE =
            MENU_TYPES.register("trash_cube",
                    TrashCubeBlockEntity.class,
                    TrashCubeMenu::new);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.mekanismtrashcube"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BLOCK.asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BLOCK.asItem());
            }).build());

    public TrashCube(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
            TILE.get(),
            (be, side) -> com.github.mochi7054.trashcube.inventory.VoidItemHandler.INSTANCE
        );
        event.registerBlockEntity(
            net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
            TILE.get(),
            (be, side) -> com.github.mochi7054.trashcube.fluid.VoidFluidHandler.INSTANCE
        );
        event.registerBlockEntity(
            mekanism.common.capabilities.Capabilities.CHEMICAL.block(),
            TILE.get(),
            (be, side) -> new com.github.mochi7054.trashcube.chemical.VoidChemicalHandler()
        );
        event.registerBlockEntity(
            mekanism.common.capabilities.Capabilities.STRICT_ENERGY.block(),
            TILE.get(),
            (be, side) -> com.github.mochi7054.trashcube.energy.VoidEnergyHandler.INSTANCE
        );
    }
}
