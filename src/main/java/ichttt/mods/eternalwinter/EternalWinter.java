package ichttt.mods.eternalwinter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class EternalWinter {
    public static final String MOD_ID = "eternalwinter";
    public static final Logger LOGGER = LoggerFactory.getLogger("EternalWinter");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Config config = new Config();

    private EternalWinter() {
    }

    public static final class Config {
        /** BLACKLIST: every biome except the ones in biomeList. WHITELIST: only the ones in biomeList. */
        public ListMode listMode = ListMode.BLACKLIST;
        /** Biome ids, with or without the namespace ("river" matches minecraft:river and somemod:river). */
        public List<String> biomeList = new ArrayList<>(List.of("river", "ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean", "cold_ocean",
            "deep_warm_ocean", "deep_lukewarm_ocean", "deep_cold_ocean", "deep_frozen_ocean"));
        /** The downfall each biome gets, from 0 to 1. -1 keeps each biome's own value. */
        public double downfall = -1;
    }

    public static void loadConfig(Path configDir) {
        Path file = configDir.resolve(MOD_ID + ".json");
        try {
            if (Files.exists(file)) {
                Config read = GSON.fromJson(Files.readString(file), Config.class);
                if (read != null) {
                    if (read.listMode == null) read.listMode = ListMode.BLACKLIST;
                    if (read.biomeList == null) read.biomeList = new ArrayList<>();
                    if (read.downfall != -1 && (read.downfall < 0 || read.downfall > 1)) {
                        LOGGER.error("Invalid downfall {}, keeping each biome's own", read.downfall);
                        read.downfall = -1;
                    }
                    config = read;
                }
            } else {
                Files.createDirectories(configDir);
                Files.writeString(file, GSON.toJson(config));
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Could not read {}, using defaults", file, e);
        }
    }

    /** Whether this biome gets turned to winter. */
    public static boolean affects(ResourceLocation biome) {
        boolean listed = config.biomeList.stream().anyMatch(s -> s.equals(biome.getPath()) || s.equals(biome.toString()));
        return config.listMode == ListMode.WHITELIST ? listed : !listed;
    }
}
