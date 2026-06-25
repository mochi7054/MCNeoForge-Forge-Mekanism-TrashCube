package com.github.mochi7054.trashcube.item;

import com.github.mochi7054.trashcube.TrashCube;
import mekanism.common.item.ItemUpgrade;

public class RadioactiveUpgradeItem extends ItemUpgrade {

    public RadioactiveUpgradeItem(Properties properties) {
        super(getUpgradeTypeForceLoad(), properties);
    }

    private static mekanism.api.Upgrade getUpgradeTypeForceLoad() {
        // Force load Upgrade class to trigger UpgradeMixin <clinit> injection
        mekanism.api.Upgrade.values();
        return TrashCube.RADIOACTIVE_UPGRADE_TYPE;
    }
}
