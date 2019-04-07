# BungeeBans

A BungeeCord plugin that allows banning players.

## Building

To build the plugin jar file use `./gradlew shadowJar` on Linux or `gradlew.bat shadowJar` on Windows.

## Permissions & commands

These permissions should be added in the BungeeCord config.yml file and **NOT** on any of the Bukkit/Spigot/whatever servers.

| Permission          | Command                                               | Usage                                              |
|---------------------|-------------------------------------------------------|----------------------------------------------------|
| bungeebans.ban      | /ban \<username/uuid\> [reason]                       | Bans the specified player with an optional reason. |
| bungeebans.banip    | /banip \<ip\> [reason]                                | Bans the specified IP with an optional reason.     |
| bungeebans.unban    | /unban \<username/uuid\> or /pardon \<username/uuid\> | Unbans the specified player.                       |
| bungeebans.unbanip  | /unbanip \<ip\> or /pardonip \<ip\>                   | Unbans the specified IP.                           |
| bungeebans.getban   | /getban \<username/uuid\>                             | Displays a player's ban status.                    |
| bungeebans.getipban | /getipban \<ip\>                                      | Displays an IP's ban status.                       |