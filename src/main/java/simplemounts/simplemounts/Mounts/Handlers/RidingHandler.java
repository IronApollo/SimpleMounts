package simplemounts.simplemounts.Mounts.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Managers.EntityManager;

public class RidingHandler implements Listener {

    public RidingHandler(SimpleMounts plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityMountEvent(EntityMountEvent event) {

        if(!(event.getMount() instanceof AbstractHorse)) return;

        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player)event.getEntity();

        AbstractHorse h1 = (AbstractHorse)event.getMount();

        Player owningPlayer = EntityManager.getOwningPlayer(h1);
        if(owningPlayer == null) return; //If is not a currently summoned mount, then its wild and player can ride

        AbstractHorse h2 = (AbstractHorse)EntityManager.getSummonedMount(player);
        if(h2 == null) {SimpleMounts.sendUserError("This is not your mount.",player); event.setCancelled(true);return;}

        if(h1.getEntityId() != (h2.getEntityId())) {SimpleMounts.sendUserError("This is not your mount",player); event.setCancelled(true);return;}

    }

}
