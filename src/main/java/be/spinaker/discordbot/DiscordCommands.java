package be.spinaker.discordbot;

import be.spinaker.backupworld.BackUp;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Locale;

public class DiscordCommands extends ListenerAdapter implements Listener{

    private static final String prefix = "/";
    private static final String token = "OTIwNTg1NjgwMzQwNTI5MjQy.YbmgOQ.NFPvuVLm39j_u7aaaRMgw5k7iv4";
    private JDA jda;
    private TextChannel chatChannel;
    private TextChannel commandChannel;
    private MainDiscordBotMinecraftMaven main;
    private BackUp backUp;

    public DiscordCommands(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        jda = main.getJda();
        chatChannel = main.getChatChannel();
        commandChannel = main.getCommandChannel();
        backUp = main.getBackup();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        Member member = event.getMember();
        if(member == null || member.getUser().isBot()) return;

        if(event.getChannel().equals(chatChannel)) {
            Bukkit.broadcastMessage("<"+ event.getAuthor().getName() + "> " + event.getMessage().getContentDisplay());
        }
        else if(event.getChannel().equals(commandChannel)) {
            String message = event.getMessage().getContentDisplay();
            if(message.startsWith(prefix)) {
                String cmd = message.substring(1);
                String[] args = cmd.split(" ");
                switch(args[0].toLowerCase(Locale.ROOT)){
                    case("stop"):
                        if(Bukkit.getOnlinePlayers().isEmpty()) {
                            System.out.println("Server stopping");
                            commandChannel.sendMessage( "Server shuting down...").queue();
                            Bukkit.broadcastMessage(ChatColor.RED + "Server shuting down...");
                            main.getCooldown().runTaskTimer(main, 0, 20);
                        }
                        else {
                            //ChatColor.BOLD == ecrire en gras
                            Bukkit.broadcastMessage(ChatColor.BOLD + event.getAuthor().getName() + ChatColor.GOLD + " is trying to shutdown the server");
                            Bukkit.broadcastMessage(ChatColor.GOLD + "Please disconnect from the server so we can shut it down");
                            Bukkit.broadcastMessage(ChatColor.GOLD + "or beat the shit out of " + ChatColor.BOLD + event.getAuthor().getName() + ChatColor.GOLD + " if he was trying to kick you out");
                            if(Bukkit.getOnlinePlayers().size() > 1) {
                                commandChannel.sendMessage("Unfortuantly the following players are still connected").queue();
                                displayOnlinePlayer(commandChannel);
                                commandChannel.sendMessage("Please ask them to leave the game").queue();
                            }
                            else {
                                commandChannel.sendMessage("Unfortuantly the following player is still connected").queue();
                                displayOnlinePlayer(commandChannel);
                                commandChannel.sendMessage("Please ask him to leave the game").queue();
                            }
                        }
                        break;
                    case("cancel"):
                        if(main.isStopping()) {
                            main.getCooldown().stop();
                            main.resetCooldown();
                            Bukkit.broadcastMessage(ChatColor.GREEN + "The shuting down of the server has been canceled !");
                            commandChannel.sendMessage("The shuting down of the server has been canceled !").queue();
                        }
                        break;
                    case("help"):
                        System.out.println("Asked for help");
                        break;
                    case("state"):
                        commandChannel.sendMessage("The server is up and running !").queue();
                        break;
                    case("save"):
                        backUp.make();
                        commandChannel.sendMessage("Backup successfuly made").queue();
                        break;
                    case("stats"):
                        //integrate all kind of stat
                        //record when a plyer is killed by another ?
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player player = Bukkit.getPlayer(args[1]);
                            System.out.println(player.getStatistic(Statistic.MOB_KILLS));
                        }
                        break;
                }
            }
            else {
                event.getChannel().sendMessage("Please use '/' to run a command").queue();
                event.getChannel().sendMessage("Or use another channel if you want to discuss with your friend").queue();
                event.getChannel().sendMessage("You can use '/help' to have a full list of the possible commands").queue();
            }
        }

    }
    private void displayOnlinePlayer(TextChannel channel) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            EmbedBuilder builder = new EmbedBuilder().setAuthor(player.getDisplayName(),
                    null,
                    "https://crafatar.com/avatars/" + player.getUniqueId().toString() + "?overlay=1"
            );
            builder.setColor(Color.ORANGE);
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void sendMessage(Player player, String content, boolean contentAuthorLine, Color color) {
        if(chatChannel == null) return;

        String message;
        if(content.startsWith("§")) {
            message = content.substring(2);
        }
        else {
           message = content;
        }
        //Get the player head to send the message
        EmbedBuilder builder = new EmbedBuilder().setAuthor(contentAuthorLine ? message : player.getDisplayName(),
                null,
                "https://crafatar.com/avatars/" + player.getUniqueId().toString() + "?overlay=1"
        );
        if(!contentAuthorLine){
            builder.setDescription(content);
        }
        builder.setColor(color);

        MessageEmbed messageEmbed = builder.build();
        chatChannel.sendMessageEmbeds(messageEmbed).queue();
    }
    public void sendMessage(String content) {
        chatChannel.sendMessage(content).queue();
    }
    public void sendRespondToCommand(String content) {
        commandChannel.sendMessage(content).queue();
    }

}
