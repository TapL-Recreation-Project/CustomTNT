package me.swipez.customtnt.command;

import me.swipez.customtnt.CustomTNT;
import me.swipez.customtnt.utils.ExplosionManager;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

public class ExplosionTest implements CommandExecutor {

    Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            if (args.length == 1){
                Player player = (Player) sender;
                int radius = Integer.parseInt(args[0]);
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        List<Block> explosionBlocks = ExplosionManager.generateExplosionBlocks(player.getLocation(), radius, random, Particle.EXPLOSION_LARGE);
                        for (Block block : explosionBlocks){
                            if (generateRequiredChecks(10)){
                                block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                            }
                            else {
                                block.setType(Material.AIR);
                            }
                            block.setType(Material.AIR);
                        }
                    }
                }.runTask(CustomTNT.plugin);
            }
            else if (args.length == 2){
                Player player = (Player) sender;
                int radius = Integer.parseInt(args[0]);
                ExplosionManager.animateExplosion(radius,player.getLocation(),this.random, Particle.EXPLOSION_HUGE, 3);
            } else if (args.length == 3){
                Player player = (Player) sender;
                int radius = Integer.parseInt(args[0]);
                ExplosionManager.animateExplosionOptimized(radius, player.getLocation(), random, player);
            }
        }
        return true;
    }

    public boolean generateRequiredChecks(int checks){
        int correct = 0;
        for (int i = 0; i < checks; i++){
            if (random.nextBoolean()){
                correct++;
            }
        }
        return correct == checks;
    }
}
