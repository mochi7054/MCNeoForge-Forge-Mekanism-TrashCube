package com.github.mochi7054.trashcube.item;

import mekanism.api.Upgrade;
import mekanism.common.item.interfaces.IUpgradeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RadioactiveUpgradeItem extends Item implements IUpgradeItem {

    public RadioactiveUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public Upgrade getUpgradeType(ItemStack stack) {
        return Upgrade.FILTER;
    }
}
