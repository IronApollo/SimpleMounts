package simplemounts.simplemounts.Util.Managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import org.json.simple.JSONObject;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Database.Database;
import simplemounts.simplemounts.Util.Database.Mount;
import simplemounts.simplemounts.Util.Serialization.HorseSerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class for managing rideable entities
 */
public abstract class EntityManager {

    private static HashMap<Player, ArrayList<Object>> summonedMounts;   //ArrayList is defined as HorseEntity,index

    public static void init() {
        summonedMounts = new HashMap<>();
    }

    /**
     * Saves an entity to a file
     * @param entity
     * @param player
     */
    public static JSONObject createEntitySave(Entity entity, Player player) throws IOException {
        JSONObject obj = HorseSerialization.serializeHorse((Horse)entity);

        UUID id = Database.insertNewMount(player,obj);

        return obj;
    }

    /**
     * Gets a list of JSONified entities the player owns
     * @param player
     * @return
     */
    public static ArrayList<JSONObject> getEntities(Player player) throws IOException, ClassNotFoundException {
        ArrayList<JSONObject> entities = Database.getEntities(player);

        return entities;
    }

    /**
     * Converts the JSON to a living horse entity
     * @return
     */
    public static Horse spawnHorse(Mount m, Player player) {
        //Generic Entity Data
        JSONObject json = m.getHorseData();

        Location location = player.getLocation();
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location,EntityType.fromName(json.get("type").toString()));

        //Start of transaction
        try {
            ((Ageable) entity).setAge(Integer.parseInt(json.get("age").toString()));
            if(json.get("name") != null) entity.setCustomName(json.get("name").toString());


            //Horse Specific Data
            Horse horse = (Horse) entity;
            horse.setColor(Horse.Color.valueOf(json.get("color").toString()));
            horse.setStyle(Horse.Style.valueOf(json.get("style").toString()));
            if(json.get("name") != null) {horse.setCustomName(json.get("name").toString());horse.setCustomNameVisible(true);}

            horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.parseDouble(json.get("max-health").toString()));
            entity.setHealth(Double.parseDouble(json.get("health").toString()));
            horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(Double.parseDouble(json.get("jump").toString()));
            horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Double.parseDouble(json.get("speed").toString()));

            if(json.get("saddle") != null) {horse.getInventory().setSaddle(new ItemStack(Material.valueOf(json.get("saddle").toString())));}
            if(json.get("armor") != null) {horse.getInventory().setArmor(new ItemStack(Material.valueOf(json.get("armor").toString())));}

            //Special values
            horse.setPersistent(true);
            horse.setTamed(true);
            horse.setAdult();
            horse.setOwner(player);
            if(SimpleMounts.getCustomConfig().getBoolean("Basic.isImmortal")) horse.setInvulnerable(true);

            ArrayList<Object> o = new ArrayList<>();
            o.add(entity);
            o.add(m.getMountId());
            summonedMounts.put(player,o);

            EntityManager.updateSummonTag(player,horse,true);

            return horse;
        } catch (Throwable e) {
            //despawn entity
            SimpleMounts.sendSystemError("Failed to summon mount",player,e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a mount from the world that the player currently has summoned
     * @param player
     */
    public static void storeSummonedMount(Player player) throws IOException {
        if(summonedMounts.isEmpty()) {SimpleMounts.sendUserError("No mounts to store!",player);return;}
        if(!summonedMounts.containsKey(player)) {SimpleMounts.sendUserError("No mounts to store!",player);return;}

        Horse e = (Horse)summonedMounts.get(player).get(0);
        UUID uuid = UUID.fromString(summonedMounts.get(player).get(1).toString());
        EntityManager.updateSummonTag(player,e,false);
        Database.updateMount(player,uuid,"horse_data",HorseSerialization.serializeHorse(e));

        //If on lead, drop lead
        if(e.isLeashed()) player.getWorld().dropItem(e.getLocation(),new ItemStack(Material.LEAD,1));

        e.remove();
        summonedMounts.remove(player);

        SimpleMounts.sendPlayerMessage("Stored Mount",player);

    }

    /**
     * Updates the summoned tag inside the JSON file for the horse
     */
    public static void updateSummonTag(Player player, Horse horse, boolean b) {
        if(b) { //True, adds entity to file
            Database.updateMount(player,(UUID)summonedMounts.get(player).get(1),"isSummoned",1);
            Database.updateMount(player,(UUID)summonedMounts.get(player).get(1),"entity_id",horse.getUniqueId());
        } else {    //Removes tag from database
            Database.updateMount(player,(UUID)summonedMounts.get(player).get(1),"isSummoned",0);
            Database.updateMount(player,(UUID)summonedMounts.get(player).get(1),"entity_id",null);
        }

    }

    /**
     * Returns a boolean value for if the entity is summoned or not
     * @param player
     */
    public static boolean isSummoned(Player player) {
        if(!summonedMounts.containsKey(player)) return false;
        if(summonedMounts.get(player).isEmpty()) return false;
        return true;
    }

    public static Entity getSummonedMount(Player player) {
        if(summonedMounts.get(player) == null) return null;
        return (Entity)summonedMounts.get(player).get(0);
    }

    /**
     * Removes the mount file from our systems. Does not remove mount from world
     * @param player
     */
    public static void removeMount(Player player) {
        Horse h = (Horse)summonedMounts.get(player).get(0);
        UUID uuid = UUID.fromString(summonedMounts.get(player).get(1).toString());

        summonedMounts.remove(player);

        Database.removeMount(player,uuid);
    }

    /**
     * Despawns all the currenly summoned mounts on the server
     */
    public static void despawnAllMounts() {

        for(Map.Entry<Player, ArrayList<Object>> entry : summonedMounts.entrySet()) {
            Player player = entry.getKey();
            ArrayList<Object> objects = entry.getValue();
            Horse horse = (Horse)objects.get(0);
            horse.remove();
            updateSummonTag(player,horse,false);
        }
    }

    public static ArrayList<Mount> getMounts(Player player) {
        return Database.getMounts(player);
    }

    public static Player getOwningPlayer(Horse h1) {
        for(Map.Entry<Player, ArrayList<Object>> entry : summonedMounts.entrySet()) {
            Player player = entry.getKey();
            ArrayList<Object> objects = entry.getValue();
            Horse horse = (Horse)objects.get(0);

            if(horse.getEntityId() == h1.getEntityId()) return player;
        }
        return null;
    }
}
