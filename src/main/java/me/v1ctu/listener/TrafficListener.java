package me.v1ctu.listener;

import lombok.RequiredArgsConstructor;
import me.v1ctu.user.User;
import me.v1ctu.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static java.util.concurrent.CompletableFuture.runAsync;

@RequiredArgsConstructor
public class TrafficListener implements Listener {

    private final UserManager userManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        runAsync(() -> userManager.handleLogin(player.getUniqueId(), player.getName()));
    }

    public void onQuit(PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final User user = userManager.getById(uuid);
        runAsync(() -> userManager.handleQuit(uuid, user));
    }
}
