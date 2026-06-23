package com.github.mochi7054.trashcube.block;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.api.math.FloatingLong;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TrashCubeBlock extends BlockTile<TrashCubeBlockEntity, BlockTypeTile<TrashCubeBlockEntity>> {

    public TrashCubeBlock(BlockBehaviour.Properties properties) {
        super(createBlockType(), properties);
    }

    private static BlockTypeTile<TrashCubeBlockEntity> createBlockType() {
        BlockTypeTile<TrashCubeBlockEntity> blockType = new BlockTypeTile<>(
            () -> TrashCube.TILE,
            () -> "container.mekanismtrashcube.trash_cube"
        );

        blockType.add(
            new mekanism.common.block.attribute.AttributeEnergy(() -> FloatingLong.ZERO, () -> FloatingLong.MAX_VALUE),
            mekanism.common.block.attribute.AttributeUpgradeSupport.ANCHOR_ONLY,
            mekanism.common.block.attribute.AttributeSideConfig.create(
                TransmissionType.ITEM,
                TransmissionType.ENERGY,
                TransmissionType.FLUID,
                TransmissionType.GAS,
                TransmissionType.INFUSION,
                TransmissionType.PIGMENT,
                TransmissionType.SLURRY
            ),
            mekanism.common.block.attribute.Attributes.ACTIVE,
            mekanism.common.block.attribute.Attributes.REDSTONE,
            mekanism.common.block.attribute.Attributes.SECURITY,
            new mekanism.common.block.attribute.AttributeStateFacing(),
            new mekanism.common.block.attribute.AttributeGui(() -> TrashCube.CONTAINER_TYPE, () -> "container.mekanismtrashcube.trash_cube")
        );

        return blockType;
    }
}
