package com.github.mochi7054.trashcube.mixin;

import java.util.Collections;
import java.util.List;
import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.mochi7054.trashcube.TrashCube;

@Mixin(value = UpgradeUtils.class, remap = false)
public class UpgradeUtilsMixin {
    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    private static void onGetItem(Upgrade upgrade, CallbackInfoReturnable<Holder<?>> cir) {
        if (cir.isCancelled()) {
            return;
        }
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(TrashCube.RADIOACTIVE_UPGRADE);
        }
    }

    @Inject(method = "getStack(Lmekanism/api/Upgrade;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void onGetStackSingle(Upgrade upgrade, CallbackInfoReturnable<net.minecraft.world.item.ItemStack> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(new net.minecraft.world.item.ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get()));
        }
    }

    @Inject(method = "getStack(Lmekanism/api/Upgrade;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void onGetStackCount(Upgrade upgrade, int count, CallbackInfoReturnable<net.minecraft.world.item.ItemStack> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(new net.minecraft.world.item.ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get(), count));
        }
    }

    @Inject(method = "getInfo", at = @At("HEAD"), cancellable = true)
    private static void onGetInfo(net.minecraft.world.level.block.entity.BlockEntity tile, Upgrade upgrade, CallbackInfoReturnable<List<?>> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(Collections.singletonList(
                Component.translatable("gui.mekanismtrashcube.upgrades.effect")
            ));
        }
    }

    @Inject(method = "getExpScaledInfo", at = @At("HEAD"), cancellable = true)
    private static void onGetExpScaledInfo(mekanism.common.tile.interfaces.IUpgradeTile tile, Upgrade upgrade, CallbackInfoReturnable<List<?>> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(Collections.singletonList(
                Component.translatable("gui.mekanismtrashcube.upgrades.effect")
            ));
        }
    }

    @Inject(method = "getMultScaledInfo", at = @At("HEAD"), cancellable = true)
    private static void onGetMultScaledInfo(mekanism.common.tile.interfaces.IUpgradeTile tile, Upgrade upgrade, CallbackInfoReturnable<List<?>> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            cir.setReturnValue(Collections.singletonList(
                Component.translatable("gui.mekanismtrashcube.upgrades.effect")
            ));
        }
    }
}
