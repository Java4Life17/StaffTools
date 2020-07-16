package stafftools.stafftools.playerDiePacket;

import com.sun.corba.se.impl.encoding.CDRInputObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import stafftools.stafftools.StaffTools;
import stafftools.stafftools.Utiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClickListener implements Listener {

    private StaffTools plugin;

    public ClickListener(StaffTools plugin) {
        this.plugin = plugin;
    }

    private Utiles utiles = new Utiles();

    @EventHandler
    public void onDeathsGUI(InventoryClickEvent event) throws IOException, InvalidConfigurationException {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().startsWith(ChatColor.translateAlternateColorCodes('&',
                "&5MUERTES DE "))) {

            String deathPlayer = event.getView().getTitle().replace(ChatColor.translateAlternateColorCodes('&',
                    "&5MUERTES DE "), "");
            List<String> drops = plugin.deathDrops.getStringList("Inventarios." + deathPlayer + ".Drops");
            List<Integer> allowed = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22,
                    23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42);
            List<Integer> glassPaneSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18,
                    26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
            List<Integer> somethingElse = Arrays.asList(46, 47, 48, 50, 51, 52);
            int clickedDeathChest = event.getSlot();
            if (event.getCurrentItem() == null) {
                return;
            }
            if (allowed.contains(clickedDeathChest) && Objects.requireNonNull(event.getCurrentItem()).getType() == Material.CHEST) {
                int inventoryNumber = event.getCurrentItem().getAmount();

                Inventory inventory = utiles.fromBase64(drops.get(inventoryNumber - 1), deathPlayer, inventoryNumber);

                //45 53 49

                ItemStack returnItems = new ItemStack(Material.ENDER_CHEST);
                ItemMeta returnMeta = returnItems.getItemMeta();
                assert returnMeta != null;
                returnMeta.setDisplayName(utiles.getColorMSG("&aREGRESAR INVENTARIO"));
                List<String> returnLore = new ArrayList<>();
                returnLore.add(utiles.getColorMSG("&7Usa esta opcion para regresar los items"));
                returnLore.add(utiles.getColorMSG("&7a el jugador que los perdio."));
                returnLore.add(" ");
                returnLore.add(" ");
                returnLore.add(utiles.getColorMSG("&4&lIMPORTANTE: &5por favor asegurate que ese usuario tenga el"));
                returnLore.add(utiles.getColorMSG("&5inventario vacio antes de regresarle sus items. Asegurate que"));
                returnLore.add(utiles.getColorMSG("&5El usuario tampoco tenga armadura puesta o esto podria causar otro"));
                returnLore.add(utiles.getColorMSG("&5bug&7!"));

                returnMeta.setLore(returnLore);
                returnItems.setItemMeta(returnMeta);
                inventory.setItem(53, returnItems);

                ItemStack removeItems = new ItemStack(Material.BARRIER);
                ItemMeta removeMeta = removeItems.getItemMeta();
                assert removeMeta != null;
                removeMeta.setDisplayName(utiles.getColorMSG("&cBORRAR INVENTARIO"));
                List<String> removeLore = new ArrayList<>();
                removeLore.add(utiles.getColorMSG("&7Usa esta opcion para borrar los items "));
                removeLore.add(utiles.getColorMSG("&7que el jugador perdio."));
                removeMeta.setLore(removeLore);
                removeItems.setItemMeta(removeMeta);
                inventory.setItem(45, removeItems);

                ItemStack stack = new ItemStack(Material.TOTEM_OF_UNDYING);
                ItemMeta stackMeta = stack.getItemMeta();
                assert stackMeta != null;
                stackMeta.setDisplayName(utiles.getColorMSG("&7_"));
                stack.setItemMeta(stackMeta);

                ItemStack regresar = new ItemStack(Material.REDSTONE);
                ItemMeta regresarMeta = regresar.getItemMeta();
                assert regresarMeta != null;
                regresarMeta.setDisplayName(utiles.getColorMSG("&dMENU PRINCIPAL"));
                regresar.setItemMeta(regresarMeta);
                inventory.setItem(49, regresar);

                for (int totem : somethingElse) {
                    inventory.setItem(totem, stack);
                }

                player.closeInventory();
                player.openInventory(inventory);


            } else if (event.getSlot() == 43 && event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                if (drops.size() <= 0) {
                    utiles.soindoP(player, Sound.ENTITY_ARROW_HIT, 0.4F);
                    return;
                }
                drops = new ArrayList<>();
                plugin.deathDrops.getConfigurationSection("Inventarios").set(deathPlayer, null);
                plugin.deathDrops.save();
                plugin.deathDrops.load();

                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, "&aSe borro todo el contenido de ese jugador.");
                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, " ");
                player.closeInventory();
                utiles.soindoP(player, Sound.BLOCK_BAMBOO_HIT, 1.3F);
                utiles.soindoP(player, Sound.ENTITY_GENERIC_EXPLODE, 0.4F);

            }


        } else if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&',
                "&0COSAS PERDIDAS DE "))) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }

            String argument = event.getView().getTitle().replace(ChatColor.translateAlternateColorCodes('&',
                    "&0"), "");
            String[] arguments = argument.split(" ");
            String deathPlayersName = arguments[3];
            int deathInventoryNumber = Integer.parseInt(arguments[4].replace("#", ""));
            List<String> drops = plugin.deathDrops.getStringList("Inventarios." + deathPlayersName + ".Drops");

            if (event.getSlot() == 45) {
                drops.remove(deathInventoryNumber - 1);
                plugin.deathDrops.set("Inventarios." + deathPlayersName + ".Drops", drops);
                plugin.deathDrops.save();
                plugin.deathDrops.load();

                utiles.colorMSG(player, "&aCon exito se borro ese inventario!");
                utiles.soindoP(player, Sound.ENTITY_WITCH_CELEBRATE, 1.6F);

                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    if (player1.hasPermission("StaffTools.DeathInfo")) {
                        utiles.colorMSG(player1, "&5El usuario &l" + player.getName() + " &5borro el inventario numero &l" + deathInventoryNumber
                                + " &5de los inventarios de las muertes de &c" + deathPlayersName + "&7.");
                    }
                }

                player.closeInventory();
            } else if (event.getSlot() == 49) {
                InventoryGetter getter = new InventoryGetter(plugin);
                player.closeInventory();
                player.openInventory(getter.getDeaths(player, deathPlayersName));
                utiles.soindoP(player, Sound.BLOCK_BARREL_CLOSE, 1.5F);
            } else if (event.getSlot() == 53) {



/*
                if (player.getName().equalsIgnoreCase(deathPlayersName)) {
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cNo puedes regresarte tus propias cosas, pidele a otro staff que lo haga por ti.");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }



 */

                Player deathMan = plugin.getServer().getPlayer(deathPlayersName);

                if (deathMan == null) {
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEse jugador no esta conectado.");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }

                int notEmpty = 0;

                for (ItemStack stack : deathMan.getInventory()) {
                    if (stack != null) {
                        notEmpty++;
                    }
                }


                if (notEmpty != 0) {

                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEl jugador no tiene el inventario vacio. Asegurate que tenga su inventario vacio y que no tenga" +
                            " armadura puesta!");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;

                }

                if (deathMan.getInventory().getHelmet() != null) {

                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEl jugador no tiene el inventario vacio. Asegurate que tenga su inventario vacio y que no tenga" +
                            " armadura puesta!");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }
                if (deathMan.getInventory().getChestplate() != null) {

                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEl jugador no tiene el inventario vacio. Asegurate que tenga su inventario vacio y que no tenga" +
                            " armadura puesta!");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }
                if (deathMan.getInventory().getLeggings() != null) {

                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEl jugador no tiene el inventario vacio. Asegurate que tenga su inventario vacio y que no tenga" +
                            " armadura puesta!");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }
                if (deathMan.getInventory().getBoots() != null) {

                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, "&cEl jugador no tiene el inventario vacio. Asegurate que tenga su inventario vacio y que no tenga" +
                            " armadura puesta!");
                    utiles.colorMSG(player, " ");
                    utiles.colorMSG(player, " ");
                    player.closeInventory();
                    utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.3F);
                    return;
                }


                Inventory copyGiven = utiles.fromBase64(drops.get(deathInventoryNumber - 1), deathPlayersName, deathInventoryNumber);

                boolean helmetGiven = false;
                ItemStack helmet = null;

                boolean chestlateGiven = false;
                ItemStack chestplate = null;

                boolean leggingsGiven = false;
                ItemStack leggints = null;

                boolean bootsGiven = false;
                ItemStack boots = null;

                List<Integer> notAvailable = new ArrayList<>();

                for (int i = 0; i < copyGiven.getSize(); i++) {


                    if (!helmetGiven) {
                        if (copyGiven.getItem(i) == null) {
                            doNothing();
                        } else if (Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.DIAMOND_HELMET) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.GOLDEN_HELMET) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.IRON_HELMET) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.LEATHER_HELMET) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.CHAINMAIL_HELMET)) {
                            helmetGiven = true;
                            helmet = copyGiven.getItem(i);
                            copyGiven.remove(Objects.requireNonNull(copyGiven.getItem(i)));
                            notAvailable.add(i);
                        }
                    }
                    if (copyGiven.getItem(i) == null) {
                        doNothing();
                    } else if (!chestlateGiven) {
                        if (Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.DIAMOND_CHESTPLATE) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.GOLDEN_CHESTPLATE) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.IRON_CHESTPLATE) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.LEATHER_CHESTPLATE) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
                            chestlateGiven = true;
                            chestplate = copyGiven.getItem(i);
                            copyGiven.remove(Objects.requireNonNull(copyGiven.getItem(i)));
                            notAvailable.add(i);
                        }
                    }
                    if (copyGiven.getItem(i) == null) {
                        doNothing();
                    } else if (!leggingsGiven) {
                        if (Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.DIAMOND_LEGGINGS) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.GOLDEN_LEGGINGS) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.IRON_LEGGINGS) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.LEATHER_LEGGINGS) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.CHAINMAIL_LEGGINGS)) {
                            leggingsGiven = true;
                            leggints = copyGiven.getItem(i);
                            copyGiven.remove(Objects.requireNonNull(copyGiven.getItem(i)));
                            notAvailable.add(i);
                        }
                    }
                    if (copyGiven.getItem(i) == null) {
                        doNothing();
                    } else if (!bootsGiven) {
                        if (Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.DIAMOND_BOOTS) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.GOLDEN_BOOTS) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.IRON_BOOTS) || Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.LEATHER_BOOTS) ||
                                Objects.requireNonNull(copyGiven.getItem(i)).getType().equals(Material.CHAINMAIL_BOOTS)) {
                            bootsGiven = true;
                            boots = copyGiven.getItem(i);
                            copyGiven.remove(Objects.requireNonNull(copyGiven.getItem(i)));
                            notAvailable.add(i);
                        }


                    }
                }

                ItemStack[] stacks = {boots, leggints, chestplate, helmet};
                player.getInventory().setArmorContents(stacks);

                boolean done = false;
                int slotReady = 0;

                for (int i = 0; i < copyGiven.getSize(); i++) {
                    if (!done && !notAvailable.contains(i)) {
                        done = true;
                        slotReady = i;
                    }
                }
                if (copyGiven.getItem(slotReady) != null) {
                    deathMan.getInventory().setItemInOffHand(copyGiven.getItem(slotReady));
                    copyGiven.remove(Objects.requireNonNull(copyGiven.getItem(slotReady)));
                }
                for (ItemStack stack : copyGiven) {
                    if (stack != null) {
                        deathMan.getInventory().addItem(stack);
                    }
                }


                drops.remove((deathInventoryNumber - 1));



                player.setTotalExperience(plugin.deathDrops.getInt("Inventarios" + deathPlayersName + ".EXP"));
                plugin.deathDrops.set("Inventarios." + deathPlayersName + ".EXP", 0);

                player.closeInventory();

                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, "&aCon exito se le regreso este inventario a " + deathPlayersName + "!");
                utiles.colorMSG(player, " ");
                utiles.colorMSG(player, " ");
                player.closeInventory();
                utiles.soindoP(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1.3F);

                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    if (player1.hasPermission("StaffTools.DeathInfo")) {
                        utiles.colorMSG(player1, "&5El usuario &l" + player.getName() + " &5regreso el inventario numero &l" + deathInventoryNumber
                                + " &5de los inventarios de las muertes de &c" + deathPlayersName + "&7.");
                    }
                }
                if (drops.size() == 0 ||
                        plugin.deathDrops.getStringList("Inventarios." + deathPlayersName + ".Drops").size() == 1) {
                    plugin.deathDrops.getConfigurationSection("Inventarios").set(deathPlayersName, null);
                } else {
                    plugin.deathDrops.set("Inventarios." + deathPlayersName + ".Drops", drops);
                }
                plugin.deathDrops.save();
                plugin.deathDrops.load();

            }

        } else if (event.getView().getTitle().equalsIgnoreCase(utiles.getColorMSG("&0&lMUERTES GUARDADS"))) {
            event.setCancelled(true);

            if (event.getClickedInventory() == player.getInventory()) {
                return;
            }


            if (event.getCurrentItem() == null) {
                return;
            }

            ItemStack clickedItem = event.getCurrentItem();
            String playerName = Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName()
                    .replace(ChatColor.translateAlternateColorCodes('&', "&b"), "");

            if (event.getSlot() >= 0 && event.getSlot() < 53) {
                InventoryGetter getter = new InventoryGetter(plugin);

                if (!plugin.deathDrops.getConfigurationSection("Inventarios").getKeys(false).contains(playerName) || !clickedItem.getType().equals(Material.EGG)) {
                    return;
                }
                utiles.soindoP(player, Sound.ENTITY_ARROW_HIT_PLAYER, 0.3F);
                player.closeInventory();
                player.openInventory(getter.getDeaths(player, playerName));

            }

            boolean notEmpty = false;
            if (event.getSlot() == 53) {
                for (int i = 0; i < 53; i++) {
                    ItemStack clickedItems = event.getInventory().getItem(i);
                    if (clickedItems != null) {
                        String playerName1 = Objects.requireNonNull(clickedItems.getItemMeta()).getDisplayName()
                                .replace(ChatColor.translateAlternateColorCodes('&', "&b"), "");
                        plugin.deathDrops.set("Inventarios." + playerName1, null);
                        notEmpty = true;
                    }
                }
                if (notEmpty) {
                    player.closeInventory();
                    utiles.soindoP(player, Sound.ITEM_TRIDENT_HIT_GROUND, 1.3F);
                    utiles.soindoP(player, Sound.ENTITY_GENERIC_EXPLODE, 1.4F);
                    utiles.colorMSG(player, "&aCon exito se borraron todos esos jugadores de la base de datos!");
                    plugin.deathDrops.save();
                    plugin.deathDrops.load();
                } else {
                    utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 1.4F);
                    utiles.colorMSG(player, "&cNo hay nada que borrar!");
                    player.closeInventory();
                }
            }
        }

    }

    private void doNothing() {
    }

}

