package com.github.mochi7054.trashcube.item;

import com.github.mochi7054.trashcube.TrashCube;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;

public class RadioactiveUpgradeItem extends ItemUpgrade {

    public RadioactiveUpgradeItem(Properties properties) {
        super(getUpgradeTypeForceLoad(), properties);
    }

    private static Upgrade getUpgradeTypeForceLoad() {
        Upgrade.values();
        return TrashCube.RADIOACTIVE_UPGRADE_TYPE;
    }
}
