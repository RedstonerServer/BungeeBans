package com.redstoner.bungeeBans.commands;

import com.mojang.api.profiles.Profile;
import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.PlayerBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class GetBanCommand extends Command {
	private BanManager<PlayerBan> bm;

	public GetBanCommand(BanManager<PlayerBan> bm) {
		super("getban", "bungeebans.getban");

		this.bm = bm;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(
					new ComponentBuilder(ChatColor.RED + "Invalid command! ")
							.append("Usage: ")
							.append(ChatColor.AQUA + "/getban ")
							.append(ChatColor.GOLD + "<username/uuid> ")
							.create()
			);

			return;
		}

		String uuid;
		String name;

		if (Util.validateUUID(args[0])) {
			uuid = args[0];

			try {
				Util.NameChange[] nameChanges = Util.findNameChangesByUUID(uuid);

				if (nameChanges.length == 0) {
					sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid UUID!"));
					return;
				}

				name = nameChanges[nameChanges.length - 1].name;
			} catch (Util.MojangException e) {
				sender.sendMessage(new TextComponent(ChatColor.RED + e.getMessage()));
				return;
			}
		} else {
			Profile[] profiles = Util.findProfilesByNames(args[0]);

			if (profiles.length != 1) {
				sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid name!"));
				return;
			}

			uuid = Util.dashUUID(profiles[0].getId());
			name = profiles[0].getName();
		}

		PlayerBan ban = bm.getBan(uuid);

		if (ban == null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "That player is not banned!"));
			return;
		}

		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "Player ")
						.append(ChatColor.AQUA + name)
						.append(ChatColor.GREEN + " with uuid ")
						.append(ChatColor.AQUA + uuid)
						.append(ChatColor.GREEN + " is banned for ")
						.append(ChatColor.AQUA + ban.getReason())
						.append(ChatColor.GREEN + " until ")
						.append(ChatColor.AQUA + ban.getExpires())
						.create()
		);

	}
}
