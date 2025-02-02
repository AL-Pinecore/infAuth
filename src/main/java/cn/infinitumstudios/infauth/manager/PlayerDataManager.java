package cn.infinitumstudios.infauth.manager;

import cn.infinitumstudios.infauth.InfAuth;
import cn.infinitumstudios.infauth.data.PlayerData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class PlayerDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File SAVE_FILE = new File("config/infauth/players.json");
    private static List<PlayerData> playerData = new ArrayList<>();

    public static void load() {
        if (!SAVE_FILE.exists()) {
            SAVE_FILE.getParentFile().mkdirs();
            save();
            return;
        }

        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type type = new TypeToken<List<PlayerData>>(){}.getType();
            playerData = GSON.fromJson(reader, type);
            if (playerData == null) playerData = new ArrayList<>();
        } catch (IOException e) {
            InfAuth.LOGGER.error("Failed to load player data", e);
            playerData = new ArrayList<>();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(playerData, writer);
        } catch (IOException e) {
            InfAuth.LOGGER.error("Failed to save player data", e);
        }
    }

    private static Optional<PlayerData> getPlayerData(UUID uuid) {
        return playerData.stream()
                .filter(data -> data.getUuid().equals(uuid.toString()))
                .findFirst();
    }

    public static boolean isRegistered(UUID uuid) {
        return getPlayerData(uuid).isPresent();
    }

    public static boolean checkPassword(UUID uuid, String password) {
        Optional<PlayerData> data = getPlayerData(uuid);
        return data.isPresent() && data.get().getPassword().equals(password);
    }

    public static void register(UUID uuid, String password) {
        if (!isRegistered(uuid)) {
            playerData.add(new PlayerData(uuid.toString(), password, false));
            save();
        }
    }

    public static boolean isAdmin(UUID uuid) {
        Optional<PlayerData> data = getPlayerData(uuid);
        return data.isPresent() && data.get().isAdmin();
    }

    public static void setAdmin(UUID uuid, boolean isAdmin) {
        getPlayerData(uuid).ifPresent(data -> {
            data.setAdmin(isAdmin);
            save();
        });
    }

    public static void changePassword(UUID uuid, String newPassword) {
        getPlayerData(uuid).ifPresent(data -> {
            data.setPassword(newPassword);
            save();
        });
    }

    public static void removeAccount(UUID uuid) {
        playerData.removeIf(data -> data.getUuid().equals(uuid.toString()));
        save();
    }

    // Optional: Method to get all admin accounts
    public static List<PlayerData> getAdminAccounts() {
        return playerData.stream()
                .filter(PlayerData::isAdmin)
                .toList();
    }
}