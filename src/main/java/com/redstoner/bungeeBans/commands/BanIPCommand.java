package com.redstoner.bungeeBans.commands;

import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Main;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.IPBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.Arrays;

public class BanIPCommand extends Command {
	private BanManager<IPBan> bm;
	private Main              plugin;

	public BanIPCommand(BanManager<IPBan> bm, Main plugin) {
		super("banip", "bungeeBans.banip");

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
								.append(ChatColor.AQUA + "/banip ")
								.append(ChatColor.GOLD + "<ip> ")
								.append(ChatColor.YELLOW + "[reason]")
								.create()
				);
			case 1:
				break;
			default:
				reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				break;
		}


		String ip      = args[0];
		String expires = "forever"; //TODO: expiry option

		if (!Util.validateIP(ip)) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid IP!"));
			return;
		}

		if (bm.getBan(ip) != null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "That IP is already banned!"));
			return;
		}

		IPBan ban = new IPBan(ip, Util.getNow(), sender.getName(), expires, reason);
		bm.addBan(ban);

		try {
			bm.saveBans();
		} catch (IOException e) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to save IP bans to file! (nothing was changed)"));
			e.printStackTrace();

			bm.removeBan(ban);
			return;
		}

		for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
			if (player.getAddress().getAddress().toString().equals(ip)) {
				player.disconnect(
						new ComponentBuilder(ChatColor.RED + "Your IP was banned by ")
								.append(ChatColor.AQUA + sender.getName())
								.append(ChatColor.RED + " for ")
								.append(ChatColor.YELLOW + reason)
								.create()
				);
			}
		}

		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "Banned IP ")
						.append(ChatColor.AQUA + ip)
						.append(ChatColor.GREEN + " for ")
						.append(ChatColor.AQUA + reason)
						.create()
		);
	}
}
