package com.github.mochi7054.trashcube;

import com.github.mochi7054.trashcube.block.TrashCubeBlock;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import com.github.mochi7054.trashcube.item.RadioactiveUpgradeItem;
import com.github.mochi7054.trashcube.item.TrashCubeBlockItem;
import mekanism.api.Upgrade;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(TrashCube.MODID)
public class TrashCube {
    public static final String MODID = "mekanismtrashcube";

    public static final mekanism.common.registration.impl.BlockDeferredRegister BLOCKS = new mekanism.common.registration.impl.BlockDeferredRegister(MODID);
    public static final mekanism.common.registration.impl.ItemDeferredRegister ITEMS = new mekanism.common.registration.impl.ItemDeferredRegister(MODID);
    public static final mekanism.common.registration.impl.TileEntityTypeDeferredRegister BLOCK_ENTITIES = new mekanism.common.registration.impl.TileEntityTypeDeferredRegister(MODID);
    public static final mekanism.common.registration.impl.ContainerTypeDeferredRegister MENU_TYPES = new mekanism.common.registration.impl.ContainerTypeDeferredRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final mekanism.common.registration.impl.BlockRegistryObject<TrashCubeBlock, TrashCubeBlockItem> BLOCK =
            BLOCKS.register("trash_cube",
                    () -> new TrashCubeBlock(BlockBehaviour.Properties.of().strength(3.5F).requiresCorrectToolForDrops().noOcclusion()),
                    TrashCubeBlockItem::new);

    public static Upgrade RADIOACTIVE_UPGRADE_TYPE;

    public static final mekanism.common.registration.impl.ItemRegistryObject<RadioactiveUpgradeItem> RADIOACTIVE_UPGRADE =
            ITEMS.register("radioactive_upgrade", () -> new RadioactiveUpgradeItem(new Item.Properties().stacksTo(64)));

    public static final mekanism.common.registration.impl.TileEntityTypeRegistryObject<TrashCubeBlockEntity> TILE =
            BLOCK_ENTITIES.register(BLOCK, TrashCubeBlockEntity::new,
                    mekanism.common.tile.base.TileEntityMekanism::tickServer,
                    mekanism.common.tile.base.TileEntityMekanism::tickClient);

    public static final mekanism.common.registration.impl.ContainerTypeRegistryObject<TrashCubeMenu> CONTAINER_TYPE =
            MENU_TYPES.register("trash_cube",
                    TrashCubeBlockEntity.class,
                    TrashCubeMenu::new);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.mekanismtrashcube"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BLOCK.asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BLOCK.asItem());
                output.accept(RADIOACTIVE_UPGRADE.asItem());
            }).build());

    public TrashCube() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
