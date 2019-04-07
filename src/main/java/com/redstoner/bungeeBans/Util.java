package com.redstoner.bungeeBans;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	private static final HttpProfileRepository profileRepo = new HttpProfileRepository("minecraft");

	public static String dashUUID(String uuid) {
		return uuid.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
	}

	public static String getNow() {
		return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
	}

	public static Profile[] findProfilesByNames(String... names) {
		return profileRepo.findProfilesByNames(names);
	}
}
