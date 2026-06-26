package com.github.mochi7054.trashcube.item;

import java.util.List;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.item.ItemUpgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadioactiveUpgradeItem extends ItemUpgrade {

    public RadioactiveUpgradeItem(Properties properties) {
        super(getUpgradeTypeForceLoad(), properties);
    }

    private static Upgrade getUpgradeTypeForceLoad() {
        Upgrade.values();
        return com.github.mochi7054.trashcube.TrashCube.RADIOACTIVE_UPGRADE_TYPE;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull net.minecraft.world.item.Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (MekKeyHandler.isKeyPressed(MekanismKeyHandler.detailsKey)) {
            tooltip.add(Component.translatable("upgrade.mekanismtrashcube.radioactive.desc"));
            tooltip.add(Component.translatable("mekanism.gui.upgrade.max_installed", 1));
        } else {
            tooltip.add(
               MekanismLang.HOLD_FOR_DETAILS
                  .translateColored(EnumColor.GRAY, new Object[]{EnumColor.INDIGO, MekanismKeyHandler.detailsKey.getTranslatedKeyMessage()})
            );
        }
    }
}
