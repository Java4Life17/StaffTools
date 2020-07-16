package stafftools.stafftools.playerDiePacket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import stafftools.stafftools.StaffTools;
import stafftools.stafftools.Utiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class listenForDeath implements Listener {
    private Utiles utiles = new Utiles();

    private StaffTools plugin;

    public listenForDeath(StaffTools plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void saveDeathItems(PlayerDeathEvent event) {

        if (event.getDrops().size() <= 3) {
            return;
        }

        Player player = event.getEntity();
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',
                "&5&lInventario de la muerte de " + event.getEntity().getName()));


        int exp = event.getNewTotalExp();

        for (ItemStack stack : event.getDrops()) {
            inventory.addItem(stack);
        }
        if (inventory.getSize() <= 0) {
            return;
        }
        String inventoryStringToSave = utiles.toBase64(inventory);

        if (!plugin.deathDrops.getConfigurationSection("Inventarios").contains(player.getName())) {
            plugin.deathDrops.getConfigurationSection("Inventarios").createSection(player.getName());
            List<String> drops = new ArrayList<>();
            drops.add(inventoryStringToSave);
            plugin.deathDrops.set("Inventarios." + player.getName() + ".Drops", drops);
            plugin.deathDrops.set("Inventarios." + player.getName() + ".EXP", exp);

        } else {
            List<String> drops = plugin.deathDrops.getStringList("Inventarios." + player.getName() + ".Drops");
            drops.add(inventoryStringToSave);
            plugin.deathDrops.set("Inventarios." + player.getName() + ".Drops", drops);
            plugin.deathDrops.set("Inventarios." + player.getName() + ".EXP", exp);
        }
        try {

            System.out.println(utiles.getColorMSG("&6El usuario " + player.getName() + " murio en el mundo " + Objects.requireNonNull(event.getEntity().getLocation()
                    .getWorld()).getName() + " en las cordenadas &b" + event.getEntity().getLocation().getBlockX() + " " +
                    event.getEntity().getLocation().getBlockY() + " " + event.getEntity().getLocation().getBlockZ() + " &6por la razon: &e(&f" +
                    event.getDeathMessage() + "&e)"));

            for(Player player1: plugin.getServer().getOnlinePlayers()){

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                if(player1.hasPermission("StaffTools.DeathInfo")){
                    player1.sendMessage(" ");
                    player1.sendMessage(" ");
                    player1.sendMessage(utiles.getColorMSG("&6El usuario " + player.getName() + " murio en el mundo " + Objects.requireNonNull(event.getEntity().getLocation()
                            .getWorld()).getName() + " en las cordenadas &b" + event.getEntity().getLocation().getBlockX() + " " +
                            event.getEntity().getLocation().getBlockY() + " " + event.getEntity().getLocation().getBlockZ() + " &6por la razon: &e(&f" +
                            event.getDeathMessage() + "&e)&b " + dtf.format(now) + "&7 ESTE MENSAJE ES IMPORTANTE PARA LOS STAFF"));
                    player1.sendMessage(" ");
                    player1.sendMessage(" ");


                    System.out.println(" ");
                    System.out.println(" ");
                    System.out.println(utiles.getColorMSG("&6El usuario " + player.getName() + " murio en el mundo " + Objects.requireNonNull(event.getEntity().getLocation()
                            .getWorld()).getName() + " en las cordenadas &b" + event.getEntity().getLocation().getBlockX() + ", " +
                            event.getEntity().getLocation().getBlockY() + ", " + event.getEntity().getLocation().getBlockZ() + " &6por la razon: &e(&f" +
                            event.getDeathMessage() + "&e)&b " + dtf.format(now) + "&7 ESTE MENSAJE ES IMPORTANTE PARA LOS STAFF"));
                    System.out.println(" ");
                    System.out.println(" ");

                }
            }

            plugin.deathDrops.save();
            plugin.deathDrops.load();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
