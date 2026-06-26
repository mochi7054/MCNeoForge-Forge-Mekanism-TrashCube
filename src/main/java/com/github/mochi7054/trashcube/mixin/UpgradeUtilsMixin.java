package com.github.mochi7054.trashcube.mixin;

import java.util.Collections;
import java.util.List;
import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.mochi7054.trashcube.TrashCube;

@Mixin(value = UpgradeUtils.class, remap = false)
public class UpgradeUtilsMixin {

    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    private static void onGetItem(Upgrade upgrade, CallbackInfoReturnable<Holder<Item>> cir) {
        if (upgrade == TrashCube.RADIOACTIVE_UPGRADE_TYPE) {
            cir.setReturnValue(TrashCube.RADIOACTIVE_UPGRADE);
        }
    }

    @Inject(method = "getInfo", at = @At("HEAD"), cancellable = true)
    private static void onGetInfo(net.minecraft.world.level.block.entity.BlockEntity tile, Upgrade upgrade, CallbackInfoReturnable<List<?>> cir) {
        if (upgrade == TrashCube.RADIOACTIVE_UPGRADE_TYPE) {
            cir.setReturnValue(Collections.singletonList(
                Component.translatable("gui.mekanismtrashcube.upgrades.effect")
            ));
        }
    }
}
