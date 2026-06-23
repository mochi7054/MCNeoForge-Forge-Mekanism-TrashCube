package com.github.mochi7054.trashcube.inventory;

import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class TrashCubeMenu extends MekanismTileContainer<TrashCubeBlockEntity> {

    public TrashCubeMenu(int containerId, Inventory inv, TrashCubeBlockEntity tile) {
        super(tile.getContainerType(), containerId, inv, tile);
    }

    public TrashCubeMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, getTileFromBuf(buf, TrashCubeBlockEntity.class, inv));
    }

    private static <TILE extends mekanism.common.tile.base.TileEntityMekanism> TILE getTileFromBuf(RegistryFriendlyByteBuf buf, Class<TILE> type, Inventory inv) {
        if (buf == null) {
            return null;
        }
        return mekanism.common.util.WorldUtils.getTileEntity(type, inv.player.level(), buf.readBlockPos());
    }

    @Override
    protected void addInventorySlots(Inventory playerInventory) {
        // Standard player inventory slots (3 rows of 9 slots)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = 9 + col + row * 9;
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                this.addSlot(new mekanism.common.inventory.container.slot.MainInventorySlot(playerInventory, slotIndex, x, y));
            }
        }

        // Hotbar (9 slots)
        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            this.addSlot(this.createHotBarSlot(playerInventory, col, x, 142));
        }
    }
}
