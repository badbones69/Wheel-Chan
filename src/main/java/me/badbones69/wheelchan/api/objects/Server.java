package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    
    private String guildID;
    private Guild guild;
    private String path;
    private CardTracker cardTracker;
    private List<Sensei> senseis = new ArrayList<>();
    private List<String> commandChannels = new ArrayList<>();
    
    public Server(String guildID) {
        this.guildID = guildID;
        path = "Servers." + guildID + ".";
        guild = WheelChan.getInstance().getJDA().getGuildById(guildID);
        cardTracker = new CardTracker();
        cardTracker.load(guildID);
        System.out.println(guild.getName() + ": " + guildID);
        FileConfiguration data = Files.DATA.getFile();
        data.getStringList(path + "Senseis").forEach(id -> senseis.add(new Sensei(id)));
        System.out.println("Senseis: " + senseis.stream().map(sensei -> sensei.getUser().getName()).collect(Collectors.joining(", ")));
        commandChannels.addAll(data.getStringList(path + "Command-Channels"));
        System.out.println("Command Channels: " + String.join(", ", commandChannels));
    }
    
    public Server(String guildID, boolean newServer) {
        if (newServer) {
            this.guildID = guildID;
            path = "Servers." + guildID + ".";
            guild = WheelChan.getInstance().getJDA().getGuildById(guildID);
            cardTracker = new CardTracker();
            cardTracker.load(guildID, true);
            System.out.println(guild.getName() + ": " + guildID);
            System.out.println("Senseis: " + senseis.stream().map(sensei -> sensei.getUser().getName()).collect(Collectors.joining(", ")));
            System.out.println("Command Channels: " + String.join(", ", commandChannels));
            saveData();
        } else {
            new Server(guildID);
        }
    }
    
    public String getGuildID() {
        return guildID;
    }
    
    public Guild getGuild() {
        return guild;
    }
    
    public String getServerName() {
        return guild.getName();
    }
    
    public CardTracker getCardTracker() {
        return cardTracker;
    }
    
    public List<Sensei> getSenseis() {
        return senseis;
    }
    
    public List<String> getSenseiIDs() {
        return senseis.stream().map(Sensei :: getID).collect(Collectors.toList());
    }
    
    public void addSensei(User sensei) {
        addSensei(sensei.getId());
    }
    
    public void addSenseis(List<User> senseiList) {
        boolean newSensei = false;
        for (User sensei : senseiList) {
            if (!isSensei(sensei.getId())) {
                senseis.add(new Sensei(sensei.getId()));
                newSensei = true;
            }
        }
        if (newSensei) {
            saveData();
        }
    }
    
    public void addSensei(String senseiID) {
        senseis.add(new Sensei(senseiID));
        saveData();
    }
    
    public void removeSensei(User sensei) {
        removeSensei(sensei.getId());
    }
    
    public void removeSensei(String senseiID) {
        senseis.remove(getSensei(senseiID));
        saveData();
    }
    
    public boolean isSensei(User sensei, Guild guild) {
        return isSensei(sensei.getId(), guild);
    }
    
    public boolean isSensei(String senseiID, Guild guild) {
        return isSensei(senseiID) || guild.getOwnerId().equals(senseiID);
    }
    
    public boolean isSensei(User sensei) {
        return isSensei(sensei.getId());
    }
    
    public boolean isSensei(String senseiID) {
        return getSensei(senseiID) != null;
    }
    
    public Sensei getSensei(String senseiID) {
        return senseis.stream().filter(sensei -> sensei.getID().equals(senseiID)).findFirst().orElse(null);
    }
    
    public List<String> getCommandChannels() {
        return commandChannels;
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
    
    public void saveData() {
        FileConfiguration data = Files.DATA.getFile();
        data.set(path + "Command-Channels", commandChannels);
        data.set(path + "Senseis", getSenseiIDs());
        Files.DATA.saveFile();
    }
    
}