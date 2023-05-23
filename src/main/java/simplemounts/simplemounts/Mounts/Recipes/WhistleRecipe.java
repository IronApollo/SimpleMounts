package simplemounts.simplemounts.Mounts.Recipes;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import simplemounts.simplemounts.SimpleMounts;

import java.util.List;

public class WhistleRecipe {

    private static ItemStack whistle;

    public WhistleRecipe() {
        whistle = new ItemStack(Material.GOAT_HORN,1);
        ItemMeta meta = whistle.getItemMeta();
        MusicInstrumentMeta musicMeta = (MusicInstrumentMeta)meta;
        musicMeta.setCustomModelData(1);
        musicMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        musicMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        musicMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        musicMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        musicMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        musicMeta.addItemFlags(ItemFlag.HIDE_DYE);
        musicMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);

        whistle.setItemMeta(musicMeta);

        ItemMeta itemMeta = whistle.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GRAY + "Horse Whistle");
        itemMeta.setLore(List.of(ChatColor.DARK_PURPLE + "With a whistle your",ChatColor.DARK_PURPLE + "trusty steed arrives"));

        whistle.setItemMeta(itemMeta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(SimpleMounts.getPlugin(),"horse-whistle"), whistle);
        recipe.shape(" S "," I "," N ");
        recipe.setIngredient('S',Material.STRING);
        recipe.setIngredient('I',Material.IRON_INGOT);
        recipe.setIngredient('N',Material.IRON_NUGGET);

        Bukkit.addRecipe(recipe);
    }

    public static ItemStack getWhistle() {
        return whistle;
    }
}
