package simplemounts.simplemounts.Mounts.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Managers.EntityManager;

public class EntityInteractHandler implements Listener {

    public EntityInteractHandler(SimpleMounts plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof AbstractHorse)) return;

        Player player = (Player) event.getPlayer();

        AbstractHorse h1 = (AbstractHorse) event.getRightClicked();

        Player owningPlayer = EntityManager.getOwningPlayer(h1);
        if (owningPlayer == null) return; //If is not a currently summoned mount, then its wild and player can ride

        AbstractHorse h2 = (AbstractHorse) EntityManager.getSummonedMount(player);
        if (h2 == null) {
            SimpleMounts.sendUserError("This is not your mount.", player);
            event.setCancelled(true);
            return;
        }

        if(h1.getEntityId() != (h2.getEntityId())) {SimpleMounts.sendUserError("This is not your mount",player); event.setCancelled(true);return;}


        return;
    }
}
