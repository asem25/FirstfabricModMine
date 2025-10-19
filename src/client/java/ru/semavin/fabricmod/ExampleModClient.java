package ru.semavin.fabricmod;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import ru.semavin.fabricmod.ui.SimpleMessageScreen;
import ru.semavin.net.ChatMsgPayload;

public class ExampleModClient implements ClientModInitializer {
    private static KeyMapping openScreenKey;

    @Override
    public void onInitializeClient() {
        openScreenKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping("key.examplemod.open", GLFW.GLFW_KEY_M, "key.categories.multiplayer")
        );

        PayloadTypeRegistry.playC2S().register(ChatMsgPayload.TYPE, ChatMsgPayload.CODEC);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreenKey.consumeClick()) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().setScreen(new SimpleMessageScreen());
                }
            }
        });
    }
}


