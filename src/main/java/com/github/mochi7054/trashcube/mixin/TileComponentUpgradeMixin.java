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

    @Shadow
    @Final
    private java.util.Map<Upgrade, Integer> upgrades;

    @Shadow
    @Final
    private mekanism.common.inventory.slot.UpgradeInventorySlot upgradeOutputSlot;

    @Shadow
    public abstract int getUpgrades(Upgrade upgrade);

    @org.spongepowered.asm.mixin.injection.Inject(method = "removeUpgrade", at = @At("HEAD"), cancellable = true)
    private void onRemoveUpgrade(Upgrade upgrade, boolean removeAll, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (upgrade == Upgrade.FILTER && tile instanceof TrashCubeBlockEntity) {
            int installed = this.getUpgrades(upgrade);
            if (installed > 0) {
                int toRemove = removeAll ? installed : 1;
                net.minecraft.world.item.ItemStack upgradeStack = new net.minecraft.world.item.ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get(), toRemove);
                net.minecraft.world.item.ItemStack simulatedRemainder = this.upgradeOutputSlot.insertItem(upgradeStack, mekanism.api.Action.SIMULATE, mekanism.api.AutomationType.INTERNAL);
                if (simulatedRemainder.getCount() < toRemove) {
                    toRemove -= simulatedRemainder.getCount();
                    if (installed == toRemove) {
                        this.upgrades.remove(upgrade);
                    } else {
                        this.upgrades.put(upgrade, installed - toRemove);
                    }

                    this.tile.recalculateUpgrades(upgrade);
                    net.minecraft.world.item.ItemStack executeStack = new net.minecraft.world.item.ItemStack(TrashCube.RADIOACTIVE_UPGRADE.get(), toRemove);
                    this.upgradeOutputSlot.insertItem(executeStack, mekanism.api.Action.EXECUTE, mekanism.api.AutomationType.INTERNAL);
                }
            }
            ci.cancel();
        }
    }
}
