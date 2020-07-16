package stafftools.stafftools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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
    public void playerJoin(AsyncPlayerPreLoginEvent event) throws IOException, InvalidConfigurationException {
        String playerName = event.getName();
        String dottedIP = event.getAddress().getHostAddress();
        String dashed_IP = dottedIP.replace(".", "_");

        boolean regInOtherIP = false;
        for (String ip : plugin.Multicuentas.getKeys(false)) {
            if (plugin.Multicuentas.getStringList(ip + ".Cuentas").contains(playerName)) {
                if (!ip.equals(dashed_IP)) {
                    regInOtherIP = true;
                }
            }
        }
        if (regInOtherIP) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, utiles.getColorMSG("&cEsta cuenta esta registrada en otra ip. Si " +
                    "crees que esto es un error, contacta a nuestro personal en discord."));
            return;
        }

        if (plugin.Multicuentas.getKeys(false).contains(dashed_IP)) {
            List<String> cuentas = plugin.Multicuentas.getStringList(dashed_IP + ".Cuentas");
            int size = cuentas.size();
            int maxAllowed = plugin.Multicuentas.getInt(dashed_IP + ".MaxCuentas");


            if (!cuentas.contains(playerName) && size < maxAllowed) {
                cuentas.add(playerName);
                plugin.Multicuentas.set(dashed_IP + ".Cuentas", cuentas);
                plugin.Multicuentas.save();
                plugin.Multicuentas.load();
                return;
            }
            if (!cuentas.contains(playerName) && plugin.Multicuentas.getBoolean(dashed_IP + ".Bypass")) {
                cuentas.add(playerName);
                plugin.Multicuentas.set(dashed_IP + ".Cuentas", cuentas);
                plugin.Multicuentas.save();
                plugin.Multicuentas.load();
                return;
            }
            if (!cuentas.contains(playerName) && size >= maxAllowed) {
                StringBuilder kickedMessageAccounts = new StringBuilder();
                for (String cuenta : cuentas) {
                    kickedMessageAccounts.append(cuenta).append(", ");
                }
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, utiles.getColorMSG("&cHas alcansado el limite de cuentas en esta ip! "
                        + "Tus otras cuentas: &f " + kickedMessageAccounts));
            }

        } else {
            List<String> cuentasNuevas = new ArrayList<>();
            cuentasNuevas.add(playerName);
            plugin.Multicuentas.createSection(dashed_IP);
            plugin.Multicuentas.set(dashed_IP + ".Cuentas", cuentasNuevas);
            plugin.Multicuentas.set(dashed_IP + ".Bypass", false);
            plugin.Multicuentas.set(dashed_IP + ".MaxCuentas", plugin.cuentasPorIP);
            plugin.Multicuentas.save();
            plugin.Multicuentas.load();
        }

    }


}



