package com.github.mochi7054.trashcube.mixin;

import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = Upgrade.class, remap = false)
public interface UpgradeInvoker {
    @Invoker("<init>")
    static Upgrade createUpgrade(String name, int ordinal, String id, APILang nameKey, APILang descKey, int maxStack, EnumColor color) {
        throw new UnsupportedOperationException();
    }
}
