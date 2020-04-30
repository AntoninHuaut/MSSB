package fr.maner.mssb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JsonConfig {

	private enum ConfigErrEnum {

		CREATION("Impossible de créer le fichier de configuration"),
		INVALID("Le fichier de configuration est invalide"),
		READ("Impossible de lire le fichier de configuration"),
		WRITE("Impossible d'écrire dans le fichier de configuration");

		private String str;
		private ConfigErrEnum(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	private JavaPlugin pl;
	private File file = null;
	private String fileName;

	public JsonConfig(JavaPlugin pl, String fileName) {
		this.pl = pl;
		
		this.fileName = fileName + ".json";
		this.file = new File(pl.getDataFolder() + File.separator + this.fileName);

		saveDefaultConfig();
	}

	private void saveDefaultConfig() {
		if(file.exists()) return;
		
		pl.saveResource(fileName, false);
	}
	
	public JsonObject getJsonObject() {
		return getJson().getAsJsonObject();
	}
	
	public JsonArray getJsonArray() {
		return getJson().getAsJsonArray();
	}

	public JsonElement getJson() {
		try {
			return new JsonParser().parse(getContent());
		} catch(IllegalStateException | JsonParseException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.INVALID.toString());
			e.printStackTrace();
			return null;
		}
	}

	private String getContent() {
		try {
			return new String(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.READ.toString());
			e.printStackTrace();
			return null;
		}
	}
}