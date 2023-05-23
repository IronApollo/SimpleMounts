package simplemounts.simplemounts.Mounts.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Managers.EntityManager;

import java.io.IOException;

public class Release implements CommandExecutor {

    /**
     * /mrelease
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!(sender.hasPermission("SimpleMounts.release"))) {
            return false;
        }

        Player player = (Player) sender;

        if(!EntityManager.isSummoned(player)) {
            SimpleMounts.sendUserError("Must first have a summoned mount", player);
            return true;
        }

        EntityManager.removeMount(player);

        SimpleMounts.sendPlayerMessage("Your mount rides back into the wilderness...",player);
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ANGRY,1.0f,1.0f);

        return true;


    }
}
