package com.github.mochi7054.trashcube.mixin;

import java.util.Arrays;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
    private static Upgrade[] UPGRADES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClinit(CallbackInfo ci) {
        Upgrade[] oldValues = $VALUES;
        int newOrdinal = oldValues.length;

        Upgrade radioactiveUpgrade = UpgradeInvoker.createUpgrade("RADIOACTIVE", newOrdinal, "radioactive", APILang.UPGRADE_FILTER, APILang.UPGRADE_FILTER_DESCRIPTION, 1, EnumColor.DARK_GREEN);
        TrashCube.RADIOACTIVE_UPGRADE_TYPE = radioactiveUpgrade;

        // Append to $VALUES
        Upgrade[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
        newValues[oldValues.length] = radioactiveUpgrade;
        $VALUES = newValues;
        UPGRADES = newValues;
    }

    @Inject(method = "getTranslationKey", at = @At("HEAD"), cancellable = true)
    private void onGetTranslationKey(CallbackInfoReturnable<String> cir) {
        if (((Upgrade) (Object) this).name().equals("RADIOACTIVE")) {
            cir.setReturnValue("upgrade.mekanismtrashcube.radioactive");
        }
    }

    @Inject(method = "getDescription", at = @At("HEAD"), cancellable = true)
    private void onGetDescription(CallbackInfoReturnable<Component> cir) {
        if (((Upgrade) (Object) this).name().equals("RADIOACTIVE")) {
            cir.setReturnValue(Component.translatable("upgrade.mekanismtrashcube.radioactive.desc"));
        }
    }
}
