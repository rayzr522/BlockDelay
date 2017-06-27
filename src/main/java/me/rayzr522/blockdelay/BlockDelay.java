package me.rayzr522.blockdelay;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Rayzr
 */
public class BlockDelay extends JavaPlugin implements Listener {
    private boolean enabled = false;
    // 5 seconds
    private long delay = 100l;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("BlockDelay.toggle")) {
            sender.sendMessage(ChatColor.RED + "You need the permission 'BlockDelay.toggle' to do that!");
            return true;
        }

        enabled = !enabled;
        sender.sendMessage(ChatColor.GOLD + "Block delay is now " + ChatColor.RED + (enabled ? "enabled" : "disabled"));
        return true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (enabled) {
            e.setCancelled(true);
            final BlockState state = e.getBlockPlaced().getState();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.isCancelled()) {
                        state.update(true);
                    }
                }
            }.runTaskLater(this, delay);
        }
    }
}
