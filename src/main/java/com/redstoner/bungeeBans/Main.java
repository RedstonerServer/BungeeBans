package com.redstoner.bungeeBans;

import com.redstoner.bungeeBans.commands.*;
import com.redstoner.bungeeBans.json.IPBan;
import com.redstoner.bungeeBans.json.PlayerBan;
import com.redstoner.bungeeBans.listeners.BanJoinListener;
import com.redstoner.bungeeBans.listeners.DisableJoinListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class Main extends Plugin {
	private File playerBanFile = new File("banned-players.json");
	private File ipBanFile     = new File("banned-ips.json");

	private BanManager<PlayerBan> playerBanManager = new BanManager<>(playerBanFile, PlayerBan.class);
	private BanManager<IPBan>     ipBanManager     = new BanManager<>(ipBanFile, IPBan.class);

	private boolean shouldSave = true;

	@Override
	public void onEnable() {
		if (
				loadBans("player", playerBanManager, playerBanFile) ||
				loadBans("IP", ipBanManager, ipBanFile)
		) {
			return;
		}

		PluginManager pm = getProxy().getPluginManager();

		pm.registerCommand(this, new BanCommand(playerBanManager, this));
		pm.registerCommand(this, new UnbanCommand(playerBanManager));
		pm.registerCommand(this, new GetBanCommand(playerBanManager));

		pm.registerCommand(this, new BanIPCommand(ipBanManager, this));
		pm.registerCommand(this, new UnbanIPCommand(ipBanManager));
		pm.registerCommand(this, new GetIPBanCommand(ipBanManager));

		pm.registerListener(this, new BanJoinListener<>("player", playerBanManager));
		pm.registerListener(this, new BanJoinListener<>("IP", ipBanManager));
	}

	@Override
	public void onDisable() {
		if (shouldSave) {
			saveBans("player", playerBanManager);
			saveBans("IP", ipBanManager);
		}
	}

	private void disable() {
		getLogger().severe("Players will not be able to join because of a severe error!!! Check the log output above!");

		shouldSave = false;
		getProxy().getPluginManager().registerListener(this, new DisableJoinListener());
	}

	private boolean loadBans(String name, BanManager banManager, File banFile) {
		try {
			getLogger().info("Loading " + name + " bans...");
			banManager.loadBans();
			getLogger().info("Loaded " + name + " bans!");

			return false;
		} catch (FileNotFoundException | NoSuchFileException e) {
			getLogger().warning("Ban file (" + name + ") does not exist! Creating!");

			try {
				if (banFile.createNewFile()) {
					getLogger().info("Ban file (" + name + ") created! Retrying load...");
					onEnable();
					return false;
				}
			} catch (IOException e2) {
				getLogger().severe(e2.getMessage());
				e2.printStackTrace();
			}

			getLogger().severe("Ban file (" + name + ") could not be created!");

			disable();
			return true;
		} catch (Exception e) {
			getLogger().severe("Failed to load " + name + " bans: " + e.getMessage());
			e.printStackTrace();

			disable();
			return true;
		}
	}

	private void saveBans(String name, BanManager banManager) {
		try {
			playerBanManager.saveBans();
			getLogger().info("Saved " + name + " bans to file!");
		} catch (IOException e) {
			getLogger().severe("Failed to save " + name + " bans: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
