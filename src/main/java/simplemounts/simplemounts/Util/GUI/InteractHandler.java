package simplemounts.simplemounts.Util.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.json.simple.JSONObject;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Database.Database;
import simplemounts.simplemounts.Util.Database.Mount;
import simplemounts.simplemounts.Util.Managers.EntityManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static simplemounts.simplemounts.SimpleMounts.checkPlayerFolder;

public class InteractHandler implements Listener {

    public InteractHandler(SimpleMounts plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        //Perm items can't be dropped in survival
        if(ItemManager.getPermItems().contains(event.getItemDrop().getItemStack())){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if(ItemManager.getPermItems().contains(event.getCurrentItem())) event.setCancelled(true);
        if(event.getClickedInventory() == null) return; //Catch if player just presses esc to exit inventory
        if(event.getClickedInventory().getSize() != 9) return; //May need to be more specific in terms of which inventory

        int clicked = event.getSlot();
        Player player = (Player)event.getWhoClicked();

        //If its a horse egg, summon the horse and check if all others are unsummoned

        ArrayList<Mount> mounts = Database.getMounts(player);

        if(clicked > mounts.size()-1) {event.setCancelled(true);return;} //Invalid place clicked. Prevents console spam

        try {

            //Logic for if a horse stored or summoned
            if(EntityManager.isSummoned(player)) {
                AbstractHorse h = (AbstractHorse)EntityManager.getSummonedMount(player);
                EntityManager.storeSummonedMount(player);
                if(mounts.get(clicked).getEntityId() != null) {
                    if(mounts.get(clicked).getEntityId().equals(h.getEntityId())); {event.setCancelled(true);player.closeInventory();return;}
                }
            }

            //If shift click, release current mount. Should spawn it outside
            if(event.isRightClick() && event.isShiftClick()) {
                AbstractHorse h = EntityManager.spawnHorse(mounts.get(clicked),(Player)event.getWhoClicked());
                EntityManager.removeMount(player);
                event.setCancelled(true);
                return;
            }

            EntityManager.spawnHorse(mounts.get(clicked),(Player)event.getWhoClicked());
            
            if(mounts.get(clicked).getHorseData().get("name") == null) {
                SimpleMounts.sendPlayerMessage( "Summoned horse", (Player)event.getWhoClicked());
                player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP,1.0f,1.0f);
            } else {
                SimpleMounts.sendPlayerMessage( "Summoned " + mounts.get(clicked).getHorseData().get("name"), (Player)event.getWhoClicked());

            }

            event.setCancelled(true);
        } catch (IOException e) {
            event.setCancelled(true);
            SimpleMounts.sendSystemError(e.getMessage(),(Player)event.getWhoClicked(),e);
            throw new RuntimeException(e);
        }

        player.closeInventory();
    }
}
