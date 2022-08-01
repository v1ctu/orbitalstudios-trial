package me.v1ctu.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.v1ctu.EconPlugin;
import me.v1ctu.user.User;
import me.v1ctu.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
public class BalEarnSubCommand {

    private final UserManager userManager;
    private final EconPlugin econPlugin;
    private final Map<UUID, Integer> cooldowns = new HashMap<>();

    @Command(
        name = "bal.earn",
        aliases = {"balance.earn"},
        target = CommandTarget.PLAYER
    )
    public void handle(Context<Player> context) {
        User user = userManager.getById(context.getSender().getUniqueId());

        UUID uuid = user.getUuid();
        int timeLeft = getCooldown(uuid);

        if (timeLeft > 0) {
            context.getSender().sendMessage("§cYou must wait §7" + timeLeft + "§c seconds before using this command again.");
            return;
        }

        double amount = new Random().nextInt(1, 5);

        userManager.depositById(uuid, amount);
        context.getSender().sendMessage("§aYou have earned §7" + amount + " §afrom the server");
        setCooldown(uuid, 60);

        new BukkitRunnable() {
            @Override
            public void run() {
                int timeLeft = getCooldown(uuid);
                setCooldown(uuid, --timeLeft);
                if(timeLeft == 0) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(econPlugin.getInstance(), 20, 20);
    }

    private void setCooldown(UUID uuid, int time) {
        if(time < 1) {
            cooldowns.remove(uuid);
            return;
        }
        cooldowns.put(uuid, time);
    }

    private int getCooldown(UUID uuid) {
        return cooldowns.getOrDefault(uuid, 0);
    }
}
