package com.redstoner.bungeeBans.commands;

import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.IPBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class GetIPBanCommand extends Command {
	private BanManager<IPBan> bm;

	public GetIPBanCommand(BanManager<IPBan> bm) {
		super("getipban", "bungeebans.getipban");

		this.bm = bm;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(
					new ComponentBuilder(ChatColor.RED + "Usage: ")
							.append(ChatColor.AQUA + "/getipban ")
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

		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "IP ")
						.append(ChatColor.AQUA + ip)
						.append(ChatColor.GREEN + " is banned for ")
						.append(ChatColor.AQUA + ban.getReason())
						.append(ChatColor.GREEN + " until ")
						.append(ChatColor.AQUA + ban.getExpires())
						.create()
		);

	}
}
