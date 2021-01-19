package fr.skyfighttv.cts.Commands;

import fr.ChadOW.cinventory.citem.ItemCreator;
import fr.skyfighttv.cts.Commands.SubCommands.*;
import fr.skyfighttv.cts.Utils.FileManager;
import fr.skyfighttv.cts.Utils.Files;
import fr.skyfighttv.cts.Utils.PlayersManager;
import fr.skyfighttv.cts.Utils.WorldManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CTS implements CommandExecutor {
    public static List<Player> inGamePlayers = new ArrayList<>();
    public static List<Player> invinciblePlayers = new ArrayList<>();

    public static void setLobbyInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[0]);

        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        for (String items : config.getConfigurationSection("LobbyItems").getKeys(false)) {
            player.getInventory().setItem(config.getInt("LobbyItems." + items + ".Location"),
                    new ItemCreator(Material.getMaterial(config.getString("LobbyItems." + items + ".Material")), 0)
                            .setName(config.getString("LobbyItems." + items + ".Title"))
                            .setLores(config.getStringList("LobbyItems." + items + ".Lore"))
                            .getItem());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            boolean isSetup = config.getBoolean("IsSetup");

            if (args.length == 0) {
                if (player.hasPermission("CTS.staff")) {
                    player.sendMessage(langConfig.getString("NotFullCommandStaff"));
                } else {
                    player.sendMessage(langConfig.getString("NotFullCommandPlayer"));
                }
                return false;
            }

            if (isSetup) {
                if (args[0].equalsIgnoreCase("play")) {
                    CTSPlay.init(player);
                } else if (args[0].equalsIgnoreCase("leave")) {
                    CTSLeave.init(player);
                } else if (args[0].equalsIgnoreCase("kits")) {
                    if (!config.getBoolean("CTS.Kits")) {
                        player.sendMessage(langConfig.getString("CommandDisabled"));
                        return false;
                    }

                    CTSKits.init(player);
                } else if (args[0].equalsIgnoreCase("setkit")) {
                    if (!player.hasPermission("CTS.setkit")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }
                    if (args.length == 1) {
                        player.sendMessage(langConfig.getString("NotFullCommandSetKit"));
                        return false;
                    }

                    CTSSetKit.init(player, args[1]);
                } else if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!player.hasPermission("CTS.setspawn")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }
                    if (args.length == 1) {
                        player.sendMessage(langConfig.getString("NotFullCommandSetSpawn"));
                        return false;
                    }

                    CTSSetSpawn.init(player, args[1]);
                } else if (args[0].equalsIgnoreCase("setsheep")) {
                    if (!player.hasPermission("CTS.setsheep")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }
                    if (args.length == 1) {
                        player.sendMessage(langConfig.getString("NotFullCommandSetSheep"));
                        return false;
                    }

                    CTSSetSheep.init(player, args[1]);
                } else if (args[0].equalsIgnoreCase("setZone")) {
                    if (!player.hasPermission("CTS.setzone")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }
                    if (args.length == 1) {
                        player.sendMessage(langConfig.getString("NotFullCommandSetZone"));
                        return false;
                    }

                    CTSSetZone.init(player, args[1]);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (!player.hasPermission("CTS.reload")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }

                    FileManager.reloadAll();
                    try {
                        PlayersManager.saveAll();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    WorldManager.reload();

                    player.sendMessage(langConfig.getString("SuccessReload"));
                } else if (args[0].equalsIgnoreCase("stats")) {
                    CTSStats.init(player);
                } else {
                    if (player.hasPermission("CTS.staff"))
                        player.sendMessage(langConfig.getString("NotFullCommandStaff"));
                    else
                        player.sendMessage(langConfig.getString("NotFullCommandPlayer"));
                }
            } else {
                if (args[0].equalsIgnoreCase("setup")) {
                    if (!player.hasPermission("CTS.setup")) {
                        player.sendMessage(langConfig.getString("NoPermission"));
                        return false;
                    }

                    CTSSetup.init(player);
                } else {
                    player.sendMessage(langConfig.getString("SetupBefore"));
                }
            }
        }
        return false;
    }
}
