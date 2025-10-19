package ru.semavin.net;

import net.minecraft.resources.ResourceLocation;

public final class Packets {
    public static final ResourceLocation CHAT_MSG = ResourceLocation.tryBuild(
            "fabricMod", "chat_msg"
    );

    private Packets() {
    }
}
