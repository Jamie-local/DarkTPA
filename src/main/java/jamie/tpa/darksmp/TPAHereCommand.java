package jamie.tpa.darksmp;

import jamie.tpa.darksmp.TPARequest.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPAHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /tpahere <player>");
            return true;
        }

        if (TPAManager.isOnCooldown(player)) {
            long remaining = TPAManager.getCooldownRemaining(player);
            player.sendMessage(ChatColor.RED + "You must wait " + remaining + "s before sending another request.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot send a request to yourself.");
            return true;
        }

        if (TPAManager.hasPendingRequest(target)) {
            player.sendMessage(ChatColor.RED + "That player already has a pending request.");
            return true;
        }

        TPAManager.sendRequest(player, target, Type.TPAHERE);

        player.sendMessage(ChatColor.GREEN + "Teleport-here request sent to " + target.getName());
        target.sendMessage(ChatColor.AQUA + player.getName() + " has requested you teleport to them.");
        target.sendMessage(ChatColor.YELLOW + "Type /tpaccept " + player.getName() + " to accept or /tpdeny " + player.getName() + " to deny.");

        return true;
    }
}
