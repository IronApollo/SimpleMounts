package simplemounts.simplemounts.Mounts.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import simplemounts.simplemounts.SimpleMounts;

import java.util.List;

public class WhistleRecipe {

    private static ItemStack whistle;

    public WhistleRecipe() {
        whistle = new ItemStack(Material.GOAT_HORN,1);

        ItemMeta itemMeta = whistle.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "Horse Whistle");
        itemMeta.setLore(List.of(ChatColor.DARK_PURPLE + "With a toot and a whistle",ChatColor.DARK_PURPLE + "your trusty steed arrives"));
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
