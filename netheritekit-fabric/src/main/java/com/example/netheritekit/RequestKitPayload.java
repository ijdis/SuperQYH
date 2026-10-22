package com.example.netheritekit;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestKitPayload() implements CustomPayload {
    public static final Id<RequestKitPayload> ID = new Id<>(Identifier.of("netheritekit", "request_kit"));
    public static final PacketCodec<PacketByteBuf, RequestKitPayload> CODEC = PacketCodec.unit(new RequestKitPayload());

    @Override
    public Id<RequestKitPayload> getId() {
        return ID;
    }
}
