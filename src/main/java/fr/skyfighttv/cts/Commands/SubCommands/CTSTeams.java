package fr.skyfighttv.cts.Commands.SubCommands;

import fr.ChadOW.cinventory.cinventory.CInventory;
import fr.ChadOW.cinventory.citem.CItem;
import fr.ChadOW.cinventory.citem.ItemCreator;
import fr.skyfighttv.cts.Commands.CTS;
import fr.skyfighttv.cts.Utils.FileManager;
import fr.skyfighttv.cts.Utils.Files;
import fr.skyfighttv.cts.Utils.GameManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CTSTeams {
    public static void init(Player player) {
        if (CTS.inGamePlayers.contains(player)) {
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            CInventory cInventory = new CInventory(config.getInt("GUI.Teams.Size"), config.getString("GUI.Teams.Title"));

            CItem blueTeam = new CItem(new ItemCreator(Material.getMaterial(config.getString("GUI.Teams.Items.BlueTeam.Material")), 0)
                    .setName(config.getString("GUI.Teams.Items.BlueTeam.Title"))
                    .setLores(config.getStringList("GUI.Teams.Items.BlueTeam.Lore")))
                    .setSlot(config.getInt("GUI.Teams.Items.BlueTeam.Location"));

            blueTeam.addEvent((cInventory1, cItem, player1, clickContext) -> {
                if (GameManager.getTeamPlayers().containsKey(player.getWorld())) {
                    YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);

                    HashMap<Player, String> teamPlayers = GameManager.getTeamPlayers().get(player.getWorld());
                    teamPlayers.put(player, langConfig.getString("TeamsName.Blue"));

                    GameManager.getTeamPlayers().put(player.getWorld(), teamPlayers);

                    player.sendMessage(langConfig.getString("SuccessChooseTeam")
                            .replaceAll("%team%", langConfig.getString("TeamsName.Blue")));
                }
            });
            cInventory.addElement(blueTeam);

            CItem redTeam = new CItem(new ItemCreator(Material.getMaterial(config.getString("GUI.Teams.Items.RedTeam.Material")), 0)
                    .setName(config.getString("GUI.Teams.Items.RedTeam.Title"))
                    .setLores(config.getStringList("GUI.Teams.Items.RedTeam.Lore")))
                    .setSlot(config.getInt("GUI.Teams.Items.RedTeam.Location"));

            redTeam.addEvent((cInventory1, cItem, player1, clickContext) -> {
                if (GameManager.getTeamPlayers().containsKey(player.getWorld())) {
                    YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);

                    HashMap<Player, String> teamPlayers = GameManager.getTeamPlayers().get(player.getWorld());
                    teamPlayers.put(player, langConfig.getString("TeamsName.Red"));

                    GameManager.getTeamPlayers().put(player.getWorld(), teamPlayers);

                    player.sendMessage(langConfig.getString("SuccessChooseTeam")
                            .replaceAll("%team%", langConfig.getString("TeamsName.Red")));
                }
            });
            cInventory.addElement(redTeam);

            cInventory.open(player);
        }
    }
}
