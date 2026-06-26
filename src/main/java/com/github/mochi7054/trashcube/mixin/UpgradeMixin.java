package com.github.mochi7054.trashcube.mixin;

import java.util.Arrays;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.enumexpansion.TrashCubeAPILang;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;

@Mixin(value = Upgrade.class, remap = false)
public class UpgradeMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static Upgrade[] $VALUES;

    @Invoker("<init>")
    private static Upgrade invokeInit(String internalName, int ordinal, String name, APILang langKey,
            APILang descLangKey, int maxStack, EnumColor color) {
        return null;
    }

    @Unique
    private static Upgrade createNew(String name, APILang langKey, APILang descLangKey,
            int maxStack, EnumColor color) {
        int index = $VALUES.length;
        Upgrade result = invokeInit(name.toUpperCase(), index, name, langKey, descLangKey, maxStack, color);
        Upgrade[] newVALUES = Arrays.copyOf($VALUES, index + 1);
        newVALUES[index] = result;
        $VALUES = newVALUES;
        return result;
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "byIndexStatic", at = @At("HEAD"), cancellable = true)
    private static void onByIndexStatic(int index, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Upgrade> cir) {
        Upgrade[] vals = Upgrade.values();
        int len = vals.length;
        int r = index % len;
        cir.setReturnValue(vals[r < 0 ? r + len : r]);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        TrashCube.RADIOACTIVE_UPGRADE_TYPE = createNew(
            "radioactive",
            TrashCubeAPILang.UPGRADE_RADIOACTIVE,
            TrashCubeAPILang.UPGRADE_RADIOACTIVE_DESCRIPTION,
            1,
            EnumColor.DARK_GREEN
        );
    }
}
