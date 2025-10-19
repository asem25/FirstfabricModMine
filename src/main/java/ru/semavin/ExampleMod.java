package ru.semavin.fabricmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import ru.semavin.net.Packets;

public class ExampleMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CHAT_MSG, (server, player, handler, buf, sender) -> {
            byte[] bytes = read(buf);
            if (bytes == null) return;

            try {
                var msg = MessageOuterClass.Message.parseFrom(bytes);
                var uuid = player.getUUID();
                var text = msg.getText();
                if (text == null || text.isBlank()) return;

                server.execute(() -> {
                    // сохранить в БД (как раньше)
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static byte[] read(FriendlyByteBuf buf) {
        try { return buf.readByteArray(); } catch (Exception e) { return null; }
    }
}

