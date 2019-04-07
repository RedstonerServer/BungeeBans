package com.redstoner.bungeeBans.commands;

import com.mojang.api.profiles.Profile;
import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Main;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.PlayerBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.Arrays;

public class BanCommand extends Command {
	private BanManager<PlayerBan> bm;
	private Main                  plugin;

	public BanCommand(BanManager<PlayerBan> bm, Main plugin) {
		super("ban", "bungeeBans.ban");

		this.bm = bm;
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String reason = "Banned by an operator.";

		switch (args.length) {
			case 0:
				sender.sendMessage(
						new ComponentBuilder(ChatColor.RED + "Usage: ")
								.append(ChatColor.AQUA + "/ban ")
								.append(ChatColor.GOLD + "<username> ")
								.append(ChatColor.YELLOW + "[reason]")
								.create()
				);
			case 1:
				break;
			default:
				reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				break;
		}

		Profile[] profiles = Util.findProfilesByNames(args[0]);

		if (profiles.length != 1) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid name!"));
			return;
		}

		String uuid    = Util.dashUUID(profiles[0].getId());
		String name    = profiles[0].getName();
		String expires = "forever"; //TODO: expiry option

		if (bm.getBan(uuid) != null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "That player is already banned!"));
			return;
		}

		PlayerBan ban = new PlayerBan(uuid, name, Util.getNow(), sender.getName(), expires, reason);
		bm.addBan(ban);

		try {
			bm.saveBans();
		} catch (IOException e) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to save player bans to file! (nothing was changed)"));
			e.printStackTrace();

			bm.removeBan(ban);
			return;
		}

		ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(name);

		if (proxiedPlayer != null) {
			proxiedPlayer.disconnect(
					new ComponentBuilder(ChatColor.RED + "You were banned by ")
							.append(ChatColor.AQUA + sender.getName())
							.append(ChatColor.RED + " for ")
							.append(ChatColor.YELLOW + reason)
							.create()
			);
		}


		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "Banned player ")
						.append(ChatColor.AQUA + name)
						.append(ChatColor.GREEN + " with uuid ")
						.append(ChatColor.AQUA + uuid)
						.append(ChatColor.GREEN + " and reason ")
						.append(ChatColor.AQUA + reason)
						.create()
		);
	}
}
