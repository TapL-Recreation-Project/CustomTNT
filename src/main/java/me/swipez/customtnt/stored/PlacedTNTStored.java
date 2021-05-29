package me.swipez.customtnt.stored;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedTNTStored {

    public static HashMap<TNTPrimed, String> tntTypes = new HashMap<>();

    public static List<ItemStack> diamondItems = new ArrayList<>();
    public static List<ItemStack> netheriteItem = new ArrayList<>();

    static {
        diamondItems.add(new ItemStack(Material.DIAMOND, 32));
        diamondItems.add(new ItemStack(Material.DIAMOND_BLOCK, 1));
        diamondItems.add(new ItemStack(Material.DIAMOND_HELMET));
        diamondItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        diamondItems.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        diamondItems.add(new ItemStack(Material.DIAMOND_BOOTS));
        diamondItems.add(new ItemStack(Material.DIAMOND_PICKAXE));
        diamondItems.add(new ItemStack(Material.DIAMOND_SWORD));
        diamondItems.add(new ItemStack(Material.DIAMOND_HOE));
        diamondItems.add(new ItemStack(Material.DIAMOND_AXE));
        diamondItems.add(new ItemStack(Material.DIAMOND_SHOVEL));

        netheriteItem.add(new ItemStack(Material.NETHERITE_INGOT, 32));
        netheriteItem.add(new ItemStack(Material.NETHERITE_BLOCK, 1));
        netheriteItem.add(new ItemStack(Material.NETHERITE_HELMET));
        netheriteItem.add(new ItemStack(Material.NETHERITE_CHESTPLATE));
        netheriteItem.add(new ItemStack(Material.NETHERITE_LEGGINGS));
        netheriteItem.add(new ItemStack(Material.NETHERITE_BOOTS));
        netheriteItem.add(new ItemStack(Material.NETHERITE_PICKAXE));
        netheriteItem.add(new ItemStack(Material.NETHERITE_SWORD));
        netheriteItem.add(new ItemStack(Material.NETHERITE_HOE));
        netheriteItem.add(new ItemStack(Material.NETHERITE_AXE));
        netheriteItem.add(new ItemStack(Material.NETHERITE_SHOVEL));
    }

}
