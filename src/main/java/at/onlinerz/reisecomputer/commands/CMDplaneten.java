package at.onlinerz.reisecomputer.commands;

import at.onlinerz.reisecomputer.frontend.MainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDplaneten implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("planeten")) {
            if(sender instanceof Player) {
                Player p = (Player)sender;
                MainGUI.createFor(p);
            }
        }
        return false;
    }
}
