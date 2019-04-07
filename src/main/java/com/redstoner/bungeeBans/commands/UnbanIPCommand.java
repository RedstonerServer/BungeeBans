package com.redstoner.bungeeBans.commands;

import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.IPBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class UnbanIPCommand extends Command {
	private BanManager<IPBan> bm;

	public UnbanIPCommand(BanManager<IPBan> bm) {
		super("unbanip", "bungeebans.unbanip", "pardonip");

		this.bm = bm;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(
					new ComponentBuilder(ChatColor.RED + "Usage: ")
							.append(ChatColor.AQUA + "/unbanip ")
							.append(ChatColor.GOLD + "<ip> ")
							.create()
			);

			return;
		}

		String ip = args[0];

		if (!Util.validateIP(ip)) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid IP!"));
			return;
		}

		IPBan ban = bm.getBan(ip);

		if (ban == null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "That IP is not banned!"));
			return;
		}

		bm.removeBan(ban);

		try {
			bm.saveBans();
		} catch (IOException e) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to save IP bans to file! (nothing was changed)"));
			e.printStackTrace();

			bm.addBan(ban);
			return;
		}

		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "Unbanned IP ")
						.append(ChatColor.AQUA + ip)
						.create()
		);
	}
}
