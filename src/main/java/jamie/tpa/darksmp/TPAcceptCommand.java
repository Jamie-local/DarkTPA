package jamie.tpa.darksmp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class TPAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player target = (Player) sender;
        Player requester;

        TPARequest request = TPAManager.getRequest(target);

        if (request == null) {
            target.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return true;
        }

        if (args.length == 1) {
            Player specified = Bukkit.getPlayerExact(args[0]);
            if (specified == null || !specified.isOnline()) {
                target.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }

            if (!request.getSender().equals(specified.getUniqueId())) {
                target.sendMessage(ChatColor.RED + "That player didn't send you a request.");
                return true;
            }

            requester = specified;
        } else {
            requester = Bukkit.getPlayer(request.getSender());
            if (requester == null || !requester.isOnline()) {
                target.sendMessage(ChatColor.RED + "The player who requested is no longer online.");
                TPAManager.clearRequest(target);
                return true;
            }
        }

        TPAManager.clearRequest(target);

        Location requesterLocation = requester.getLocation();
        Location targetLocation = target.getLocation();

        switch (request.getType()) {
            case TPA:
                spawnParticles(requester);
                requester.sendMessage(ChatColor.GREEN + "Teleporting...");
                requester.teleport(target);
                break;

            case TPAHERE:
                spawnParticles(target);
                target.sendMessage(ChatColor.GREEN + "Teleporting...");
                target.teleport(requester);
                break;

            case TPASWAP:
                spawnParticles(target);
                spawnParticles(requester);
                target.sendMessage(ChatColor.GREEN + "Swapping locations...");
                requester.sendMessage(ChatColor.GREEN + "Swapping locations...");

                target.teleport(requesterLocation);
                requester.teleport(targetLocation);
                break;
        }

        requester.playSound(requester.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        return true;
    }

    private void spawnParticles(Player player) {
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100, 0.5, 1, 0.5, 0.2);
    }
}
