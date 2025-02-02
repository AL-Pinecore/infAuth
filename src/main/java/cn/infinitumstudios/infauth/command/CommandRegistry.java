package cn.infinitumstudios.infauth.command;

import cn.infinitumstudios.infauth.Config;
import cn.infinitumstudios.infauth.InfAuth;
import cn.infinitumstudios.infauth.helper.LocalizationHelper;
import cn.infinitumstudios.infauth.manager.PlayerDataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class CommandRegistry {

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("register")
                        .then(Commands.argument("password", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    String password = StringArgumentType.getString(context, "password");
                                    UUID uuid = player.getUUID();

                                    if (PlayerDataManager.isRegistered(uuid)) {
                                        context.getSource().sendFailure(LocalizationHelper.alreadyRegistered());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (password.length() < Config.minPasswordLength) {
                                        context.getSource().sendFailure(LocalizationHelper.passwordTooShort(Config.minPasswordLength));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (password.equals(player.getGameProfile().getName())) {
                                        context.getSource().sendFailure(LocalizationHelper.passwordUsername());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    PlayerDataManager.register(uuid, password);
                                    InfAuth.setAuthenticated(player, true);
                                    context.getSource().sendSuccess(LocalizationHelper::registerSuccess, false);
                                    return Command.SINGLE_SUCCESS;
                                }))
        );

        event.getDispatcher().register(
                Commands.literal("login")
                        .then(Commands.argument("password", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    String password = StringArgumentType.getString(context, "password");
                                    UUID uuid = player.getUUID();

                                    if(InfAuth.isAuthenticated(player)){
                                        context.getSource().sendFailure(LocalizationHelper.alreadyLoggedIn());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (!PlayerDataManager.isRegistered(uuid)) {
                                        context.getSource().sendFailure(LocalizationHelper.notRegistered());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (!PlayerDataManager.checkPassword(uuid, password)) {
                                        context.getSource().sendFailure(LocalizationHelper.wrongPassword());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    InfAuth.setAuthenticated(player, true);
                                    context.getSource().sendSuccess(LocalizationHelper::loginSuccess, false);
                                    return Command.SINGLE_SUCCESS;
                                }))
        );
    }
}

