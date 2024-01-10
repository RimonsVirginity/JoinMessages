package me.rimon.joinmessages;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {
    private final JoinMessages plugin;
    private final LuckPerms luckPerms;

    public PlayerEventListener(JoinMessages plugin) {
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joiningPlayer = event.getPlayer();
        if (shouldBroadcast(joiningPlayer, true)) {
            String joinMessage = ChatColor.DARK_GRAY + "["+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+"] " + ChatColor.YELLOW + joiningPlayer.getName() + ChatColor.WHITE + " has joined the server.";
            broadcastMessage(joinMessage);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player leavingPlayer = event.getPlayer();
        if (shouldBroadcast(leavingPlayer, false)) {
            String leaveMessage = ChatColor.DARK_GRAY + "["+ChatColor.RED+"-"+ChatColor.DARK_GRAY+"] " + ChatColor.YELLOW + leavingPlayer.getName() + ChatColor.WHITE + " has left the server."; // Customize this message
            broadcastMessage(leaveMessage);
        }
    }

    private boolean shouldBroadcast(Player player, boolean isJoining) {
        boolean isStaffJoinMessageEnabled = plugin.getConfig().getBoolean("staff." + player.getUniqueId() + ".enabled", true);
        return !(isStaffMember(player) && !isStaffJoinMessageEnabled && isJoining);
    }

    private void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getConfig().getBoolean(player.getUniqueId() + ".enabled", true)) {
                player.sendMessage(message);
            }
        }
    }

    private boolean isStaffMember(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getNodes(NodeType.INHERITANCE).stream()
                    .map(InheritanceNode.class::cast)
                    .anyMatch(node -> node.getGroupName().equalsIgnoreCase("trainee"));
        }
        return false;
    }
}
