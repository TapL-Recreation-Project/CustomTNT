package me.swipez.customtnt.listeners;

import me.swipez.customtnt.stored.PlacedTNTStored;
import me.swipez.customtnt.tnt.TntManager;
import me.swipez.customtnt.utils.ExplosionManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TntPlaceListener implements Listener {

    Random random = new Random();

    @EventHandler
    public void onPlayerPlaceTNT(BlockPlaceEvent event){
        if (!event.getItemInHand().getType().equals(Material.TNT)){
            return;
        }
        ItemStack itemInHand = event.getItemInHand();
        if (itemInHand.getItemMeta() == null){
            return;
        }
        if (itemInHand.getEnchantments().size() > 0){
            event.getBlock().setType(Material.AIR);
            TNTPrimed primed = (TNTPrimed) event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);

            primed.setCustomNameVisible(true);
            primed.setCustomName(itemInHand.getItemMeta().getDisplayName());
            primed.setCustomNameVisible(true);
            primed.setGlowing(true);

            primed.setFuseTicks(80);

            if (itemInHand.isSimilar(TntManager.PICKAXE_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "pickaxe");
            }
            if (itemInHand.isSimilar(TntManager.DIAMOND_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "diamond");
            }
            if (itemInHand.isSimilar(TntManager.ENCHANTING_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "enchanting");
            }
            if (itemInHand.isSimilar(TntManager.TNT_STACKED)){
                PlacedTNTStored.tntTypes.put(primed, "stacked");
            }
            if (itemInHand.isSimilar(TntManager.POTION_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "potion");
            }
            if (itemInHand.isSimilar(TntManager.PUPPY_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "puppy");
            }
            if (itemInHand.isSimilar(TntManager.NETHERITE_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "netherite");
            }
            if (itemInHand.isSimilar(TntManager.GOLDEN_TNT)){
                PlacedTNTStored.tntTypes.put(primed, "golden");
            }

        }
    }
    @EventHandler
    public void onEntityBlownUp(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof TNTPrimed){
            TNTPrimed tntPrimed = (TNTPrimed) event.getDamager();
            if (PlacedTNTStored.tntTypes.containsKey(tntPrimed)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        if (!event.getEntity().getType().equals(EntityType.PRIMED_TNT)){
            return;
        }
        TNTPrimed primed = (TNTPrimed) event.getEntity();
        if (PlacedTNTStored.tntTypes.containsKey(primed)){
            String type = PlacedTNTStored.tntTypes.get(primed);
            Player player = null;
            double closestDistance = 10000;
            for (Player allPlayers : Bukkit.getOnlinePlayers()){
                double checkedDistance = allPlayers.getLocation().distance(primed.getLocation());
                if (checkedDistance < closestDistance){
                    closestDistance = checkedDistance;
                    player = allPlayers;
                }
            }
            if (type.equals("pickaxe")){
                ExplosionManager.animateExplosionOptimized(20, primed.getLocation(), random, player);
            }
            if (type.equals("puppy")){
                ExplosionManager.animateExplosionOptimized(4, primed.getLocation(), random, player);
                int randomPuppy = random.nextInt(10)+20;
                for (int i = 0; i < randomPuppy; i++){
                    int randomX = random.nextInt(4);
                    int randomZ = random.nextInt(4);
                    if (random.nextBoolean()){
                        randomX *= -1;
                    }
                    if (random.nextBoolean()){
                        randomZ *= -1;
                    }

                    Wolf wolf = (Wolf) primed.getWorld().spawnEntity(primed.getLocation().add(randomX+random.nextDouble(),0,randomZ+random.nextDouble()), EntityType.WOLF);
                    wolf.setBaby();
                }
            }
            if (type.equals("diamond")){
                ExplosionManager.animateExplosionOptimized(4, primed.getLocation(), random, player);
                int randomPuppy = random.nextInt(7)+5;
                for (int i = 0; i < randomPuppy; i++){
                    int randomX = random.nextInt(4);
                    int randomZ = random.nextInt(4);
                    if (random.nextBoolean()){
                        randomX *= -1;
                    }
                    if (random.nextBoolean()){
                        randomZ *= -1;
                    }

                    primed.getWorld().dropItemNaturally(primed.getLocation().add(randomX, 0, randomZ), PlacedTNTStored.diamondItems.get(random.nextInt(PlacedTNTStored.diamondItems.size())));
                }
            }
            if (type.equals("golden")){
                ExplosionManager.animateExplosionOptimized(5, primed.getLocation(), random, player);
                for (Entity entity : primed.getNearbyEntities(5,5,5)){
                    if (entity instanceof Player){
                        Player realPlayer = (Player) entity;
                        realPlayer.playSound(realPlayer.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                        int amplifier = random.nextInt(80)+20;
                        realPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, amplifier, false, false));
                    }
                }
            }
            if (type.equals("netherite")){
                ExplosionManager.animateExplosionOptimized(5, primed.getLocation(), random, player);
                int randomPuppy = random.nextInt(10)+7;
                for (int i = 0; i < randomPuppy; i++){
                    int randomX = random.nextInt(4);
                    int randomZ = random.nextInt(4);
                    if (random.nextBoolean()){
                        randomX *= -1;
                    }
                    if (random.nextBoolean()){
                        randomZ *= -1;
                    }
                    ItemStack itemStack = PlacedTNTStored.netheriteItem.get(random.nextInt(PlacedTNTStored.netheriteItem.size()));
                    if (itemStack != null){
                        if (EnchantmentTarget.TOOL.includes(itemStack) || EnchantmentTarget.ARMOR.includes(itemStack) || EnchantmentTarget.WEAPON.includes(itemStack) || EnchantmentTarget.BOW.includes(itemStack) || EnchantmentTarget.CROSSBOW.includes(itemStack) || EnchantmentTarget.TRIDENT.includes(itemStack) || EnchantmentTarget.FISHING_ROD.includes(itemStack)) {
                            ItemMeta meta = itemStack.getItemMeta();
                            int randomint = 300;
                            Enchantment randEnchant;
                            for (int k = 0; k < 3; k++) {
                                if (randomint != 0) {
                                    randEnchant = Enchantment.values()[(int) (Math.random() * (Enchantment.values()).length)];
                                    if (randEnchant.canEnchantItem(itemStack)) {
                                        randomint--;
                                        if (meta.hasEnchant(randEnchant)) {
                                            meta.addEnchant(randEnchant, meta.getEnchantLevel(randEnchant) + 1, false);
                                        } else {
                                            meta.addEnchant(randEnchant, 1, true);
                                        }
                                    } else {
                                        k = 0;
                                    }
                                }
                            }
                            itemStack.setItemMeta(meta);
                        }
                    }
                    primed.getWorld().dropItemNaturally(primed.getLocation().add(randomX, 0, randomZ), itemStack);
                }
            }
            if (type.equals("stacked")){
                ExplosionManager.animateExplosionOptimized(50, primed.getLocation(), random, player);

            }
            if (type.equals("potion")){
                ExplosionManager.animateExplosionOptimized(6, primed.getLocation(), random, player);
                List<PotionEffectType> potionEffectTypes = Arrays.asList(PotionEffectType.values());
                for (int i = 0; i < 4; i++){
                    int randomX = random.nextInt(4);
                    int randomZ = random.nextInt(4);
                    if (random.nextBoolean()){
                        randomX *= -1;
                    }
                    if (random.nextBoolean()){
                        randomZ *= -1;
                    }
                    int seconds = random.nextInt(5)+5;
                    AreaEffectCloud areaEffectCloud = (AreaEffectCloud) primed.getWorld().spawnEntity(primed.getLocation().add(randomX, 0, randomZ), EntityType.AREA_EFFECT_CLOUD);
                    areaEffectCloud.setColor(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    areaEffectCloud.setDuration(seconds*20);
                    areaEffectCloud.setRadius(2);
                    areaEffectCloud.addCustomEffect(new PotionEffect(potionEffectTypes.get(random.nextInt(potionEffectTypes.size())), seconds*20, 2, false, true), true);
                }
            }
            if (type.equals("enchanting")){
                ExplosionManager.animateExplosionOptimized(4, primed.getLocation(), random, player);
                for (Entity entity : primed.getNearbyEntities(5,5,5)){
                    if (entity instanceof Player){
                        Player realPlayer = (Player) entity;
                        realPlayer.playSound(realPlayer.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                        for (int i = 0; i < realPlayer.getInventory().getSize(); i++){
                            ItemStack itemStack = realPlayer.getInventory().getItem(i);
                            if (itemStack != null){
                                if (EnchantmentTarget.TOOL.includes(itemStack) || EnchantmentTarget.ARMOR.includes(itemStack) || EnchantmentTarget.WEAPON.includes(itemStack) || EnchantmentTarget.BOW.includes(itemStack) || EnchantmentTarget.CROSSBOW.includes(itemStack) || EnchantmentTarget.TRIDENT.includes(itemStack) || EnchantmentTarget.FISHING_ROD.includes(itemStack)) {
                                    ItemMeta meta = itemStack.getItemMeta();
                                    int randomint = 20;
                                    Enchantment randEnchant;
                                    for (int k = 0; k < 3; k++) {
                                        if (randomint != 0) {
                                            randEnchant = Enchantment.values()[(int) (Math.random() * (Enchantment.values()).length)];
                                            if (randEnchant.canEnchantItem(itemStack) && !randEnchant.getKey().toString().toLowerCase().contains("curse")) {
                                                randomint--;
                                                if (meta.hasEnchant(randEnchant)) {
                                                    meta.addEnchant(randEnchant, meta.getEnchantLevel(randEnchant) + 1, true);
                                                } else {
                                                    meta.addEnchant(randEnchant, 1, true);
                                                }
                                            } else {
                                                k = 0;
                                            }
                                        }
                                    }
                                    itemStack.setItemMeta(meta);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
