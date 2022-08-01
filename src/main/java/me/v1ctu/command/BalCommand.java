package me.v1ctu.command;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.v1ctu.user.UserManager;
import org.bukkit.entity.Player;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RequiredArgsConstructor
public class BalCommand {

    private final UserManager userManager;

    @Command(
        name = "bal",
        aliases = {"balance"},
        target = CommandTarget.PLAYER
    )
    public void handle(Context<Player> context, @Optional String target) {
        Player sender = context.getSender();

        if (target == null) {
            runAsync(() -> sender.sendMessage("§aYou have §7USD" + userManager.getById(sender.getUniqueId()).getBalance() + "§a."));
            return;
        }

        supplyAsync(() -> userManager.getByName(target)).whenComplete((user, throwable) -> {
            if(user == null) {
                sender.sendMessage("§cUser not found");
                return;
            }
            runAsync(() -> sender.sendMessage("§7" + user.getName() + " §ahas USD§7" + user.getBalance() + "§a."));
        });
    }
}
