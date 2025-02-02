package cn.infinitumstudios.infauth.manager;

import cn.infinitumstudios.infauth.InfAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File SAVE_FILE = new File("config/infauth/players.json");
    private static Map<String, String> playerData = new HashMap<>();

    public static void load() {
        if (!SAVE_FILE.exists()) {
            SAVE_FILE.getParentFile().mkdirs();
            save();
            return;
        }

        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            playerData = GSON.fromJson(reader, type);
            if (playerData == null) playerData = new HashMap<>();
        } catch (IOException e) {
            InfAuth.LOGGER.error("Failed to load player data", e);
            playerData = new HashMap<>();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(playerData, writer);
        } catch (IOException e) {
            InfAuth.LOGGER.error("Failed to save player data", e);
        }
    }

    public static boolean isRegistered(String username) {
        return playerData.containsKey(username.toLowerCase());
    }

    public static boolean checkPassword(String username, String password) {
        return playerData.getOrDefault(username.toLowerCase(), "").equals(password);
    }

    public static void register(String username, String password) {
        playerData.put(username.toLowerCase(), password);
        save();
    }
}