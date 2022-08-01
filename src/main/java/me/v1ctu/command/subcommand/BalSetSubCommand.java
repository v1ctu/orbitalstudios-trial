package me.v1ctu.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.v1ctu.user.User;
import me.v1ctu.user.UserManager;
import org.bukkit.entity.Player;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RequiredArgsConstructor
public class BalSetSubCommand {

    private final UserManager userManager;

    @Command(
        name = "bal.set",
        aliases = {"balance.set"},
        permission = "bal.set",
        target = CommandTarget.PLAYER
    )
    public void handle(Context<Player> context, Player target, double amount) {
        Player sender = context.getSender();

        if (amount < 0) {
            sender.sendMessage("§cAmount must be positive");
            return;
        }

        supplyAsync(() -> userManager.getById(target.getUniqueId())).whenComplete((user, throwable) -> {
            if (user == null) {
                sender.sendMessage("§cUser not found");
                return;
            }

            runAsync(() -> {
                userManager.setById(user.getUuid(), amount);
            });

            sender.sendMessage("§aSuccessfully set balance of user §7" + user.getName() + " to §7USD " + amount);
            if (target.isOnline()) {
                target.sendMessage("§aYou have been set balance of §7USD " + amount);}
        });

    }
}
