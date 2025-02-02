package cn.infinitumstudios.infauth.handler;

import cn.infinitumstudios.infauth.Config;
import cn.infinitumstudios.infauth.InfAuth;
import cn.infinitumstudios.infauth.client.ClientAuthState;
import cn.infinitumstudios.infauth.helper.LocalizationHelper;
import cn.infinitumstudios.infauth.manager.PlayerDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = InfAuth.MODID)
public class EventHandler {
    private static final Map<UUID, Vec3> joinPositions = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InfAuth.setAuthenticated(player, false);
            joinPositions.put(player.getUUID(), player.position());

            if (PlayerDataManager.isRegistered(player.getGameProfile().getName())) {
                player.sendSystemMessage(LocalizationHelper.welcomeBack());
                player.sendSystemMessage(LocalizationHelper.loginPrompt());
            } else {
                player.sendSystemMessage(LocalizationHelper.welcomeNewPlayer());
                player.sendSystemMessage(LocalizationHelper.registerPrompt());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InfAuth.setAuthenticated(player, false);
            joinPositions.remove(player.getUUID());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            Vec3 joinPos = joinPositions.get(player.getUUID());
            if (joinPos != null) {
                player.teleportTo(joinPos.x, joinPos.y, joinPos.z);
            }
        }
    }

    @SubscribeEvent
    public void onInventoryInteraction(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onInventoryOpen(PlayerContainerEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !InfAuth.isAuthenticated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        if (event.getServer().getPlayerList().getPlayers().isEmpty()) return;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            if (!InfAuth.isAuthenticated(player)) {
                // Prevent movement by constantly resetting position
                Vec3 joinPos = joinPositions.get(player.getUUID());
                if (joinPos != null) {
                    player.teleportTo(joinPos.x, joinPos.y, joinPos.z);
                }

                Long loginTime = InfAuth.getLoginTimer(player.getUUID());
                if (loginTime != null && System.currentTimeMillis() - loginTime > Config.timeoutSeconds * 1000L) {
                    player.connection.disconnect(LocalizationHelper.timeout());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientInput(InputEvent event) {
        if (!ClientAuthState.isAuthenticated() && Minecraft.getInstance().getCurrentServer() != null) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }
}

