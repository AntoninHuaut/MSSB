package fr.maner.mssb.utils;

import com.google.gson.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;

public class JsonConfig {

    private enum ConfigErrEnum {

        CREATION("Impossible de créer le fichier de configuration"),
        INVALID("Le fichier de configuration est invalide"),
        READ("Impossible de lire le fichier de configuration"),
        WRITE("Impossible d'écrire dans le fichier de configuration");

        private final String str;

        ConfigErrEnum(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }

    protected final JavaPlugin pl;
    private final File file;
    private final String fileName;

    public JsonConfig(JavaPlugin pl, String fileName) {
        this.pl = pl;

        this.fileName = fileName + ".json";
        this.file = new File(pl.getDataFolder() + File.separator + this.fileName);

        saveDefaultConfig();
    }

    private void saveDefaultConfig() {
        if (file.exists()) return;

        pl.saveResource(fileName, false);
    }

    protected int getInt(JsonObject obj, String key) {
        return get(obj, key).getAsInt();
    }

    protected String getString(JsonObject obj, String key) {
        return get(obj, key).getAsString();
    }

    protected double getDouble(JsonObject obj, String key) {
        return get(obj, key).getAsDouble();
    }

    protected float getFloat(JsonObject obj, String key) {
        return get(obj, key).getAsFloat();
    }

    protected JsonElement get(JsonObject obj, String key) {
        return obj.get(key);
    }

    public JsonObject getJsonObject() {
        return getJson().getAsJsonObject();
    }

    public JsonArray getJsonArray() {
        return getJson().getAsJsonArray();
    }

    public JsonElement getJson() {
        try {
            var content = getContent();
            if (content.isEmpty()) return null;
            
            return JsonParser.parseString(content.get());
        } catch (IllegalStateException | JsonParseException e) {
            pl.getLogger().log(Level.SEVERE, ConfigErrEnum.INVALID.toString());
            e.printStackTrace();
            return null;
        }
    }

    private Optional<String> getContent() {
        try {
            return Optional.of(new String(Files.readAllBytes(Paths.get(file.toURI()))));
        } catch (IOException e) {
            pl.getLogger().log(Level.SEVERE, ConfigErrEnum.READ.toString());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}