package ru.semavin;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.*;
import org.lwjgl.glfw.GLFW;
import ru.semavin.ui.SimpleMessageScreen;

public class ExampleModClient implements ClientModInitializer {
    private static KeyMapping openScreenKey;

    @Override
    public void onInitializeClient() {
        openScreenKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping("key.examplemod.open", GLFW.GLFW_KEY_M, "key.categories.multiplayer")
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreenKey.consumeClick()) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().setScreen(new SimpleMessageScreen());
                }
            }
        });
    }
}


