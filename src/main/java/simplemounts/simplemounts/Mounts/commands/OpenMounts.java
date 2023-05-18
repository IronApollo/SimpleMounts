package simplemounts.simplemounts.Mounts.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplemounts.simplemounts.Mounts.GUI.MountsPage;
import simplemounts.simplemounts.SimpleMounts;

import static org.bukkit.Bukkit.getPlayer;

public class OpenMounts implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {return false;}
        if(!(sender.hasPermission("SimpleMounts.OpenMounts.self"))) {return false;}

        Player player = (Player) sender;

        try {
            player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 2.5F,2.5F);
            if(args.length != 0) {
                if(!sender.hasPermission("SimpleMounts.OpenMounts.others")) {sender.sendMessage(ChatColor.RED + "No permission to view other player's mounts");}
                if(!(getPlayer(args[0]) instanceof Player) || getPlayer(args[0]) == null) {sender.sendMessage(ChatColor.RED + "Please select a valid player.");}

                new MountsPage(player,player);  //Will need to change to sending player vs target
                return true;

            } else {
                new MountsPage(player,player);
                return true;
            }

        } catch (Throwable e) {
            SimpleMounts.sendSystemError("Failed to Open Mount Menu",player,e);
            return false;
        }
    }
}
