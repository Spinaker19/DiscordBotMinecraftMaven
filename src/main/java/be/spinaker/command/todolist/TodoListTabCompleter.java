package be.spinaker.command.todolist;

import be.spinaker.configs.ConfigType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TodoListTabCompleter implements TabCompleter {

    List<String> args1 = new ArrayList<>();
    List<String> args2 = new ArrayList<>();
    FileConfiguration todoFile = ConfigType.TODO.getConfig().getFileConfiguration();

    public TodoListTabCompleter() {
        args1.add("add");
        args1.add("check");
        args1.add("recap");
        args1.add("clean");
        args1.add("removeAll");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        System.out.println(command);
        if(args.length == 1) {
            return args1;
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("check")) {
            getArgs2();
            return args2;
        }
        return null;
    }

    private void getArgs2() {
        args2.clear();
        for(String elem : todoFile.getKeys(false)) {
            if(todoFile.getInt(elem + "." + "check") == 0) {
                args2.add(elem);
            }
        }
    }

}
