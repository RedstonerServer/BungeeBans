package com.redstoner.bungeeBans;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.redstoner.bungeeBans.json.Ban;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanManager<T extends Ban> {
	private Gson gson = new Gson();
	private File file;

	private Class<T> type;

	private List<T> bans = new ArrayList<>();

	BanManager(File file, Class<T> type) {
		this.file = file;
		this.type = type;
	}

	public void loadBans() throws JsonSyntaxException, IOException {
		String json = new String(Files.readAllBytes(file.toPath()));

		try {
			@SuppressWarnings ("unchecked")
			T[] bans = gson.fromJson(json, (Class<T[]>) java.lang.reflect.Array.newInstance(type, 0).getClass());
			if (bans != null) this.bans.addAll(Arrays.asList(bans));
		} catch (ClassCastException e) {
			//IGNORE
			e.printStackTrace();
		}
	}

	public void saveBans() throws IOException {
		String json = gson.toJson(bans);

		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(json.getBytes());
		outputStream.close();
	}

	public void addBan(T ban) {
		this.bans.add(ban);
	}

	public void removeBan(T ban) {
		this.bans.remove(ban);
	}

	public T getBan(String identifier) {
		for (T ban : bans) {
			if (ban.getIdentifier().equals(identifier)) {
				return ban;
			}
		}

		return null;
	}
}
