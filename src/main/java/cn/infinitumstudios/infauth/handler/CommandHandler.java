package cn.infinitumstudios.infauth.handler;


import cn.infinitumstudios.infauth.InfAuth;
import cn.infinitumstudios.infauth.helper.LocalizationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InfAuth.MODID)
public class CommandHandler {

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();

        // Check if command is from a player
        if (source.getEntity() instanceof ServerPlayer player) {
            String command = event.getParseResults().getReader().getString();

            // Allow only login and register commands when not authenticated
            if (!InfAuth.isAuthenticated(player)) {
                if (!command.startsWith("login") && !command.startsWith("register")) {
                    if(event.isCancelable())
                        event.setCanceled(true);
                    player.sendSystemMessage(LocalizationHelper.mustLoginFirst());
                }
            }
        }
    }
}
