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

        Upgrade[] values = Upgrade.values();
        BY_ID = ByIdMap.continuous(Enum::ordinal, values, OutOfBoundsStrategy.WRAP);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
        Function<String, Upgrade> nameLookup = StringRepresentable.createNameLookup(values, Function.identity());
        Function<String, Upgrade> remapper = it -> "gas".equals(it) ? Upgrade.CHEMICAL : nameLookup.apply(it);
        CODEC = new EnumCodec<>(values, remapper);

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
    }
}
