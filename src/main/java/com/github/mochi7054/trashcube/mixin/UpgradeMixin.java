package com.github.mochi7054.trashcube.mixin;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
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
import mekanism.api.text.ILangEntry;
import mekanism.api.text.EnumColor;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap.OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable.EnumCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

@Mixin(value = Upgrade.class, remap = false)
public class UpgradeMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static Upgrade[] $VALUES;

    @Shadow
    @Final
    @Mutable
    public static IntFunction<Upgrade> BY_ID;

    @Shadow
    @Final
    @Mutable
    public static StreamCodec<ByteBuf, Upgrade> STREAM_CODEC;

    @Shadow
    @Final
    @Mutable
    public static Codec<Upgrade> CODEC;

    @Invoker("<init>")
    private static Upgrade invokeInit(String internalName, int ordinal, String name, ILangEntry langKey,
            ILangEntry descLangKey, int maxStack, EnumColor color) {
        return null;
    }

    @Unique
    private static Upgrade createNew(String name, ILangEntry langKey, ILangEntry descLangKey,
            int maxStack, EnumColor color) {
        int index = $VALUES.length;
        Upgrade result = invokeInit(name.toUpperCase(), index, name, langKey, descLangKey, maxStack, color);
        Upgrade[] newVALUES = Arrays.copyOf($VALUES, index + 1);
        newVALUES[index] = result;
        $VALUES = newVALUES;
        return result;
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

        // Dynamically resolve enum by index to handle runtime expansion and prevent caching issues
        BY_ID = index -> {
            Upgrade[] vals = Upgrade.values();
            int len = vals.length;
            int r = index % len;
            return vals[r < 0 ? r + len : r];
        };

        STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.idMapper(BY_ID, Upgrade::ordinal);

        CODEC = com.mojang.serialization.Codec.STRING.flatXmap(
            s -> {
                for (Upgrade u : Upgrade.values()) {
                    if (u.getSerializedName().equals(s)) {
                        return com.mojang.serialization.DataResult.success(u);
                    }
                }
                if ("gas".equals(s)) {
                    return com.mojang.serialization.DataResult.success(Upgrade.CHEMICAL);
                }
                return com.mojang.serialization.DataResult.error(() -> "Unknown upgrade: " + s);
            },
            u -> com.mojang.serialization.DataResult.success(u.getSerializedName())
        );
    }
}
