package jamie.tpa.darksmp;

import jamie.tpa.darksmp.TPARequest.Type;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player target = (Player) sender;

        if (args.length != 1) {
            target.sendMessage(ChatColor.RED + "Usage: /tpaccept <player>");
            return true;
        }

        Player requester = Bukkit.getPlayerExact(args[0]);

        if (requester == null || !requester.isOnline()) {
            target.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        // Check if request exists and matches sender
        TPARequest request = TPAManager.getRequest(target);

        if (request == null || !request.getSender().equals(requester.getUniqueId())) {
            target.sendMessage(ChatColor.RED + "No valid request from that player.");
            return true;
        }

        Location targetLoc = target.getLocation();
        Location requesterLoc = requester.getLocation();

        // Effects before teleport
        playTeleportEffect(requester);
        playTeleportEffect(target);

        switch (request.getType()) {
            case TPA:
                requester.teleport(targetLoc);
                requester.sendMessage(ChatColor.GREEN + "Teleporting to " + target.getName() + "...");
                target.sendMessage(ChatColor.AQUA + requester.getName() + " is teleporting to you.");
                break;

            case TPAHERE:
                target.teleport(requesterLoc);
                target.sendMessage(ChatColor.GREEN + "Teleporting to " + requester.getName() + "...");
                requester.sendMessage(ChatColor.AQUA + target.getName() + " is teleporting to you.");
                break;

            case TPASWAP:
                requester.teleport(targetLoc);
                target.teleport(requesterLoc);
                requester.sendMessage(ChatColor.LIGHT_PURPLE + "Swapped positions with " + target.getName() + ".");
                target.sendMessage(ChatColor.LIGHT_PURPLE + "Swapped positions with " + requester.getName() + ".");
                break;
        }

        TPAManager.clearRequest(target);
        return true;
    }

    private void playTeleportEffect(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        world.spawnParticle(Particle.PORTAL, loc, 80, 1, 1, 1, 0.05);
        world.spawnParticle(Particle.END_ROD, loc, 40, 0.5, 1, 0.5, 0.03);
    }
}
