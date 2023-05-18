package simplemounts.simplemounts.Mounts.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONObject;
import simplemounts.simplemounts.SimpleMounts;
import simplemounts.simplemounts.Util.Database.Database;
import simplemounts.simplemounts.Util.Database.Mount;
import simplemounts.simplemounts.Util.GUI.GUIBuilder;
import simplemounts.simplemounts.Util.GUI.ItemManager;
import simplemounts.simplemounts.Util.Managers.EntityManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class MountsPage {
    public MountsPage(Player target, Player sender) {
        //Create a GUI loading all info for their current mounts
        Inventory GUI = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Mounts");

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullmeta = (SkullMeta)head.getItemMeta();
        skullmeta.setDisplayName(ChatColor.GOLD + "Display Name: " + ChatColor.GRAY + target.getName());
        skullmeta.setOwningPlayer(target);
        head.setItemMeta(skullmeta);
        GUI.setItem(8,head);
        ItemManager.addPermItem(head);


        //Grab the entities associated with the player
        ArrayList<Mount> mounts = Database.getMounts(target);

        ArrayList<JSONObject> jsonObs = new ArrayList<>();

        for(Mount m: mounts) {
            jsonObs.add(m.getHorseData());
        }

        //Populate the GUI
        int counter = 0;
        for(JSONObject e: jsonObs) {

            Horse.Color hc = Horse.Color.valueOf(e.get("color").toString());

            ItemStack spawnEgg;
            switch(hc) {
                case WHITE:
                    spawnEgg = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG);
                    break;
                case CREAMY:
                    spawnEgg = new ItemStack(Material.LLAMA_SPAWN_EGG);
                    break;
                case CHESTNUT:
                    spawnEgg = new ItemStack(Material.RABBIT_SPAWN_EGG);
                    break;
                case BROWN:
                    spawnEgg = new ItemStack(Material.DONKEY_SPAWN_EGG);
                    break;
                case BLACK:
                    spawnEgg = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG);
                    break;
                case GRAY:
                    spawnEgg = new ItemStack(Material.STRAY_SPAWN_EGG);
                    break;
                case DARK_BROWN:
                    spawnEgg = new ItemStack(Material.MULE_SPAWN_EGG);
                    break;
                default:
                    spawnEgg = new ItemStack(Material.HORSE_SPAWN_EGG);
                    break;
            }


            ItemMeta meta = spawnEgg.getItemMeta();

            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "------------");
            DecimalFormat df = new DecimalFormat("0.0");

            lore.add(ChatColor.RED + "Health : " + df.format(((Double.parseDouble(e.get("health").toString()))/Double.parseDouble(e.get("max-health").toString()))*100) + "%");
            lore.add(ChatColor.AQUA + "Speed : " + df.format((((Double.parseDouble(e.get("speed").toString()))-0.1125)/(0.3375 - 0.1125))*100) + "%");
            lore.add(ChatColor.GREEN + "Jump  : " + df.format((((Double.parseDouble(e.get("jump").toString()))-0.4)/(1.0 - 0.4))*100) + "%");
            lore.add(ChatColor.DARK_GRAY + "------------");
            if(mounts.get(counter).isSummoned()) {
                lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Click to Store Mount");
            } else {
                lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Click to Summon Mount");
                lore.add("");
                lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Shift + Q to release mount");
            }

            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);


            //Check for if custom name
            if(e.get("name") == null) {
                meta.setDisplayName(ChatColor.RESET + "Horse");
            } else {
                meta.setDisplayName(ChatColor.RESET + e.get("name").toString());
            }

            spawnEgg.setItemMeta(meta);

            GUI.setItem(counter,spawnEgg);
            counter++;
        }
        GUI = GUIBuilder.fillWithBackgroundItem(GUI, Material.BLACK_STAINED_GLASS_PANE);

        sender.openInventory(GUI);
    }

    /**
     * Creates a bar of squares corresponding to the weighted values of the stats
     * @param lowerBound
     * @param higherBound
     * @param value
     * @return
     */
    private String getStatString(double lowerBound, double higherBound, double value) {
        final int DIVISOR = 10;

        String shoe = new String(Character.toChars(0x1F349));
        String returnString = "";

        final double STEP = (higherBound - lowerBound) / DIVISOR;

        double calcWeightedValue = (value - lowerBound) / STEP;
        Bukkit.getLogger().info("Step =" + STEP + " WeightedValue = " + calcWeightedValue);
        for(int i = 0; i < DIVISOR; i ++) {
            returnString += shoe;
            if(i > calcWeightedValue) returnString += ChatColor.GRAY;
        }
        return returnString;
    }


}
