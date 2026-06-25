package com.github.mochi7054.trashcube.mixin;

import java.util.HashSet;
import java.util.Set;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.common.inventory.slot.UpgradeInventorySlot;
import mekanism.common.tile.component.TileComponentUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.block.entity.TrashCubeBlockEntity;

@Mixin(value = UpgradeInventorySlot.class, remap = false)
public class UpgradeInventorySlotMixin {

    @ModifyVariable(
        method = "input(Lmekanism/api/IContentsListener;Ljava/util/Set;)Lmekanism/common/inventory/slot/UpgradeInventorySlot;",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private static Set<Upgrade> modifySupportedTypes(Set<Upgrade> supportedTypes, IContentsListener listener) {
        if (supportedTypes != null && listener instanceof TrashCubeBlockEntity) {
            Set<Upgrade> mutableSet = new HashSet<>(supportedTypes);
            mutableSet.add(TrashCube.RADIOACTIVE_UPGRADE_TYPE);
            return mutableSet;
        }
        return supportedTypes;
    }
}
