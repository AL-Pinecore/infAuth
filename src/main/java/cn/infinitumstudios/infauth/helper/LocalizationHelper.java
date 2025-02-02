package cn.infinitumstudios.infauth.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LocalizationHelper {
    public static MutableComponent getComponent(String key) {
        return Component.translatable("infauth." + key);
    }

    public static MutableComponent getComponent(String key, Object... args) {
        return Component.translatable("infauth." + key, args);
    }

    // Command messages
    public static MutableComponent alreadyRegistered() {
        return getComponent("command.already_registered");
    }

    public static MutableComponent notRegistered() {
        return getComponent("command.not_registered");
    }

    public static MutableComponent passwordTooShort(int minLength) {
        return getComponent("command.password_too_short", minLength);
    }

    public static MutableComponent passwordUsername() {
        return getComponent("command.password_username");
    }

    public static MutableComponent registerSuccess() {
        return getComponent("command.register_success");
    }

    public static MutableComponent wrongPassword() {
        return getComponent("command.wrong_password");
    }

    public static MutableComponent loginSuccess() {
        return getComponent("command.login_success");
    }

    public static MutableComponent timeout() {
        return getComponent("kick.timeout");
    }

    public static MutableComponent welcomeNewPlayer() {
        return getComponent("message.welcome_new");
    }

    public static MutableComponent welcomeBack() {
        return getComponent("message.welcome_back");
    }

    public static MutableComponent registerPrompt() {
        return getComponent("message.register_prompt");
    }

    public static MutableComponent loginPrompt() {
        return getComponent("message.login_prompt");
    }

    public static MutableComponent alreadyLoggedIn() {
        return getComponent("command.already_logged_in");
    }

    public static MutableComponent mustLoginFirst() {
        return getComponent("command.must_login_first");
    }
}

