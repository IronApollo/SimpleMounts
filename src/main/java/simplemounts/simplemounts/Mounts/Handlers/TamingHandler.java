package simplemounts.simplemounts.Mounts.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.json.simple.JSONObject;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Managers.EntityManager;

import java.io.IOException;

public class TamingHandler implements Listener {

    public TamingHandler(SimpleMounts plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onEntityTameEvent(EntityTameEvent event) {


    }
}
