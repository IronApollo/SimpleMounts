package simplemounts.simplemounts.Mounts.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import simplemounts.simplemounts.Mounts.GUI.MountsPage;
import simplemounts.simplemounts.Mounts.Recipes.WhistleRecipe;
import simplemounts.simplemounts.SimpleMounts;

public class SummonHandler implements Listener {

    public SummonHandler(SimpleMounts plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handles summoning mounts
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(item == null) return;
        if(!item.equals(WhistleRecipe.getWhistle())) return;

        Player player = event.getPlayer();
        if(player.hasCooldown(Material.GOAT_HORN)) return;

        //Open GUI
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_4,2.5f,2.5f);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new MountsPage(player,player);

    }

}
