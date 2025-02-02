package cn.infinitumstudios.infauth.client;

import net.minecraft.world.phys.Vec3;

public class ClientAuthState {
    private static boolean authenticated = false;
    private static Vec3 loginPosition = null;

    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static void setAuthenticated(boolean status) {
        authenticated = status;
    }

    public static Vec3 getLoginPosition() {
        return loginPosition;
    }

    public static void setLoginPosition(Vec3 pos) {
        loginPosition = pos;
    }
}
