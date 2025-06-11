package jamie.tpa.darksmp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DarkTPAReloadCommand implements CommandExecutor {

    private final TPALOADER plugin;

    public DarkTPAReloadCommand(TPALOADER plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("darktpa.reload")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to reload DarkTPA.");
            return true;
        }

        plugin.reloadConfig();
        TPAManager.reloadConfigValues(plugin.getConfig());

        player.sendMessage(ChatColor.GREEN + "DarkTPA configuration reloaded.");
        return true;
    }
}
