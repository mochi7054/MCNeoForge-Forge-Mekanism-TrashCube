package com.github.mochi7054.trashcube.client;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.client.gui.TrashCubeScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TrashCube.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrashCubeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(TrashCube.CONTAINER_TYPE.get(), TrashCubeScreen::new);
        });
    }
}

