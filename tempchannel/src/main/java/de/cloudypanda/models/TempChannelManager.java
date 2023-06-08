package de.cloudypanda.models;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.internal.managers.channel.concrete.VoiceChannelManagerImpl;

public class TempChannelManager extends VoiceChannelManagerImpl {
    public TempChannelManager(VoiceChannel channel) {
        super(channel);
    }

    public boolean canDeleteVoiceChannel() {
        return this.channel.getMembers().size() == 0;
    }
}
