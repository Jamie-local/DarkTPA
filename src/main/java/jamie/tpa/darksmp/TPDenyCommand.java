package jamie.tpa.darksmp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPDenyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player target = (Player) sender;

        if (args.length != 1) {
            target.sendMessage(ChatColor.RED + "Usage: /tpdeny <player>");
            return true;
        }

        Player requester = Bukkit.getPlayerExact(args[0]);

        if (requester == null || !requester.isOnline()) {
            target.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        TPARequest request = TPAManager.getRequest(target);

        if (request == null || !request.getSender().equals(requester.getUniqueId())) {
            target.sendMessage(ChatColor.RED + "No valid request from that player.");
            return true;
        }

        TPAManager.clearRequest(target);

        target.sendMessage(ChatColor.RED + "You denied the teleport request from " + requester.getName() + ".");
        requester.sendMessage(ChatColor.RED + target.getName() + " denied your teleport request.");

        return true;
    }
}
