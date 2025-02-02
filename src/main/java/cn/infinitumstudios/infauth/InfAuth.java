package cn.infinitumstudios.infauth;

import cn.infinitumstudios.infauth.command.CommandRegistry;
import cn.infinitumstudios.infauth.handler.EventHandler;
import cn.infinitumstudios.infauth.handler.NetworkHandler;
import cn.infinitumstudios.infauth.manager.PlayerDataManager;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(InfAuth.MODID)
public class InfAuth {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "infauth";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, Long> loginTimers = new HashMap<>();
    private static final Map<UUID, Boolean> authenticatedPlayers = new HashMap<>();

    public InfAuth(FMLJavaModLoadingContext ctx) {
        IEventBus modEventBus = ctx.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new CommandRegistry());

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ctx.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        PlayerDataManager.load();
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    public static boolean isAuthenticated(ServerPlayer player) {
        return authenticatedPlayers.getOrDefault(player.getUUID(), false);
    }

    public static void setAuthenticated(ServerPlayer player, boolean status) {
        if (status) {
            loginTimers.remove(player.getUUID());
            authenticatedPlayers.put(player.getUUID(), true);
        } else {
            authenticatedPlayers.put(player.getUUID(), false);
            loginTimers.put(player.getUUID(), System.currentTimeMillis());
        }
        NetworkHandler.sendAuthStatusToClient(player, status);
    }

    public static Long getLoginTimer(UUID uuid) {
        return loginTimers.get(uuid);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
