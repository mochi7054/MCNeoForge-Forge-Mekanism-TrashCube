package com.github.mochi7054.trashcube.client;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.client.gui.TrashCubeScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterMenuScreensEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrashCube.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrashCubeClient {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TrashCube.CONTAINER_TYPE.get(), TrashCubeScreen::new);
    }
}
