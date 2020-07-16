package stafftools.stafftools.playerDiePacket;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import stafftools.stafftools.StaffTools;
import stafftools.stafftools.Utiles;

import java.util.*;

public class InventoryGetter implements Listener {
    private StaffTools plugin;

    public InventoryGetter(StaffTools plugin) {
        this.plugin = plugin;
    }

    private Utiles utiles = new Utiles();

    public Inventory getDeaths(Player commandSender, String playerName) {

        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',
                "&5MUERTES DE " + playerName));

        try {

            if (!plugin.deathDrops.getConfigurationSection("Inventarios").contains(playerName)) {
                utiles.colorMSG(commandSender, "&cParese que ese jugador no tiene inventarios guardados o no ah muerto!");
                utiles.soindoP(commandSender, Sound.ENTITY_VILLAGER_NO, 1.5F);
                return null;
            }

            List<Integer> glassPaneSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
            List<Integer> allowed = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42);

            List<String> drops = plugin.deathDrops.getStringList("Inventarios." + playerName + ".Drops");

            ItemStack playerDeathSelect = new ItemStack(Material.CHEST);
            ItemMeta playerDeathSelectMeta = playerDeathSelect.getItemMeta();

            int track = 1;
            for (int i = 0; i < allowed.size(); i++) {

                if (i >= drops.size()) {
                    break;
                }

                assert playerDeathSelectMeta != null;
                playerDeathSelectMeta.setDisplayName(utiles.getColorMSG("&dMUERTE #" + track));
                playerDeathSelect.setItemMeta(playerDeathSelectMeta);
                playerDeathSelect.setAmount(i + 1);
                inventory.setItem(allowed.get(i), playerDeathSelect);
                track++;
            }


            ItemStack stack = new ItemStack(Material.EMERALD, 1);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName(" ");
            //stack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
            //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(meta);
            for (int glassPaneSlot : glassPaneSlots) {
                inventory.setItem(glassPaneSlot, stack);
            }




            ItemStack deleteAll = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta deleteAllmeta = deleteAll.getItemMeta();
            assert deleteAllmeta != null;
            deleteAllmeta.setDisplayName(utiles.getColorMSG("&c&lBORRAR TODOS &7(Asegurar que no aiga reclamos!)"));
            deleteAll.setItemMeta(deleteAllmeta);
            inventory.setItem(43, deleteAll);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return inventory;
    }
    public Inventory getAllDeaths(){

        Inventory allDeaths = Bukkit.createInventory(null, 54, utiles.getColorMSG("&0&lMUERTES GUARDADS"));

        List<String> players = new ArrayList<>(plugin.deathDrops.getConfigurationSection("Inventarios").getKeys(false));


        ItemStack deleteAll = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta deleteMeta = deleteAll.getItemMeta();
        assert deleteMeta != null;
        deleteMeta.setDisplayName(utiles.getColorMSG("&c&lBORRAR TODOS &7(Asegurar que no aiga reclamos!)"));
        deleteAll.setItemMeta(deleteMeta);
        allDeaths.setItem(53, deleteAll);

        for(int i = 0 ; i < players.size(); i++){
            ItemStack playerSection = new ItemStack(Material.EGG);
            ItemMeta playerSectionMeta = playerSection.getItemMeta();
            assert playerSectionMeta != null;
            playerSectionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + players.get(i)));
            playerSection.setItemMeta(playerSectionMeta);
            allDeaths.setItem(i, playerSection);
        }





        return allDeaths;
    }
}