package ru.semavin.ui;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import static jdk.internal.org.jline.terminal.Terminal.MouseTracking.Button;

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
        input.setResponder(s -> {}); // no-op

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

        var msg = MessageOuterClass.Message.newBuilder().setText(text).build();
        byte[] bytes = msg.toByteArray();

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByteArray(bytes);

        ClientPlayNetworking.send(Packets.CHAT_MSG, buf);
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredString(this.font, "Введите сообщение (до 256 символов)",
                this.width / 2, this.height / 2 - 30, 0xFFFFFF);
        input.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
