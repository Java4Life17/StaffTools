package stafftools.stafftools.playerDiePacket;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import stafftools.stafftools.StaffTools;
import stafftools.stafftools.Utiles;

public class deathCommands implements CommandExecutor {
    private StaffTools plugin;

    public deathCommands(StaffTools plugin) {
        this.plugin = plugin;
    }
    private Utiles utiles = new Utiles();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("deathInv")) {


            if(!player.hasPermission("StaffTools.DeathInventories.Manage")){
                utiles.colorMSG(player, "&cNo tienes permisos para ejecutar ese comando!");
                utiles.soindoP(player, Sound.ENTITY_VILLAGER_NO, 0.9F);
            }
            try {

                if(args[0].equalsIgnoreCase("manejar")){
                    player.openInventory(plugin.inventoryGetter.getDeaths(player, args[1]));
                }else if(args[0].equalsIgnoreCase("todo")){
                    player.openInventory(plugin.inventoryGetter.getAllDeaths());
                }

            } catch (Exception e) {
                utiles.colorMSG(player, "&CError. &7/deathInv manejar <nombre de jugador>");
            }
        }

        return false;
    }
}
