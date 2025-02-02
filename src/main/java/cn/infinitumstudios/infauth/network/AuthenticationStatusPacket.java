package cn.infinitumstudios.infauth.network;

import cn.infinitumstudios.infauth.client.ClientAuthState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AuthenticationStatusPacket {
    private final boolean authenticated;

    public AuthenticationStatusPacket(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public static void encode(AuthenticationStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.authenticated);
    }

    public static AuthenticationStatusPacket decode(FriendlyByteBuf buf) {
        return new AuthenticationStatusPacket(buf.readBoolean());
    }

    public static void handle(AuthenticationStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // We're on the client
            ClientAuthState.setAuthenticated(msg.authenticated);
        });
        ctx.get().setPacketHandled(true);
    }
}

