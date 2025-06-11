package jamie.tpa.darksmp;

import org.bukkit.plugin.java.JavaPlugin;

public class TPALOADER extends JavaPlugin {

    private static TPALOADER instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        TPAManager.reloadConfigValues(getConfig());
        getCommand("tpa").setExecutor(new TPACommand());
        getCommand("tpaccept").setExecutor(new TPAcceptCommand());
        getCommand("tpdeny").setExecutor(new TPDenyCommand());
        getCommand("tpahere").setExecutor(new TPAHereCommand());
        getCommand("tpaswap").setExecutor(new TPSwapCommand());

        getCommand("darktpa").setExecutor(new DarkTPAReloadCommand(this));
        getLogger().info("DarkTPA has been enabled.");
        getLogger().info("DarkTPA | 1.1 | jamie.local For DarkSoul");
    }

    @Override
    public void onDisable() {
        getLogger().info("DarkTPA has been disabled.");
    }

    public static TPALOADER getInstance() {
        return instance;
    }
}
