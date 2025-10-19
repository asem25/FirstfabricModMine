package ru.semavin.fabricmod.ui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.semavin.net.ChatMsgPayload;
import ru.semavin.proto.*;

public class SimpleMessageScreen extends Screen {
    private EditBox input;

    public SimpleMessageScreen() {
        super(Component.literal("Send Protobuf Message"));
    }

    @Override
    protected void init() {
        int w = 320, h = 20;
        int x = (this.width - w) / 2;
        int y = this.height / 2 - 10;

        input = new EditBox(this.font, x, y, w, h, Component.literal("message"));
        input.setMaxLength(256);
        input.setValue("");
        input.setFocused(true);
        input.setResponder(s -> {});

        this.addRenderableWidget(input);

        Button send = Button.builder(Component.literal("Отправить"), b -> send())
                .bounds(x, y + 30, 120, 20)
                .build();
        this.addRenderableWidget(send);

        this.setInitialFocus(input);
    }

    private void send() {
        String text = input.getValue().trim();
        if (text.isEmpty()) return;

        var msg = MessageOuterClass.Message.newBuilder()
                .setText(text)
                .build();

        ClientPlayNetworking.send(new ChatMsgPayload(msg.toByteArray()));
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        this.renderBackground(g, mouseX, mouseY, delta);
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredString(this.font, "Введите сообщение",
                this.width / 2, this.height / 2 - 30, 0xFFFFFF);
        input.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
