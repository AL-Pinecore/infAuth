package cn.infinitumstudios.infauth;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = InfAuth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TIMEOUT_SECONDS = BUILDER
            .comment("How long (in seconds) before an unregistered/not logged in player is kicked")
            .defineInRange("timeoutSeconds", 15, 10, 60);

    private static final ForgeConfigSpec.IntValue MIN_PASSWORD_LENGTH = BUILDER
            .comment("Minimum password length")
            .defineInRange("minPasswordLength", 3, 3, 32);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int timeoutSeconds;
    public static int minPasswordLength;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        timeoutSeconds = TIMEOUT_SECONDS.get();
        minPasswordLength = MIN_PASSWORD_LENGTH.get();
    }
}


