package com.github.mochi7054.trashcube.client;

import com.github.mochi7054.trashcube.TrashCube;
import com.github.mochi7054.trashcube.client.gui.TrashCubeScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = TrashCube.MODID, dist = Dist.CLIENT)
public class TrashCubeClient {
    public TrashCubeClient(ModContainer container, IEventBus modEventBus) {
        modEventBus.register(TrashCubeClient.class);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        mekanism.client.ClientRegistrationUtil.registerScreen(event, TrashCube.CONTAINER_TYPE, TrashCubeScreen::new);
    }
}
