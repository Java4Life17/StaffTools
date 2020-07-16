package stafftools.stafftools;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;
import stafftools.stafftools.playerDiePacket.ClickListener;
import stafftools.stafftools.playerDiePacket.InventoryGetter;
import stafftools.stafftools.playerDiePacket.deathCommands;
import stafftools.stafftools.playerDiePacket.listenForDeath;

import java.util.Objects;

public final class StaffTools extends JavaPlugin implements Listener{

    public YamlFile Multicuentas;
    public YamlFile deathDrops;
    public YamlFile Configuration;
    public InventoryGetter inventoryGetter = new InventoryGetter(this);
    public int cuentasPorIP = 2;



    @Override
    public void onEnable() {
        listenForDeath death = new listenForDeath(this);
        Multicuentas = new YamlFile("plugins/StaffTools/Multicuentas.yml");
        deathDrops = new YamlFile("plugins/StaffTools/deathDrops.yml");
        Configuration = new YamlFile("plugins/StaffTools/Configuration.yml");

        cargarArchivo();

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        Objects.requireNonNull(getCommand("Mlcheck")).setExecutor(new Comandos(this));
        Objects.requireNonNull(getCommand("Mlcheck")).setTabCompleter(new AutoComplete(this));
        Objects.requireNonNull(getCommand("deathInv")).setTabCompleter(new AutoComplete(this));
        getServer().getPluginManager().registerEvents(new listenForDeath(this), this);
        Objects.requireNonNull(getCommand("deathInv")).setExecutor(new deathCommands(this));
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ClickListener(this), this);

        cuentasPorIP = Configuration.getInt("CuentasPorIP");

    }

    private void cargarArchivo() {
        try {


            if (!Multicuentas.exists()) {
                this.saveResource("Multicuentas.yml", true);
            }
            Multicuentas.load();
            if(!deathDrops.exists()){
                this.saveResource("deathDrops.yml", true);
            }
            deathDrops.load();
            if(!Configuration.exists()){
                this.saveResource("Configuration.yml", true);
            }
            Configuration.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static class Utiles implements Listener {

        public void enviarMensajeDeColor(Player jugador, String mensaje){
            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
        }

        public void tocarSonido(Player jugador, Sound sonido){
            jugador.playSound(jugador.getLocation(), sonido, 100, 1.0F);
        }

    }
    @EventHandler
    public void dontMessUpTheGUI(InventoryClickEvent event){

        if(event.getView().getTitle().startsWith(ChatColor.translateAlternateColorCodes('&',
                "&5MUERTES DE "))) {
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100, 1.5F);
            event.setCancelled(true);

        }

    }
}
