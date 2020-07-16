package stafftools.stafftools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoComplete implements TabCompleter {

    private StaffTools plugin;

    public AutoComplete(StaffTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("mlcheck")) {

            List<String> cuentas = new ArrayList<>();
            for (String ip : plugin.Multicuentas.getKeys(false)) {
                for (String cuenta : plugin.Multicuentas.getStringList(ip + ".Cuentas")) {
                    if (!cuentas.contains(cuenta)) {
                        cuentas.add(cuenta);
                    }
                }
            }

            if (!player.hasPermission("Mlcheck.Cuentas")) {
                return null;
            }
            List<String> subCommands = Arrays.asList("cuentas", "reparar", "borrar");
            List<String> onlinePlayers = new ArrayList<>();
            List<String> results = new ArrayList<>();
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                onlinePlayers.add(player1.getName());

            }


            if (args.length == 1) {

                for (String subCommand : subCommands) {
                    if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                        results.add(subCommand);
                    }
                }
                return results;

            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("cuentas")) {
                    for (String onlinePlayer : onlinePlayers) {
                        if (onlinePlayer.toLowerCase().startsWith(args[1].toLowerCase())) {
                            results.add(onlinePlayer);
                        }
                    }
                    return results;
                } else if (args[0].equalsIgnoreCase("reparar")) {


                    for (String cuenta : cuentas) {
                        if (cuenta.toLowerCase().startsWith(args[1].toLowerCase())) {
                            results.add(cuenta);
                        }
                    }
                    return results;
                } else if (args[0].equalsIgnoreCase("borrar")) {
                    for (String cuenta : cuentas) {
                        if (cuenta.toLowerCase().startsWith(args[1].toLowerCase())) {
                            results.add(cuenta);
                        }
                    }
                    return results;
                }


            }
        } else if (command.getName().equalsIgnoreCase("deathInv")) {

            if (!player.hasPermission("StaffTools.DeathInventories.Manage")) {
                return null;
            }

            List<String> subCommands = Arrays.asList("manejar", "todo");
            List<String> finalOne = new ArrayList<>();

            if (args.length == 1) {
                for(String subCommand: subCommands){
                    if(subCommand.toLowerCase().startsWith(args[0].toLowerCase())){finalOne.add(subCommand);}
                }

                return finalOne;
            }
            if (args.length == 2) {
                if(!args[0].equalsIgnoreCase("manejar")){
                    return null;
                }
                List<String> deathPlayers = new ArrayList<>(plugin.deathDrops.getConfigurationSection("Inventarios").getKeys(false));

                for (String s : deathPlayers) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;

            }
        }

        return null;
    }


}
