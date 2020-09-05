package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class TestCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static Message lastMessage;
    private static Message oldestMessage;
    private static Message firstMessage;
    private static boolean errored = false;
    private static Map<User, Integer> mapCache = new HashMap<>();
    private static int limit = 1;
    private static int maxLoop = 10000;
    private static int messagedChecked = 0;
    private static int spawnCards = 0;
    
    public static void runSaveCommand(MessageReceivedEvent e, Server server) {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Map-Last-Run-Start", firstMessage.getChannel().getId() + ":" + firstMessage.getId());
        data.set("Map-Cache", convertMap());
        Files.DATA.saveFile();
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN)
        .setDescription(
        "Saved map cache\n" +
        "Total Messages Checked: **" + String.format(Locale.ENGLISH, "%,d", messagedChecked) + "**\n" +
        "Total Spawned Cards Found: **" + String.format(Locale.ENGLISH, "%,d", spawnCards) + "**\n" +
        "Map Last Run Start: **" + wheelChan.convertToTime(firstMessage.getTimeCreated().toInstant().toEpochMilli()) + "**\n" +
        "Oldest Message Found: **" + wheelChan.convertToTime(oldestMessage.getTimeCreated().toInstant().toEpochMilli(), true) + "**");
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static List<String> convertMap() {
        List<String> list = new ArrayList<>();
        mapCache.forEach((key, value) -> list.add(key.getName() + ":" + value + ":" + key.getId()));
        return list;
    }
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        MessageChannel channel = e.getChannel();
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (server.isSensei(e.getAuthor(), e.getGuild())) {
            StringBuilder message = new StringBuilder();
            if (lastMessage == null || !errored) {
                lastMessage = e.getMessage();
                firstMessage = e.getMessage();
                mapCache.clear();
                limit = 1;
                messagedChecked = 0;
                spawnCards = 0;
            }
            try {
                getMap(channel);
            } catch (Exception ex) {
                errored = true;
                System.out.println("Saved to cache");
                ex.printStackTrace();
            }
            while (errored) {
                try {
                    getMap(channel);
                    errored = false;
                } catch (Exception ex) {
                    System.out.println("Saved to cache");
                    ex.printStackTrace();
                }
            }
            oldestMessage = lastMessage;
            int limit = 0;
            for (Entry<User, Integer> entry : sortByValue(mapCache, true).entrySet()) {
                spawnCards += entry.getValue();
                if (limit < 10) {
                    message.append(entry.getKey().getName()).append(": ").append(String.format(Locale.ENGLISH, "%,d", entry.getValue())).append("\n");
                }
                limit++;
            }
            message.append("Total: ").append(String.format(Locale.ENGLISH, "%,d", spawnCards));
            embed = new EmbedBuilder().setDescription(message);
            e.getChannel().sendMessage(embed.build()).complete();
        }
    }
    
    public static Map<User, Integer> getMapCache() {
        return mapCache;
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean reverse) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        if (reverse) Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    private static void getMap(MessageChannel channel) {
        for (; limit <= maxLoop; limit++) {
            List<Message> messageList = channel.getHistoryAfter(lastMessage, 100).complete().retrievePast(100).complete();
            System.out.println(limit + ": " + messageList.size());
            for (Message message : messageList) {
                if (!message.getId().equals(lastMessage.getId())) {
                    if (wheelChan.isShoob(message.getAuthor()) && !message.getEmbeds().isEmpty()) {
                        MessageEmbed embed = message.getEmbeds().get(0);
                        if (embed.getDescription() != null && embed.getDescription().contains("got the card!")) {
                            User user = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
                            if (user != null) {
                                mapCache.put(user, mapCache.getOrDefault(user, 0) + 1);
                                //System.out.println("- Found claim from " + user.getName());
                            }
                        }
                    }
                    messagedChecked++;
                }
                //mapCache.put(message.getAuthor(), mapCache.getOrDefault(message.getAuthor(), 0) + 1);
            }
            lastMessage = messageList.get(messageList.size() - 1);
            if (messageList.size() < 100) {
                break;
            }
        }
    }
    
}