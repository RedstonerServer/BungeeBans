package com.redstoner.bungeeBans;

import com.google.gson.Gson;
import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Util {
	private static final Gson gson = new Gson();

	private static final HttpProfileRepository profileRepo = new HttpProfileRepository("minecraft");

	private static final String undashedUuidRegex = "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)";
	private static final String dashedUuidRegex   = "([0-9a-fA-F]{8})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]+)";

	private static final String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static final Pattern uuidValidity = Pattern.compile(dashedUuidRegex);
	private static final Pattern ipValidity   = Pattern.compile(ipRegex);

	public static String dashUUID(String uuid) {
		return uuid.replaceFirst(undashedUuidRegex, "$1-$2-$3-$4-$5");
	}

	public static String trimUUID(String uuid) {
		return uuid.replaceAll("-", "");
	}

	public static String getNow() {
		return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
	}

	public static Profile[] findProfilesByNames(String... names) {
		return profileRepo.findProfilesByNames(names);
	}

	public static boolean validateUUID(String uuid) {
		return uuidValidity.matcher(uuid).matches();
	}

	public static boolean validateIP(String ip) {
		return ipValidity.matcher(ip).matches();
	}

	public static NameChange[] findNameChangesByUUID(String uuid) throws MojangException {
		NameChange[] names = null;

		try {
			Scanner jsonScanner = new Scanner(
					new URL("https://api.mojang.com/user/profiles/" + trimUUID(uuid) + "/names").openConnection().getInputStream(),
					"UTF-8"
			);

			names = gson.fromJson(jsonScanner.next(), NameChange[].class);
			jsonScanner.close();
		} catch (IOException e) {
			if (e.getMessage().contains("HTTP response code: 429")) {
				throw new MojangException("Mojang api request limit reached! Please try again later!");
			} else {
				throw new MojangException(e.getMessage());
			}
		} catch (Exception e) {
			throw new MojangException("Invalid UUID!");
		}

		return names;
	}

	public static class MojangException extends Exception {
		MojangException(String message) {
			super(message);
		}
	}

	public static class NameChange {
		public String name;
		public long   changedToAt;

		public Date getChangeDate() {
			return new Date(changedToAt);
		}
	}
}
