package stafftools.stafftools;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.Objects;

public final class StaffTools extends JavaPlugin {

    public YamlFile Multicuentas;

    @Override
    public void onEnable() {
        Multicuentas = new YamlFile("plugins/StaffTools/Multicuentas.yml");
        cargarArchivo();

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        Objects.requireNonNull(getCommand("Mlcheck")).setExecutor(new Comandos(this));
        Objects.requireNonNull(getCommand("Mlcheck")).setTabCompleter(new AutoComplete(this));

    }

    private void cargarArchivo() {
        try {


            if (!Multicuentas.exists()) {
                this.saveResource("Multicuentas.yml", true);
            }
            Multicuentas.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        
    }

    public static class Utiles implements Listener {

        public void enviarMensajeDeColor(Player jugador, String mensaje){
            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
        }

        public void tocarSonido(Player jugador, Sound sonido){
            jugador.playSound(jugador.getLocation(), sonido, 100, 1.0F);
        }

    }
}
