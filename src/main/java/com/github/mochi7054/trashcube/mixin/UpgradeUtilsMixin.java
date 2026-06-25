package com.github.mochi7054.trashcube.mixin;

import java.util.Collections;
import java.util.List;
import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.mochi7054.trashcube.TrashCube;

@Mixin(value = UpgradeUtils.class, remap = false)
public class UpgradeUtilsMixin {

    @Inject(method = "getStack(Lmekanism/api/Upgrade;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void onGetStackSingle(Upgrade upgrade, CallbackInfoReturnable<ItemStack> cir) {
        if (upgrade == TrashCube.RADIOACTIVE_UPGRADE_TYPE) {
            cir.setReturnValue(new ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get()));
        }
    }

    @Inject(method = "getStack(Lmekanism/api/Upgrade;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void onGetStackCount(Upgrade upgrade, int count, CallbackInfoReturnable<ItemStack> cir) {
        if (upgrade == TrashCube.RADIOACTIVE_UPGRADE_TYPE) {
            cir.setReturnValue(new ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get(), count));
        }
    }

    @Inject(method = "getInfo", at = @At("HEAD"), cancellable = true)
    private static void onGetInfo(net.minecraft.world.level.block.entity.BlockEntity tile, Upgrade upgrade, CallbackInfoReturnable<List<Component>> cir) {
        if (upgrade == TrashCube.RADIOACTIVE_UPGRADE_TYPE) {
            cir.setReturnValue(Collections.singletonList(
                Component.translatable("gui.mekanismtrashcube.upgrades.effect")
            ));
        }
    }
}
