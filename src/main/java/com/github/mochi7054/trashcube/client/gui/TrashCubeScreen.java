package com.github.mochi7054.trashcube.client.gui;

import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;
import com.github.mochi7054.trashcube.inventory.TrashCubeMenu;
import mekanism.client.gui.GuiConfigurableTile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TrashCubeScreen extends GuiConfigurableTile<TrashCubeBlockEntity, TrashCubeMenu> {

    public TrashCubeScreen(TrashCubeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
        this.titleLabelY = 6;
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        // The side tabs (Security, Redstone, Upgrade, Side Config) are automatically added
        // by the superclass GuiConfigurableTile based on the Block Entity's attributes.
    }
}
