package de.cloudypanda.models;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.ArrayList;
import java.util.List;

public class TempChannelHandler {

    private List<TempChannelManager> activeTempChannels = new ArrayList<>();

    private RestAction<VoiceChannel> createTempChannel(CreateTempChannelTO channelTO) {
        return channelTO.getCategory().createVoiceChannel(channelTO.getName())
                .onSuccess(channel -> activeTempChannels.add(new TempChannelManager(channel)));
    }

    public void createTempChannelAndMoveMember(CreateTempChannelTO channelTO, Member member){
        createTempChannel(channelTO)
                .onSuccess(x -> member.getGuild().moveVoiceMember(member, x).queue())
                .queue();
    }

    /**
     * Removes a channel from the active channels if its exists
     * @param channelTO
     * @return true if it removed an element, false otherwise
     */
    public boolean removeTempChannel(RemoveTempChannelTO channelTO){
        return this.activeTempChannels.removeIf(x -> x.getChannel().getIdLong() == channelTO.getChannelId());
    }
}
