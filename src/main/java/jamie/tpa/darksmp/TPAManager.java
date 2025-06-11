package jamie.tpa.darksmp;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TPAManager {

    private static final Map<UUID, TPARequest> activeRequests = new HashMap<>();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    private static int requestTimeoutSeconds;
    private static int cooldownSeconds;

    public static void reloadConfigValues(FileConfiguration config) {
        cooldownSeconds = config.getInt("cooldown-seconds", 60);
        requestTimeoutSeconds = config.getInt("request-timeout-seconds", 60);
    }

    public static boolean isOnCooldown(Player sender) {
        long now = System.currentTimeMillis();
        return cooldowns.getOrDefault(sender.getUniqueId(), 0L) > now;
    }

    public static long getCooldownRemaining(Player sender) {
        long now = System.currentTimeMillis();
        long expires = cooldowns.getOrDefault(sender.getUniqueId(), 0L);
        return Math.max(0, (expires - now) / 1000);
    }

    public static void applyCooldown(Player sender) {
        cooldowns.put(sender.getUniqueId(), System.currentTimeMillis() + (cooldownSeconds * 1000L));
    }

    public static boolean hasPendingRequest(Player target) {
        return activeRequests.containsKey(target.getUniqueId())
               && !isExpired(target.getUniqueId());
    }

    public static boolean isExpired(UUID target) {
        TPARequest req = activeRequests.get(target);
        if (req == null) return true;

        long now = System.currentTimeMillis();
        return (now - req.getTimestamp()) > (requestTimeoutSeconds * 1000L);
    }

    public static void sendRequest(Player sender, Player target, TPARequest.Type type) {
        activeRequests.put(target.getUniqueId(), new TPARequest(sender.getUniqueId(), type));
        applyCooldown(sender);
    }

    public static TPARequest getRequest(Player target) {
        if (!hasPendingRequest(target)) {
            activeRequests.remove(target.getUniqueId());
            return null;
        }
        return activeRequests.get(target.getUniqueId());
    }

    public static Player getRequester(Player target) {
        TPARequest request = getRequest(target);
        if (request == null) return null;
        return Bukkit.getPlayer(request.getSender());
    }

    public static void clearRequest(Player target) {
        activeRequests.remove(target.getUniqueId());
    }

    public static TPARequest.Type getRequestType(Player target) {
        TPARequest req = getRequest(target);
        return (req != null) ? req.getType() : null;
    }
}
