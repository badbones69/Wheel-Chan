package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.Map.Entry;

public class TestCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static Message lastMessage;
    private static boolean errored = false;
    private static Map<User, Integer> mapCache = new HashMap<>();
    private static int limit = 1;
    private static int maxLoop = 666;
    
    public static void runCommand(MessageReceivedEvent e) {
        MessageChannel channel = e.getChannel();
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (wheelChan.isSensei(e.getAuthor(), e.getGuild())) {
            StringBuilder message = new StringBuilder();
            if (lastMessage == null || !errored) {
                lastMessage = e.getMessage();
                mapCache.clear();
                limit = 1;
            }
            try {
                getMap(channel);
            } catch (Exception ex) {
                errored = true;
                System.out.println("Saved to cache");
            }
            while (errored) {
                try {
                    getMap(channel);
                    errored = false;
                } catch (Exception ex) {
                    System.out.println("Saved to cache");
                }
            }
            int total = 0;
            int limit = 0;
            for (Entry<User, Integer> entry : sortByValue(mapCache).entrySet()) {
                total += entry.getValue();
                if (limit < 10) {
                    message.append(entry.getKey().getName()).append(": ").append(String.format(Locale.ENGLISH, "%,d", entry.getValue())).append("\n");
                }
                limit++;
            }
            message.append("Total: ").append(String.format(Locale.ENGLISH, "%,d", total));
            embed = new EmbedBuilder().setDescription(message);
            e.getChannel().sendMessage(embed.build()).complete();
        }
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    //561359380792999949 Krabbs
    //285275949690519554 Chong
    
    private static void getMap(MessageChannel channel) {
        for (; limit <= maxLoop; limit++) {
            List<Message> messageList = channel.getHistoryAfter(lastMessage, 100).complete().retrievePast(100).complete();
            System.out.println(limit + ": " + messageList.size());
            for (Message message : messageList) {
                if (wheelChan.isShoob(message.getAuthor()) && message.getContentDisplay().contains("No sniping allowed")) {
                    User sniper = message.getMentionedUsers().get(0);
                    mapCache.put(sniper, mapCache.getOrDefault(sniper, 0) + 1);
                }
                //mapCache.put(message.getAuthor(), mapCache.getOrDefault(message.getAuthor(), 0) + 1);
                lastMessage = message;
            }
            if (messageList.size() < 100) {
                break;
            }
        }
    }
    
}