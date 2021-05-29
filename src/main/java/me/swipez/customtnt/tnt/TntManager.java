package me.swipez.customtnt.tnt;

import me.swipez.customtnt.CustomTNT;
import me.swipez.customtnt.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class TntManager {

    public static ItemStack PICKAXE_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.DARK_RED+"Pickaxe TNT")
            .lore(ChatColor.GRAY+"Destroys everything in a 20 block radius!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack DIAMOND_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.AQUA+"Diamond TNT")
            .lore(ChatColor.GRAY+"Scatters diamonds all around!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack ENCHANTING_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.LIGHT_PURPLE+"Enchanting TNT")
            .lore(ChatColor.GRAY+"This TNT will enchant your inventory!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack TNT_STACKED = ItemBuilder.of(Material.TNT)
            .name(ChatColor.RED+"TNTNTNTNTNT")
            .lore(ChatColor.GRAY+"Explodes in a 50 block radius!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack POTION_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.DARK_GREEN+"Potion TNT")
            .lore(ChatColor.GRAY+"Scatters effects on the floor!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack PUPPY_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.GREEN+"Puppy TNT")
            .lore(ChatColor.GRAY+"Spawns 20-30 baby wolves")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack NETHERITE_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.DARK_PURPLE+"Netherite TNT")
            .lore(ChatColor.GRAY+"Scatters netherite all around!")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack GOLDEN_TNT = ItemBuilder.of(Material.TNT)
            .name(ChatColor.GOLD+"Golden TNT")
            .lore(ChatColor.GRAY+"Gives you 20-100 hearts of absorption.")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();


    public static void initRecipes(){
        registerPickaxeTntRecipe();
        registerGenericSurround(Material.SAND, Material.COAL, new ItemStack(Material.GUNPOWDER), "custom_gunpowder_recipe");
        registerGenericSurround(Material.DIAMOND, Material.TNT, DIAMOND_TNT, "diamond_tnt");
        registerGenericSurround(Material.LAPIS_LAZULI, Material.TNT, ENCHANTING_TNT, "enchanting_tnt");
        registerUltimateTntRecipe();

        //Water bottle, ignore
        ItemStack bottle = new ItemStack(Material.POTION, 1);
        ItemMeta meta = bottle.getItemMeta();
        PotionMeta pmeta = (PotionMeta) meta;
        PotionData pdata = new PotionData(PotionType.WATER);
        pmeta.setBasePotionData(pdata);
        bottle.setItemMeta(meta);

        registerSpecificSurround(bottle, Material.TNT, POTION_TNT, "potion_tnt");
        registerGenericSurround(Material.BONE, Material.TNT, PUPPY_TNT, "puppy_tnt");
        registerGenericSurround(Material.NETHERITE_INGOT, Material.TNT, NETHERITE_TNT, "netherite_tnt");
        registerGenericSurround(Material.GOLD_NUGGET, Material.TNT, GOLDEN_TNT, "golden_tnt");
    }

    private static void registerPickaxeTntRecipe(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(CustomTNT.plugin, "pickaxe_tnt"), PICKAXE_TNT)
                .shape("PPP","PTP","PPP")
                .setIngredient('P', new RecipeChoice.MaterialChoice(Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.WOODEN_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE))
                .setIngredient('T', Material.TNT);
        Bukkit.addRecipe(shapedRecipe);
    }

    private static void registerGenericSurround(Material surroundingItem, Material centerItem, ItemStack result, String key){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(CustomTNT.plugin, key), result)
                .shape("PPP","PTP","PPP")
                .setIngredient('P', surroundingItem)
                .setIngredient('T', centerItem);
        Bukkit.addRecipe(shapedRecipe);
    }

    private static void registerSpecificSurround(ItemStack surroundingItem, Material centerItem, ItemStack result, String key){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(CustomTNT.plugin, key), result)
                .shape("PPP","PTP","PPP")
                .setIngredient('P', new RecipeChoice.ExactChoice(surroundingItem))
                .setIngredient('T', centerItem);
        Bukkit.addRecipe(shapedRecipe);
    }

    private static void registerUltimateTntRecipe(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(CustomTNT.plugin, "ultimate_tnt"), TNT_STACKED)
                .shape("TTT","TTT","TTT")
                .setIngredient('T', Material.TNT);
        Bukkit.addRecipe(shapedRecipe);
    }


}
