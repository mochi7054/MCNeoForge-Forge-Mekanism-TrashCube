package com.github.mochi7054.trashcube.block;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.api.math.FloatingLong;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.Collections;

public class TrashCubeBlock extends BlockTile<TrashCubeBlockEntity, BlockTypeTile<TrashCubeBlockEntity>> implements IHasDescription {

    public TrashCubeBlock(BlockBehaviour.Properties properties) {
        super(createBlockType(), properties);
    }

    @Override
    public ILangEntry getDescription() {
        return new ILangEntry() {
            @Override
            public String getTranslationKey() {
                return "description.mekanismtrashcube.trash_cube";
            }
            @Override
            public MutableComponent translate(Object... args) {
                return Component.translatable(getTranslationKey(), args);
            }
            @Override
            public MutableComponent translateColored(EnumColor color, Object... args) {
                return translate(args).withStyle(style -> style.withColor(color.getColor()));
            }
            @Override
            public MutableComponent translateColored(TextColor color, Object... args) {
                return translate(args).withStyle(style -> style.withColor(color));
            }
        };
    }

    private static BlockTypeTile<TrashCubeBlockEntity> createBlockType() {
        Upgrade.values(); // Force classloading to initialize RADIOACTIVE_UPGRADE_TYPE

        ILangEntry langEntry = new ILangEntry() {
            @Override
            public String getTranslationKey() {
                return "container.mekanismtrashcube.trash_cube";
            }
            @Override
            public MutableComponent translate(Object... args) {
                return Component.translatable(getTranslationKey(), args);
            }
            @Override
            public MutableComponent translateColored(EnumColor color, Object... args) {
                return translate(args).withStyle(style -> style.withColor(color.getColor()));
            }
            @Override
            public MutableComponent translateColored(TextColor color, Object... args) {
                return translate(args).withStyle(style -> style.withColor(color));
            }
        };

        BlockTypeTile<TrashCubeBlockEntity> blockType = new BlockTypeTile<>(
            () -> TrashCube.TILE,
            langEntry
        );

        blockType.add(
            new mekanism.common.block.attribute.AttributeEnergy(() -> FloatingLong.ZERO, () -> FloatingLong.MAX_VALUE),
            new mekanism.common.block.attribute.AttributeUpgradeSupport(java.util.Set.of(Upgrade.ANCHOR, TrashCube.RADIOACTIVE_UPGRADE_TYPE)),
            mekanism.common.block.attribute.Attributes.ACTIVE,
            mekanism.common.block.attribute.Attributes.REDSTONE,
            mekanism.common.block.attribute.Attributes.SECURITY,
            new mekanism.common.block.attribute.AttributeStateFacing(),
            new mekanism.common.block.attribute.AttributeGui(() -> TrashCube.CONTAINER_TYPE, langEntry)
        );

        return blockType;
    }
}

