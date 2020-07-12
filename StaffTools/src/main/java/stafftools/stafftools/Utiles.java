package stafftools.stafftools;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Utiles implements Listener {

    public void colorMSG(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void soindoP(Player player, Sound sound, float pitch) {
        player.playSound(player.getLocation(), sound, 100, pitch);
    }

    public String getColorMSG(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

}
