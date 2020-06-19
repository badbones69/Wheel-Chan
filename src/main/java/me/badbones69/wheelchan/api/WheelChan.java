package me.badbones69.wheelchan.api;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpie;
import me.badbones69.wheelchan.listeners.CommandListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WheelChan {
    
    private static WheelChan instance = new WheelChan();
    private JDA jda;
    private List<Senpie> senpais = new ArrayList<>();
    private List<String> commandChannels = new ArrayList<>();
    
    public static WheelChan getInstance() {
        return instance;
    }
    
    public void load() {
        loadJDA();
        Messages.loadMessages();
        senpais.clear();
        commandChannels.clear();
        FileConfiguration data = Files.DATA.getFile();
        data.getStringList("Senpais").forEach(senpieID -> senpais.add(new Senpie(senpieID)));
        commandChannels.addAll(data.getStringList("Command-Channels"));
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public List<Senpie> getSenpais() {
        return senpais;
    }
    
    public List<String> getSenpieNames() {
        return senpais.stream().map(senpie -> senpie.getUser().getName()).collect(Collectors.toList());
    }
    
    public List<String> getSenpieIDs() {
        return senpais.stream().map(Senpie :: getID).collect(Collectors.toList());
    }
    
    public void removeSenpie(User senpie) {
        removeSenpie(senpie.getId());
    }
    
    public void removeSenpie(String senpieID) {
        senpais.remove(getSenpie(senpieID));
        saveData();
    }
    
    public void addSenpie(User senpie) {
        addSenpie(senpie.getId());
    }
    
    public void addSenpie(String senpieID) {
        senpais.add(new Senpie(senpieID));
        saveData();
    }
    
    public Senpie getSenpie(String senpieID) {
        for (Senpie senpie : senpais) {
            if (senpie.getID().equals(senpieID)) {
                return senpie;
            }
        }
        return null;
    }
    
    public boolean isSenpie(User senpie) {
        return isSenpie(senpie.getId());
    }
    
    public boolean isSenpie(String senpieID) {
        return getSenpie(senpieID) != null;
    }
    
    public void removeCommandChannel(MessageChannel channel) {
        removeCommandChannel(channel.getId());
    }
    
    public void removeCommandChannel(String channelID) {
        commandChannels.remove(channelID);
        saveData();
    }
    
    public void addCommandChannel(MessageChannel channel) {
        addCommandChannel(channel.getId());
    }
    
    public void addCommandChannel(String channelID) {
        commandChannels.add(channelID);
        saveData();
    }
    
    public boolean isCommandChannel(MessageChannel channel) {
        return isCommandChannel(channel.getId());
    }
    
    public boolean isCommandChannel(String channel) {
        return commandChannels.contains(channel);
    }
    
    private void loadJDA() {
        if (jda == null) {
            try {
                jda = new JDABuilder(AccountType.BOT).setToken(Files.CONFIG.getFile()
                .getString("Token"))
                .addEventListeners(new CommandListener()).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void saveData() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Command-Channels", commandChannels);
        data.set("Senpais", getSenpieIDs());
        Files.DATA.saveFile();
    }
    
}