package com.redstoner.bungeeBans.listeners;

import com.mojang.api.profiles.Profile;
import com.redstoner.bungeeBans.BanManager;
import com.redstoner.bungeeBans.Util;
import com.redstoner.bungeeBans.json.Ban;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BanJoinListener<T extends Ban> implements Listener {
	private String        name;
	private BanManager<T> bm;

	public BanJoinListener(String name, BanManager<T> bm) {
		this.name = name;
		this.bm = bm;
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin(PreLoginEvent event) {
		event.setCancelled(true);

		PendingConnection conn = event.getConnection();
		String            name = conn.getName();

		Profile[] profiles = Util.findProfilesByNames(name);

		if (profiles.length != 1) {
			event.setCancelReason(
					new ComponentBuilder(ChatColor.RED + "Server error occured while joining: ")
							.append(ChatColor.AQUA + "The mojang API does not know your UUID!")
							.create()
			);

			return;
		}

		T ban = bm.getBan(Util.dashUUID(profiles[0].getId()));

		if (ban != null) {
			event.setCancelReason(
					new ComponentBuilder(ChatColor.RED + "You were " + name + " banned by ")
							.append(ChatColor.AQUA + ban.getSource())
							.append(ChatColor.RED + " for ")
							.append(ChatColor.AQUA + ban.getReason())
							.append(ChatColor.RED + " on ")
							.append(ChatColor.AQUA + ban.getCreated())
							.append(ChatColor.RED + " until ")
							.append(ChatColor.AQUA + ban.getExpires())
							.create()
			);
		} else {
			event.setCancelled(false);
		}
	}
}
