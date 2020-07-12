package stafftools.stafftools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JoinListener implements Listener {
    private StaffTools plugin;

    public JoinListener(StaffTools plugin) {
        this.plugin = plugin;
    }

    private Utiles utiles = new Utiles();

    @EventHandler
    public void playerJoinServer(PlayerJoinEvent event) throws IOException, InvalidConfigurationException {

        Player player = event.getPlayer();

        String playerIP = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

        for (String ip : plugin.Multicuentas.getKeys(false)) {
            String ip_one = ip.replace("_", ".");

            if (playerIP.equals(ip_one)) {
                List<String> cuentas = plugin.Multicuentas.getStringList(ip + ".Cuentas");
                if (cuentas.contains(player.getName())) {
                    return;
                }

                int size = plugin.Multicuentas.getStringList(ip + ".Cuentas").size();
                int maxCuentas = plugin.Multicuentas.getInt(ip + ".MaxCuentas");


                if (size >= maxCuentas) {

                    if (plugin.Multicuentas.getBoolean(ip + ".Bypass")) {

                        cuentas.add(player.getName());
                        plugin.Multicuentas.set(ip + ".Cuentas", cuentas);


                        plugin.Multicuentas.save();
                        plugin.Multicuentas.load();


                        return;


                    }

                    StringBuilder finalCuentas = new StringBuilder();
                    for (String string : cuentas) {
                        finalCuentas.append(string).append(" ");
                    }
                    player.kickPlayer(utiles.getColorMSG("&cHas excedido el limite de cuentas permitidas! Cuentas: &f&l" + finalCuentas));
                    return;
                }
                cuentas.add(player.getName());
                plugin.Multicuentas.set(ip + ".Cuentas", cuentas);
                plugin.Multicuentas.save();
                plugin.Multicuentas.load();
                return;

            }


        }

        boolean registradoEnotraIP = false;

        for (String ip : plugin.Multicuentas.getKeys(false)) {
            String ip1 = ip.replace("_", ".");
            for (String cuenta : plugin.Multicuentas.getStringList(ip + ".Cuentas")) {
                if (cuenta.equals(player.getName())) {
                    registradoEnotraIP = true;
                }
            }
        }

        if(registradoEnotraIP){
            player.kickPlayer(utiles.getColorMSG("&cEsta cuenta fue registrada en otra IP. Si crees que esto es un error, contacta a un " +
                    "staff en nuestro discord."));
        }else{

            playerIP = playerIP.replace(".", "_");
            plugin.Multicuentas.createSection(playerIP);
            List<String> cuentas = new ArrayList<>();
            cuentas.add(player.getName());
            plugin.Multicuentas.set(playerIP + ".Cuentas", cuentas);
            plugin.Multicuentas.set(playerIP + ".Bypass", false);
            plugin.Multicuentas.set(playerIP + ".MaxCuentas", 2);
            plugin.Multicuentas.save();
            plugin.Multicuentas.load();
        }


    }


}



