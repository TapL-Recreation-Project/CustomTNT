package me.swipez.customtnt.utils;

import me.swipez.customtnt.CustomTNT;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ExplosionManager {

    public static List<Block> getBlocksInSphere(Location center, int radius) {
        List<Block> ret = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distance2 = (dx * dx) + (dy * dy) + (dz * dz);
                    if (distance2 > radius * radius) continue;
                    Block block = (center.clone().add(dx, dy, dz)).getBlock();
                    if (!block.getType().isAir() && !block.getType().equals(Material.BEDROCK) && !block.getType().equals(Material.END_PORTAL_FRAME)) {
                        ret.add(block);
                    }
                }
            }
        }
        return ret;
    }

    public static List<Block> getBlocksWithinSphericalShell(List<Block> sphere, Location center, int innerRadius, int outerRadius, Random random) {
        List<Block> ret = new ArrayList<>();
        int startingOuter = outerRadius;
        for (int dx = -outerRadius; dx <= outerRadius; dx++) {
            for (int dy = -outerRadius; dy <= outerRadius; dy++) {
                for (int dz = -outerRadius; dz <= outerRadius; dz++) {
                    double distance2 = (dx * dx) + (dy * dy) + (dz * dz);
                    boolean isWithinShell = distance2 >= innerRadius * innerRadius &&
                            distance2 <= outerRadius * outerRadius;
                    if (!isWithinShell) continue;
                    outerRadius = startingOuter - random.nextInt(2) + 1;
                    Block block = (center.clone().add(dx, dy, dz)).getBlock();
                    boolean isNearOutermostShell = (int) block.getLocation().distance(center) >= outerRadius;
                    if (isNearOutermostShell){
                        block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
                    }
                    if (!block.getType().isAir() && !block.getType().equals(Material.BEDROCK) && !block.getType().equals(Material.END_PORTAL_FRAME)) {
                        ret.add(block);
                    }
                }
            }
        }
        return ret;
    }

    public static List<Block> generateExplosionBlocksSlowest(Location center, int radius, Random random, Particle particle){
        List<Block> explosionBlocks = new ArrayList<>();
        int beginner = radius;
        for(int X = -radius; X < radius; X++) {
            for(int Y = -radius; Y < radius; Y++) {
                for(int Z = -radius; Z < radius; Z++) {
                    if(Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                        //Bukkit.broadcastMessage(beginner+"");
                        Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
                        if (generateRequiredChecks(10, random)){
                            center.getWorld().spawnParticle(particle, block.getLocation(), 1);
                        }
                        center.getWorld().playSound(block.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                        if (!block.getType().isAir()){
                            explosionBlocks.add(block);
                            if (random.nextBoolean()){
                                radius = beginner-random.nextInt(2);
                            }
                        }
                        //else {
                        //radius = beginner;
                        //}
                    }
                }
            }
        }

        return explosionBlocks;
    }

    private static void generateExplosionEffect(Location center, int radius, Random random, int loops){
        for (int i = 0; i < loops; i++){
            int randomX = random.nextInt(radius);
            int randomY = random.nextInt(radius);
            int randomZ = random.nextInt(radius);
            if (random.nextBoolean()){
                center.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, center.add(randomX, randomY, randomZ), 5);
            }
            else {
                center.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, center.subtract(randomX, randomY, randomZ), 5);
            }
        }
    }

    public static List<Block> generateExplosionBlocks(Location center, int radius, Random random, Particle particle){
        List<Block> explosionBlocks = new ArrayList<>();
        int beginner = radius;
        for(int X = -radius; X < radius; X++) {
            for(int Y = -radius; Y < radius; Y++) {
                for(int Z = -radius; Z < radius; Z++) {
                    if(Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                        Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
                        center.getWorld().playSound(block.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                        if (!block.getType().isAir()){
                            explosionBlocks.add(block);
                            if (random.nextBoolean()){
                                radius = beginner-random.nextInt(2);
                            }
                        }
                    }
                }
            }
        }

        return explosionBlocks;
    }

    public static void animateExplosionOptimized(int radius, Location center, Random random, Player player) {
        final List<Block> sphereBlocks = getBlocksInSphere(center, radius);

        // Increase radius by this amount every step
        final int RADIUS_STEP = 1;
        final long TICKS_BETWEEN_STEPS = 1L;

        for (int i = 0; i < radius - RADIUS_STEP; i+=RADIUS_STEP) {
            final int currentRadius = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<Block> shellBlocks = getBlocksWithinSphericalShell(sphereBlocks, center,
                            currentRadius, currentRadius + RADIUS_STEP, random);
                    player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                    ItemStack storedItemStack = null;
                    for (Block block : shellBlocks) {
                        // Account for irregularities
                        boolean isNearOutermostShell = (int) block.getLocation().distance(center) >= radius - RADIUS_STEP - 2;
                        if (!isNearOutermostShell) {
                            // Everything inside this will be set to air, anyway, no point
                            // triggering physics updates to them
                            block.setType(Material.AIR, false);
                        } else {
                            // Here we do, in case there's any adjacent water, etc
                            try {
                                Collection<ItemStack> dropItems = block.getDrops();
                                for (ItemStack itemStack : dropItems){
                                    if (itemStack != null){
                                        if (storedItemStack == null){
                                            storedItemStack = itemStack;
                                        }
                                        else if (storedItemStack.getType().equals(itemStack.getType())){
                                            storedItemStack.setAmount(itemStack.getAmount()+storedItemStack.getAmount());
                                            if (storedItemStack.getAmount() >= 32){
                                                block.getWorld().dropItemNaturally(block.getLocation(), storedItemStack);
                                            }
                                        }
                                        else {
                                            if (storedItemStack.getType().isAir()){
                                                storedItemStack.setType(itemStack.getType());
                                            }
                                            block.getWorld().dropItemNaturally(block.getLocation(), storedItemStack);
                                            storedItemStack = itemStack;
                                        }
                                    }
                                }
                                block.setType(Material.AIR, true);
                            }catch (IllegalArgumentException exception){
                                //ignore
                            }


                        }
                    }
                }
            }.runTaskLater(CustomTNT.plugin, i * TICKS_BETWEEN_STEPS);
        }
    }

    public static void animateExplosion(int radius, Location center, Random random, Particle particle, int dropCheckCount){
        int starting = radius;
        int loops = 0;
        while (starting > 2){
            starting -= 2;
            loops++;
        }
        for (int i = 0; i < loops; i++){
            int amount = i+1;
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {

                    List<Block> explosionBlocks = generateExplosionBlocksSlowest(center, amount*2, random, particle);;
                    for (Block block : explosionBlocks){
                        if (generateRequiredChecks(dropCheckCount, random)){
                            block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                        }
                        else {
                            block.setType(Material.AIR);
                        }
                        block.setType(Material.AIR);
                    }
                }
            }.runTaskLater(CustomTNT.plugin, i * 10L);
        }
    }

    public static boolean generateRequiredChecks(int checks, Random random){
        int correct = 0;
        for (int i = 0; i < checks; i++){
            if (random.nextBoolean()){
                correct++;
            }
        }
        return correct == checks;
    }
}
