package com.github.mochi7054.trashcube.item;

import com.github.mochi7054.trashcube.block.TrashCubeBlock;
import mekanism.common.item.block.ItemBlockTooltip;
import net.minecraft.world.item.Item;

public class TrashCubeBlockItem extends ItemBlockTooltip<TrashCubeBlock> {
    public TrashCubeBlockItem(TrashCubeBlock block) {
        super(block, true, new Item.Properties());
    }

    public TrashCubeBlockItem(TrashCubeBlock block, Item.Properties properties) {
        super(block, true, properties);
    }
}
