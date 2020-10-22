package me.badbones69.wheelchan.api;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpai;
import me.badbones69.wheelchan.api.objects.Server;
import me.badbones69.wheelchan.listeners.CardSpawnListener;
import me.badbones69.wheelchan.listeners.CommandListener;
import me.badbones69.wheelchan.listeners.SpawnPackListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class WheelChan {
    
    private static WheelChan instance = new WheelChan();
    private JDA jda;
    private List<Server> serverList = new ArrayList<>();
    private List<Senpai> senpais = new ArrayList<>();
    private boolean testing = true;
    
    public static WheelChan getInstance() {
        return instance;
    }
    
    public void load() throws InterruptedException {
        loadJDA();
        Messages.loadMessages();
        serverList.clear();
        jda.awaitReady();
        jda.getGuildCache().forEach(guild -> serverList.add(new Server(guild.getId())));
        FileConfiguration data = Files.DATA.getFile();
        if (data.contains("Senpais") && data.getStringList("Senpais").isEmpty()) {
            data.getConfigurationSection("Senpais").getKeys(false).forEach(senpaiID ->
            senpais.add(new Senpai(senpaiID, data.getLong("Senpais." + senpaiID + ".Cooldown"),
            data.getString("Senpais." + senpaiID + ".Name", "Failed to get user!"))));
        }
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public List<Server> getServerList() {
        return serverList;
    }
    
    public Server getServer(Guild guild) {
        return getServer(guild.getId());
    }
    
    public Server getServer(String guildID) {
        Server server = serverList.stream().filter(serv -> serv.getGuildID().equals(guildID)).findFirst().orElse(null);
        if (server == null) {
            server = newServer(jda.getGuildById(guildID));
        }
        return server;
    }
    
    public Server newServer(Guild guild) {
        Server server = new Server(guild.getId(), true);
        serverList.add(server);
        return server;
    }
    
    public List<Senpai> getSenpais() {
        return senpais;
    }
    
    public List<Senpai> getSenpais(Server server) {
        return senpais.stream().filter(senpai -> server.getGuild().isMember(senpai.getUser())).collect(Collectors.toList());
    }
    
    public List<String> getSenpaiNames() {
        return senpais.stream().map(senpai -> senpai.getUser().getName()).collect(Collectors.toList());
    }
    
    public List<String> getSenpaiIDs() {
        return senpais.stream().map(Senpai :: getID).collect(Collectors.toList());
    }
    
    public void clearAllSenpais() {
        senpais.clear();
        saveSenpais();
    }
    
    public void addSenpai(User senpai) {
        senpais.add(new Senpai(senpai.getId(), senpai.getName()));
        saveSenpais();
    }
    
    public void addSenpais(List<User> senpaiList) {
        boolean newSenpai = false;
        for (User senpai : senpaiList) {
            if (!isSenpai(senpai)) {
                senpais.add(new Senpai(senpai.getId(), senpai.getName()));
                newSenpai = true;
            }
        }
        if (newSenpai) {
            saveSenpais();
        }
    }
    
    public void removeSenpai(User senpai) {
        removeSenpai(senpai.getId());
    }
    
    public void removeSenpai(String senpaiID) {
        Senpai senpai = getSenpai(senpaiID);
        //Got a NPE from here so fixing with a check.
        if (senpai != null) {
            senpais.remove(senpai);
            saveSenpais();
        }
    }
    
    public boolean isSenpai(User senpai) {
        return isSenpai(senpai.getId());
    }
    
    public boolean isSenpai(String senpaiID) {
        return getSenpai(senpaiID) != null;
    }
    
    public Senpai getSenpai(User senpai) {
        return getSenpai(senpai.getId());
    }
    
    public Senpai getSenpai(String senpaiID) {
        return senpais.stream().filter(senpai -> senpai.getID().equals(senpaiID)).findFirst().orElse(null);
    }
    
    public void saveSenpais() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Senpais", null);
        for (Senpai senpai : senpais) {
            data.set("Senpais." + senpai.getID() + ".Name", senpai.getUser().getName());
            data.set("Senpais." + senpai.getID() + ".Cooldown", senpai.getSpawnPackCooldown());
        }
        Files.DATA.saveFile();
    }
    
    public void saveSenpai(Senpai senpai) {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Senpais." + senpai.getID() + ".Name", senpai.getUser().getName());
        data.set("Senpais." + senpai.getID() + ".Cooldown", senpai.getSpawnPackCooldown());
        Files.DATA.saveFile();
    }
    
    public boolean isTesting() {
        return testing;
    }
    
    private void loadJDA() {
        if (jda == null) {
            try {
                jda = JDABuilder.createDefault(Files.CONFIG.getFile().getString("Token"))
                .addEventListeners(new CommandListener(), new SpawnPackListener(), new CardSpawnListener())
                .setActivity(Activity.watching("Shoob spawn great cards!"))
                .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isShoob(User user) {
        return user.isBot() && user.getId().equals("673362753489993749");
    }
    
    public String convertToTime(Calendar time) {
        return convertToTime(time.getTimeInMillis());
    }
    
    public String convertToTime(Calendar time, boolean reverseCheck) {
        return convertToTime(time.getTimeInMillis(), reverseCheck);
    }
    
    public String convertToTime(long time) {
        return convertToTime(time, false);
    }
    
    public String convertToTime(long time, boolean reverseCheck) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int total = reverseCheck ? (int) (Calendar.getInstance().getTimeInMillis() / 1000) - (int) (cal.getTimeInMillis() / 1000) : (int) (cal.getTimeInMillis() / 1000) - (int) (Calendar.getInstance().getTimeInMillis() / 1000);
        int day = 0, hour = 0, minute = 0, second = 0;
        for (; total > 86400; total -= 86400, day++) ;
        for (; total > 3600; total -= 3600, hour++) ;
        for (; total >= 60; total -= 60, minute++) ;
        second += total;
        String message = "";
        if (day > 0) message += day + "d, ";
        if (day > 0 || hour > 0) message += hour + "h, ";
        if (day > 0 || hour > 0 || minute > 0) message += minute + "m, ";
        if ((day == 0 && hour == 0) && (minute > 0 || second > 0)) message += second + "s, ";
        return message.length() < 2 ? "Now" : message.substring(0, message.length() - 2);
    }
    
    public String replaceEmotes(String message, Guild guild) {
        if (message.contains(":")) {
            // - ':emote:'
            // - 'Hello :emote:'
            // - ':emote: Hello'
            // - 'Hello :emote: lol'
            for (Emote emote : guild.getEmotes()) {
                if (message.contains(":" + emote.getName() + ":")) {
                    message = message.replace(":" + emote.getName() + ":", emote.getAsMention());
                }
            }
        }
        return message;
    }
    
}