# BungeeBans

A BungeeCord plugin that allows banning players.

## Building

To build the plugin jar file use `./gradlew shadowJar` on Linux or `gradlew.bat shadowJar` on Windows.

## Permissions & commands

These permissions should be added in the BungeeCord config.yml file and **NOT** on any of the Bukkit/Spigot/whatever servers.

| Permission        | Command                                     | Usage                                              |
|-------------------|---------------------------------------------|----------------------------------------------------|
| bungeebans.ban    | /ban \<username\> [reason]                  | Bans the specified player with an optional reason. |
| bungeebans.unban  | /unban \<username\> or /pardon \<username\> | Unbans the specified player.                       |
| bungeebans.getban | /getban \<username\>                        | Displays a player's ban status.                    |