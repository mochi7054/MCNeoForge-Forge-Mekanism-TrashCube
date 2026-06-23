package com.github.mochi7054.trashcube.inventory;

import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class TrashCubeMenu extends MekanismTileContainer<TrashCubeBlockEntity> {

    public TrashCubeMenu(int containerId, Inventory inv, TrashCubeBlockEntity tile) {
        super(tile.getContainerType(), containerId, inv, tile);
    }

    public TrashCubeMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv, getTileFromBuf(buf, TrashCubeBlockEntity.class));
    }

    @Override
    protected void addInventorySlots(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = 9 + col + row * 9;
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                this.addSlot(new mekanism.common.inventory.container.slot.MainInventorySlot(playerInventory, slotIndex, x, y));
            }
        }

        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            this.addSlot(this.createHotBarSlot(playerInventory, col, x, 142));
        }
    }
}
