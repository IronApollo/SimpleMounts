package simplemounts.simplemounts.Util.Serialization;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.json.simple.JSONObject;

public class HorseSerialization {

    protected HorseSerialization() {
    }

    /**
     * Serialize a Horse into a JSONObject.
     * @param horse The Horse to serialize
     * @return The serialized Horse
     */
    public static JSONObject serializeHorse(Horse horse) {
            JSONObject root = new JSONObject();

            root.put("age", horse.getAge());
            root.put("name",horse.getCustomName());
            root.put("health", horse.getHealth());
            if(horse.getCustomName() != null) root.put("name", horse.getCustomName());
            root.put("type", horse.getType().toString());

            root.put("color", horse.getColor().name().toString());
            if(horse.getInventory().getArmor() != null) root.put("armor", horse.getInventory().getArmor().getType().toString());
            if(horse.getInventory().getSaddle() != null) root.put("saddle", horse.getInventory().getSaddle().getType().toString());

            root.put("style", horse.getStyle().toString());
            root.put("summoned", false);

            //NMS attributes - maybe not if this works?

            root.put("speed", horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
            root.put("jump", horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue());
            root.put("max-health", horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

            return root;

    }



}
