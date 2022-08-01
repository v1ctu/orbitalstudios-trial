package me.v1ctu.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.v1ctu.user.User;
import me.v1ctu.user.UserManager;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RequiredArgsConstructor
public class BalGiveSubCommand {

    private final UserManager userManager;

    @Command(
        name = "bal.give",
        aliases = {"balance.give"},
        target = CommandTarget.PLAYER
    )
    public void handle(Context<Player> context, Player target, double amount) {
        User sender = userManager.getById(context.getSender().getUniqueId());

        if (amount <= 0) {
            context.getSender().sendMessage("§cAmount must be positive");
            return;
        }

        if (sender.getBalance() < amount) {
            context.getSender().sendMessage("§cYou don't have enough balance");
            return;
        }

        supplyAsync(() -> userManager.getById(target.getUniqueId())).whenComplete((user, throwable) -> {
            if (user == null) {
                context.getSender().sendMessage("§cUser not found");
                return;
            }

            runAsync(() -> {
                userManager.depositById(target.getUniqueId(), amount);
                userManager.withdrawById(context.getSender().getUniqueId(), amount);

                context.sendMessage("§aSuccessfully gave §7USD " + amount + " §ato user §7" + user.getName());
                context.sendMessage("§aYou have now §7USD" + sender.getBalance() + "§a.");

                if (target.isOnline()) {
                    target.sendMessage("§aYou received §7USD " + amount + " §afrom user §7" + sender.getName());
                    target.sendMessage("§aYour balance is now §7USD " + user.getBalance());
                }
            });
        });
    }
}
