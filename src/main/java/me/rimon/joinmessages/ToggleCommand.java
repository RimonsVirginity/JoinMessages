package me.rimon.joinmessages;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {
    private final JoinMessages plugin;
    private final LuckPerms luckPerms;

    public ToggleCommand(JoinMessages plugin) {
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        boolean isStaff = isPlayerInGroup(player, "trainee");

        // Toggle setting based on player type
        String configPath = (isStaff ? "staff." : "") + player.getUniqueId() + ".enabled";
        boolean isEnabled = plugin.getConfig().getBoolean(configPath, true);
        plugin.getConfig().set(configPath, !isEnabled);
        plugin.saveConfig();

        player.sendMessage("Your join messages are now " + (isEnabled ? "disabled" : "enabled"));
        return true;
    }

    private boolean isPlayerInGroup(Player player, String groupName) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getNodes(NodeType.INHERITANCE).stream()
                    .map(InheritanceNode.class::cast)
                    .anyMatch(node -> node.getGroupName().equalsIgnoreCase(groupName));
        }
        return false;
    }
}