package com.redstoner.bungeeBans.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class DisableJoinListener implements Listener {
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin(PreLoginEvent event) {
		event.setCancelled(true);

		event.setCancelReason(
				new ComponentBuilder(ChatColor.RED + "Joining is disabled because of a bungee error! Please notify an admin ASAP!")
						.create()
		);
	}
}
