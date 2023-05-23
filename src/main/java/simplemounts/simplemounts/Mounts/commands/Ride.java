package simplemounts.simplemounts.Mounts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Managers.EntityManager;

public class Ride implements CommandExecutor {

    /**
     * /mclaim
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (!(sender.hasPermission("SimpleMounts.Ride"))) {
            return true;
        }

        Player player = (Player) sender;

        Entity e = EntityManager.getSummonedMount(player);

        if(e == null) {SimpleMounts.sendUserError("Must have a mount summoned", player); return true;}

        e.teleport(player.getLocation());

        SimpleMounts.sendPlayerMessage("Yeehawwwwww",player);
        e.addPassenger(player);

        return true;
    }
}
