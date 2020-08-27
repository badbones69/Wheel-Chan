package me.badbones69.wheelchan.events;

import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SpawnTimer {
    
    private MessageChannel channel;
    private SpawnCard card;
    private int timer = 25;
    private Message message;
    
    public SpawnTimer(MessageChannel channel, SpawnCard card) {
        this.channel = channel;
        this.card = card;
        startTimer();
    }
    
    public MessageChannel getChannel() {
        return channel;
    }
    
    public SpawnCard getCard() {
        return card;
    }
    
    private void startTimer() {
        message = channel.sendMessage(getEmbedMessage().build()).complete();
        timer--;
        new Thread(() -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (card.isClaimed()) {
                    message.delete().complete();
                    cancel();
                } else {
                    message.editMessage(getEmbedMessage().build()).complete();
                    timer--;
                    if (timer < 0) {
                        message.delete().complete();
                        cancel();
                    }
                }
            }
        }, 1000L, 1000L)).start();
    }
    
    private EmbedBuilder getEmbedMessage() {
        String messageString = "Card Spawn Timer: **__%time%__**s";
        return new EmbedBuilder()
        .setColor(Color.CYAN)
        .setDescription(Messages.replacePlaceholders("%time%", timer + "", messageString));
    }
    
}