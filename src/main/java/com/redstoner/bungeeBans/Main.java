package com.redstoner.bungeeBans;

import com.redstoner.bungeeBans.commands.BanCommand;
import com.redstoner.bungeeBans.commands.GetBanCommand;
import com.redstoner.bungeeBans.commands.UnbanCommand;
import com.redstoner.bungeeBans.json.PlayerBan;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Plugin {
	private File bansFile = new File("banned-players.json");

	private BanManager<PlayerBan> playerBanManager = new BanManager<>(bansFile, PlayerBan.class);

	private boolean shouldSave = true;

	@Override
	public void onEnable() {
		try {
			getLogger().info("Loading bans...");

			playerBanManager.loadBans();

			getLogger().info("Loaded bans!");
		} catch (FileNotFoundException e) {
			getLogger().warning("Bans file does not exist! Creating!");

			try {
				if (bansFile.createNewFile()) {
					getLogger().info("File created! Retrying load...");
					onEnable();
					return;
				} else {
					getLogger().severe("File could not be created! Disabling!");
				}
			} catch (IOException e2) {
				getLogger().severe("File could not be created! Disabling!");
				getLogger().severe(e2.getMessage());
				e2.printStackTrace();
			}

			disable();
			return;
		} catch (Exception e) {
			getLogger().severe("Failed to load bans: " + e.getMessage());
			e.printStackTrace();

			disable();
			return;
		}

		PluginManager pm = getProxy().getPluginManager();

		pm.registerCommand(this, new BanCommand(playerBanManager, this));
		pm.registerCommand(this, new UnbanCommand(playerBanManager));
		pm.registerCommand(this, new GetBanCommand(playerBanManager));

		pm.registerListener(this, new BanJoinListener<>(playerBanManager));
	}

	@Override
	public void onDisable() {
		if (shouldSave) {
			try {
				playerBanManager.saveBans();
				getLogger().info("Saved bans to file!");
			} catch (IOException e) {
				getLogger().severe("Failed to save bans: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void disable() {
		getLogger().severe("Disabling plugin!");

		shouldSave = false;
		this.onDisable();

		PluginManager pm = getProxy().getPluginManager();

		pm.unregisterListeners(this);
		pm.unregisterCommands(this);
	}


}
