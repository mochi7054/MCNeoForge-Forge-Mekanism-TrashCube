package com.github.mochi7054.trashcube.mixin;

import java.util.Arrays;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.mochi7054.trashcube.lang.TrashCubeLangEntry;
import com.github.mochi7054.trashcube.TrashCube;

@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeMixin {

    @Shadow
    @Final
    @Mutable
    private static Upgrade[] $VALUES;

    @Shadow
    @Final
    @Mutable
    private static java.util.function.IntFunction<Upgrade> BY_ID;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClinit(CallbackInfo ci) {
        ILangEntry langKey = new TrashCubeLangEntry("upgrade.mekanismtrashcube.radioactive");
        ILangEntry descLangKey = new TrashCubeLangEntry("upgrade.mekanismtrashcube.radioactive.desc");

        Upgrade[] oldValues = $VALUES;
        int newOrdinal = oldValues.length;

        Upgrade radioactiveUpgrade = UpgradeInvoker.createUpgrade("RADIOACTIVE", newOrdinal, "radioactive", langKey, descLangKey, 1, EnumColor.DARK_GREEN);
        TrashCube.RADIOACTIVE_UPGRADE_TYPE = radioactiveUpgrade;

        // Append to $VALUES
        Upgrade[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
        newValues[oldValues.length] = radioactiveUpgrade;
        $VALUES = newValues;

        // Override BY_ID
        java.util.function.IntFunction<Upgrade> originalById = BY_ID;
        final int targetId = newOrdinal;
        BY_ID = id -> {
            if (id == targetId) {
                return radioactiveUpgrade;
            }
            return originalById != null ? originalById.apply(id) : null;
        };
    }
}
