package stafftools.stafftools;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Comandos implements CommandExecutor {

    private StaffTools plugin;

    public Comandos(StaffTools plugin) {
        this.plugin = plugin;
    }

    private Utiles utiles = new Utiles();

    //mlcheck cuentas <usuario>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equals("Mlcheck")) {
            try {

                if (args[0].equalsIgnoreCase("cuentas")) {

                    if (!player.hasPermission("Mlcheck.Cuentas")) {
                        utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        utiles.colorMSG(player, "&cNo tienes permiso para este comando!");
                        return false;
                    }

                    Player player1 = plugin.getServer().getPlayer(args[1]);
                    if (player1 == null) {
                        utiles.colorMSG(player, "&cEse jugador no esta en linea!");
                        utiles.soindoP(player, Sound.ENTITY_GUARDIAN_DEATH, 1.1F);
                        return false;
                    }
                    String ip = Objects.requireNonNull(player1.getAddress()).getAddress().getHostAddress();
                    ip = ip.replace(".", "_");
                    List<String> cuentas = plugin.Multicuentas.getStringList(ip + ".Cuentas");
                    int track = 1;
                    utiles.colorMSG(player, "&c&l=============================");

                    for (String cuenta : cuentas) {
                        utiles.colorMSG(player, "&a" + track + ": &3" + cuenta);
                        track++;
                    }
                    utiles.colorMSG(player, "&c&l=============================");

                    utiles.soindoP(player, Sound.EVENT_RAID_HORN, 1.8F);


                } else if (args[0].equalsIgnoreCase("reparar")) {
                    if (!player.hasPermission("Mlcheck.Cuentas")) {
                        utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        utiles.colorMSG(player, "&cNo tienes permiso para este comando!");
                        return false;
                    }
                    boolean exists = false;
                    String ipThatExists = "";
                    for (String ip : plugin.Multicuentas.getKeys(false)) {
                        if (plugin.Multicuentas.getStringList(ip + ".Cuentas").contains(args[1])) {
                            exists = true;
                            ipThatExists = ip;
                        }
                    }

                    if (!exists) {
                        utiles.colorMSG(player, "&cParese que esa cuanta no esta registrada con otra IP!");
                        utiles.soindoP(player, Sound.ENTITY_GUARDIAN_DEATH, 1.1F);
                        return false;
                    }
                    List<String> temp = new ArrayList<>(plugin.Multicuentas.getStringList(ipThatExists + ".Cuentas"));
                    String name = args[1];
                    temp.remove(name);
                    plugin.Multicuentas.set(ipThatExists + ".Cuentas", temp);
                    plugin.Multicuentas.save();
                    plugin.Multicuentas.load();

                    utiles.colorMSG(player, "&aCon exito se reparo esa cuenta. El usuario " + args[1] + " ya" +
                            "puede entrar y registrar su cuenta con su IP.");
                    utiles.soindoP(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F);
                    return true;


                } else if (args[0].equalsIgnoreCase("borrar")) {
                    if (!player.hasPermission("Mlcheck.Cuentas.Admin")) {
                        utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        utiles.colorMSG(player, "&cNo tienes permiso para este comando!");
                        return false;
                    }

                    boolean exists = false;
                    String ipVessel = "";

                    for (String ip : plugin.Multicuentas.getKeys(false)) {
                        String accountName = args[1];
                        if (plugin.Multicuentas.getStringList(ip + ".Cuentas").contains(accountName)) {
                            exists = true;
                            ipVessel = ip;
                        }
                    }

                    if (!exists) {
                        utiles.colorMSG(player, "&cParese que esa cuanta no esta registrada en el server!");
                        utiles.soindoP(player, Sound.ENTITY_GUARDIAN_DEATH, 1.1F);
                        return false;
                    }
                    plugin.Multicuentas.set(ipVessel, null);
                    plugin.Multicuentas.save();
                    plugin.Multicuentas.load();

                    utiles.soindoP(player, Sound.ENTITY_ARROW_HIT_PLAYER, 1.5F);
                    utiles.colorMSG(player, "&aCon exito se borro esta data de los archivos!");


                }
                else if(args[0].equalsIgnoreCase("reload")){
                    if (!player.hasPermission("Mlcheck.Cuentas.Admin")) {
                        utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        utiles.colorMSG(player, "&cNo tienes permiso para este comando!");
                        return false;
                    }

                    utiles.colorMSG(player, "&aEl plugin se ah recargado!");
                    utiles.soindoP(player, Sound.ENTITY_PHANTOM_DEATH, 1.2F);
                    plugin.Multicuentas.load();
                    plugin.Multicuentas.save();
                }


            } catch (Exception e) {
                utiles.colorMSG(player, "&cError en formato! Comandos similares:");
                utiles.colorMSG(player, "&5/Mlcheck cuentas <jugador> - &7Ver las cuentas de un jugador");
                utiles.colorMSG(player, "&5/Mlcheck fix <Nombre de Cuenta> - &7- Reparar una cuenta que fue" +
                        " registrada con otra IP.");
                utiles.colorMSG(player, "&5/Mlcheck borrar <jugador> - &7Borrar toda la data de una seccion. ");
                utiles.soindoP(player, Sound.ENTITY_GUARDIAN_DEATH, 1.1F);
            }
        }

        return false;
    }
}
