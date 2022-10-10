package net.vinaco.bc_uhc_worldborder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("uhcconfigure") || cmd.getName().equalsIgnoreCase("wbconfigure")){
            if (args.length == 1){
                List<String> arguments = new ArrayList<>();
                arguments.add("uhcmax");
                arguments.add("uhcmin");
                arguments.add("uhcduration");
                arguments.add("uhccenter");
                arguments.add("finalesize");
                arguments.add("finalecenter");
                arguments.add("finalelocation");
                arguments.add("spreaddistance");
                arguments.add("spreadrange");
                return arguments;
            }
            return null;
        }

        return null;
    }

}
