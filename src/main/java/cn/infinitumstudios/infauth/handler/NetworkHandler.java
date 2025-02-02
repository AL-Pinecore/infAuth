package cn.infinitumstudios.infauth.handler;

import cn.infinitumstudios.infauth.InfAuth;
import cn.infinitumstudios.infauth.network.AuthenticationStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(InfAuth.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(AuthenticationStatusPacket.class, id++)
                .decoder(AuthenticationStatusPacket::decode)
                .encoder(AuthenticationStatusPacket::encode)
                .consumerMainThread(AuthenticationStatusPacket::handle)
                .add();
    }

    public static void sendAuthStatusToClient(ServerPlayer player, boolean authenticated) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                new AuthenticationStatusPacket(authenticated));
    }
}
