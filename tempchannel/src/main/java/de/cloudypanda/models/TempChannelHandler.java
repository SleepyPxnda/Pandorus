package de.cloudypanda.models;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.ArrayList;
import java.util.List;

public class TempChannelHandler {

    private List<TempChannelManager> activeTempChannels = new ArrayList<>();

    private RestAction<VoiceChannel> createTempChannel(Category category, String channelName) {
        return category.createVoiceChannel(channelName)
                .onSuccess(channel -> activeTempChannels.add(new TempChannelManager(channel)));
    }

    public void createTempChannelAndMoveMember(Category category, String name, Member member){
        createTempChannel(category, name + (activeTempChannels.size() + 1))
                .onSuccess(x -> member.getGuild().moveVoiceMember(member, x).queue())
                .queue();
    }

    /**
     * Removes a channel from the active channels if its exists
     * @param channelId
     * @return true if it removed an element, false otherwise
     */
    public boolean checkAndRemoveTempChannel(Long channelId){
        return this.activeTempChannels.removeIf(x -> x.canDeleteVoiceChannel() && x.getChannel().getIdLong() == channelId);
    }
}
