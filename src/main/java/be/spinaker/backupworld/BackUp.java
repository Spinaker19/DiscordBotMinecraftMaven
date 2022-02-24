package be.spinaker.backupworld;

import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackUp {

    private MainDiscordBotMinecraftMaven main;
    private final String standardName;

    public BackUp(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        standardName = "/Backup " + main.getConfig().get("server_name");
    }

    public void make() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH'h'mm'm'ss's'");
        String name =standardName + "[" + format.format(date) + "]";

        String destinationPath = (String) main.getConfig().get("backup_path") + name;

        File dir = new File(destinationPath);

        if(dir.mkdir()) {
            System.out.println("Dir Successfully created");

            //take the three world and copy them in the backup file tha we just created;
            for(World w : Bukkit.getWorlds()) {
                File world = w.getWorldFolder();
                File newWorldFolder = new File(destinationPath + "/" + world.getName());
                if(newWorldFolder.mkdir()) {
                    try {
                        FileUtils.copyDirectory(world, newWorldFolder);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            removeOldBackup();
            Bukkit.broadcastMessage(ChatColor.GREEN + "Backup succesfuly made !");
        }
        else Bukkit.broadcastMessage(ChatColor.RED + "Unable to make a backup");
    }

    private void removeOldBackup() {
        try {
            File backupDir = new File(main.getConfig().getString("backup_path"));
            System.out.println(backupDir.listFiles().length);
            if(backupDir.listFiles().length > main.getConfig().getInt("max_backups")) {
                System.out.println("Too many backups");
                File rmBackup = getOldestBackup(backupDir);
                FileUtils.deleteDirectory(rmBackup);
            }
        } catch(NullPointerException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private File getOldestBackup(File backupDir) throws ParseException {

        File[] backups = backupDir.listFiles();
        File oldestFile = null;
        Date oldestDate = null;
        int i = 0;
        for(File backup : backups) {
            System.out.println(backup.getName());
            String dateName = backup.getName().replace(standardName.substring(1), "");
            System.out.println(standardName.substring(1));
            System.out.println(dateName);
            SimpleDateFormat dateFormat = new SimpleDateFormat("'['dd-MM-yyyy HH'h'mm'm'ss's]'");
            Date date = dateFormat.parse(dateName);
            System.out.println("Got the date :" + date.toString());
            if(oldestFile != null && oldestDate.after(date)) {
                oldestFile = backup;
                oldestDate = date;
            } else if(oldestFile == null){
                oldestFile= backup;
                oldestDate = date;
            }
            i++;
            System.out.println(i);
        }
        System.out.println(oldestFile.getName());
        return oldestFile;
    }
}
