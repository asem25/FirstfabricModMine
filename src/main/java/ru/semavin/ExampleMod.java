package ru.semavin;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import ru.semavin.db.repo.MessageRepo;
import ru.semavin.entity.MessageEntity;
import ru.semavin.net.ChatMsgPayload;
import ru.semavin.proto.*;

import java.util.UUID;

public final class ExampleMod implements ModInitializer {

    private static MessageRepo repo;

    @Override
    public void onInitialize() {
        repo = new MessageRepo();

        PayloadTypeRegistry.playC2S().register(ChatMsgPayload.TYPE, ChatMsgPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ChatMsgPayload.TYPE, (payload, ctx) -> {
            byte[] bytes = payload.data();
            if (bytes == null || bytes.length == 0) return;

            try {
                var msg = MessageOuterClass.Message.parseFrom(bytes);
                String text = msg.getText();
                if (text == null || text.isBlank()) return;

                UUID uuid = ctx.player().getUUID();
                ctx.server().execute(() -> repo.save(new MessageEntity(uuid, trim256(text))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String trim256(String s) {
        return (s.length() <= 256) ? s : s.substring(0, 256);
    }
}


