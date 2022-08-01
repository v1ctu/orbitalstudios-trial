package me.v1ctu;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.v1ctu.command.BalCommand;
import me.v1ctu.command.subcommand.BalEarnSubCommand;
import me.v1ctu.command.subcommand.BalGiveSubCommand;
import me.v1ctu.command.subcommand.BalSetSubCommand;
import me.v1ctu.listener.TrafficListener;
import me.v1ctu.user.UserCache;
import me.v1ctu.user.UserManager;
import me.v1ctu.user.UserRepository;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class EconPlugin extends JavaPlugin {

    private EconPlugin instance;
    private HikariDataSource hikariDataSource;
    private UserManager userManager;

    @Override
    public void onEnable() {
        instance = this;
        createHikariPool();
        createManager();
        registerListeners();
        registerCommands();
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        hikariDataSource.close();
        getLogger().info("Disabled");
    }

    private void createHikariPool() {
        final HikariConfig hikariConfig = new HikariConfig("/hikari.properties");
        hikariDataSource = new HikariDataSource(hikariConfig);

        try (final Connection connection = hikariDataSource.getConnection()) {
            if (!connection.isValid(7)) {
                getLogger().severe("Couldn't connect to the database, shutting down...");
                getServer().shutdown();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createManager() {
        final UserRepository userRepository = new UserRepository(hikariDataSource);
        final UserCache userCache = new UserCache();
        userManager = new UserManager(userCache, userRepository);
    }

    private void registerListeners() {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new TrafficListener(userManager), this);
    }

    private void registerCommands() {
        final BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
            new BalCommand(userManager),
            new BalEarnSubCommand(userManager, this),
            new BalGiveSubCommand(userManager),
            new BalSetSubCommand(userManager)
        );
    }


}
