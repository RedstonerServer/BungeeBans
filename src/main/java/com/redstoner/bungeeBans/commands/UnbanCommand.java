package com.redstoner.bungeeBans.commands;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.PlayerBan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class UnbanCommand extends Command {
	HttpProfileRepository profileRepo = new HttpProfileRepository("minecraft");

	private BanManager<PlayerBan> bm;

	public UnbanCommand(BanManager<PlayerBan> bm) {
		super("unban", "bungeebans.unban", "pardon");

		this.bm = bm;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(
					new ComponentBuilder(ChatColor.RED + "Invalid command! ")
							.append("Usage: ")
							.append(ChatColor.AQUA + "/unban ")
							.append(ChatColor.GOLD + "<username> ")
							.create()
			);

			return;
		}

		Profile[] profiles = profileRepo.findProfilesByNames(args[0]);

		if (profiles.length != 1) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Invalid name!"));
			return;
		}

		String uuid = Util.dashUUID(profiles[0].getId());
		String name = profiles[0].getName();

		PlayerBan ban = bm.getBan(uuid);

		if (ban == null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "That player is not banned!"));
			return;
		}

		bm.removeBan(ban);

		try {
			bm.saveBans();
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Saved bans to file!"));
		} catch (IOException e) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to save bans to file!"));
			e.printStackTrace();

			bm.addBan(ban);
			return;
		}

		sender.sendMessage(
				new ComponentBuilder(ChatColor.GREEN + "Unbanned player ")
						.append(ChatColor.AQUA + name)
						.append(ChatColor.GREEN + " with uuid ")
						.append(ChatColor.AQUA + uuid)
						.create()
		);

	}
}
