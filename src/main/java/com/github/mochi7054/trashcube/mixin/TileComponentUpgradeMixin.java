package com.github.mochi7054.trashcube.mixin;

import java.util.Set;
import mekanism.api.Upgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.base.TileEntityMekanism;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;

@Mixin(value = TileComponentUpgrade.class, remap = false)
public abstract class TileComponentUpgradeMixin {

    @Shadow
    @Final
    private TileEntityMekanism tile;

    @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
    private void onSupports(Upgrade upgrade, CallbackInfoReturnable<Boolean> cir) {
        if (upgrade != null && upgrade.name().equals("RADIOACTIVE")) {
            if (tile instanceof TrashCubeBlockEntity) {
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getSupportedTypes", at = @At("RETURN"), cancellable = true)
    private void onGetSupportedTypes(CallbackInfoReturnable<Set<Upgrade>> cir) {
        if (tile instanceof TrashCubeBlockEntity) {
            Set<Upgrade> supported = cir.getReturnValue();
            if (supported != null && !supported.contains(TrashCube.RADIOACTIVE_UPGRADE_TYPE)) {
                Set<Upgrade> mutable = new java.util.HashSet<>(supported);
                mutable.add(TrashCube.RADIOACTIVE_UPGRADE_TYPE);
                cir.setReturnValue(java.util.Collections.unmodifiableSet(mutable));
            }
        }
    }
}
