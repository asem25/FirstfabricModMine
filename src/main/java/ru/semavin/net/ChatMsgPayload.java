package ru.semavin.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChatMsgPayload(byte[] data) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("examplemod", "chat_msg");
    public static final Type<ChatMsgPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, ChatMsgPayload> CODEC =
            StreamCodec.of(
                    (buf, value) -> buf.writeByteArray(value.data()),
                    buf -> new ChatMsgPayload(buf.readByteArray())
            );

    public ChatMsgPayload(FriendlyByteBuf buf) {
        this(buf.readByteArray());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByteArray(data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
