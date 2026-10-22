package com.example.netheritekit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NetheriteKitClient implements ClientModInitializer {
    private static KeyBinding keyGetKit;

    @Override
    public void onInitializeClient() {
        keyGetKit = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.netheritekit.getkit",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyGetKit.wasPressed()) {
                ClientPlayNetworking.send(new RequestKitPayload());
            }
        });
    }
}
