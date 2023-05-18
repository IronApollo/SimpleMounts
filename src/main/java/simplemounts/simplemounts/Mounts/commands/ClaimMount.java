package simplemounts.simplemounts.Mounts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.json.simple.JSONObject;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Database.Mount;
import simplemounts.simplemounts.Util.Managers.EntityManager;

import java.io.IOException;
import java.util.ArrayList;

public class ClaimMount implements CommandExecutor {

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
        if(!(sender instanceof Player)) return true;
        if(!(sender.hasPermission("SimpleMounts.ClaimMounts"))) {return true;}

        Player player = (Player) sender;

        //environment checks
        if(!(player.getVehicle() instanceof LivingEntity)) {//Checks if its a living entity the player is riding
            SimpleMounts.sendUserError("Must be riding a ridable living entity to claim", player);
            return true;
        }

        LivingEntity le = (LivingEntity)player.getVehicle();

        if(le instanceof Pig || le instanceof Strider) {
            SimpleMounts.sendUserError("You really think I'd let you claim this as a mount?", player);
            return true;
        }



        //Code for claiming of mount
        if(!(le instanceof Horse)) {
            SimpleMounts.sendUserError("Only entities of type horse are supported at the moment", player);
            return true;    //temporary till more types
        }

        Horse horse = (Horse)le;

        if(horse.getOwner() == null) {
            SimpleMounts.sendUserError("The Horse must be tamed before you can claim it.",player);
            return true;
        }

        if(!horse.getOwner().equals(player)) {
            SimpleMounts.sendUserError("This is not your mount. Sending claim request...", player);
        }

        ArrayList<Mount> mounts = EntityManager.getMounts(player);

        SimpleMounts.sendPlayerMessage("Mounts Size: " + mounts.size() + " | Config value: " + SimpleMounts.getCustomConfig().getInt("Basic.max-mounts"),player);

        //Check if player is already at the max amount of mounts
        if(mounts.size() >= SimpleMounts.getCustomConfig().getInt("Basic.max-mounts")) {
            SimpleMounts.sendUserError("You are currently at the max amount of mounts",player);
            return true;
        }

        //Write as a transaction for saving and removing of entity
        try {
            JSONObject json = EntityManager.createEntitySave(horse,player);

            horse.remove(); //Remove original horse

            SimpleMounts.sendPlayerMessage("You have tamed a " + horse.getType().toString().toLowerCase() + "!", player);

        } catch (Throwable e) {
            //General Exception. Undo all actions
            SimpleMounts.sendSystemError("Failed to write entity to file", player,e);

        }
        return true;
    }

}
