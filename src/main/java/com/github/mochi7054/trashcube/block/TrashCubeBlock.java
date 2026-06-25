package com.github.mochi7054.trashcube.block;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.api.text.ILangEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;

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
        };
    }

    private static BlockTypeTile<TrashCubeBlockEntity> createBlockType() {
        BlockTypeTile<TrashCubeBlockEntity> blockType = new BlockTypeTile<>(
            () -> TrashCube.TILE,
            new mekanism.api.text.ILangEntry() {
                @Override
                public String getTranslationKey() {
                    return "container.mekanismtrashcube.trash_cube";
                }
            }
        );

        blockType.add(
            new mekanism.common.block.attribute.AttributeEnergy(() -> 0L, () -> 1000000000000L),
            mekanism.common.block.attribute.AttributeUpgradeSupport.create(mekanism.api.Upgrade.ANCHOR, com.github.mochi7054.trashcube.TrashCube.RADIOACTIVE_UPGRADE_TYPE),
            mekanism.common.block.attribute.AttributeSideConfig.create(
                TransmissionType.ITEM,
                TransmissionType.ENERGY,
                TransmissionType.FLUID,
                TransmissionType.CHEMICAL
            ),
            mekanism.common.block.attribute.Attributes.ACTIVE,
            mekanism.common.block.attribute.Attributes.REDSTONE,
            mekanism.common.block.attribute.Attributes.SECURITY,
            new mekanism.common.block.attribute.AttributeStateFacing(),
            new mekanism.common.block.attribute.AttributeGui(() -> TrashCube.CONTAINER_TYPE, new mekanism.api.text.ILangEntry() {
                @Override
                public String getTranslationKey() {
                    return "container.mekanismtrashcube.trash_cube";
                }
            })
        );

        return blockType;
    }
}
