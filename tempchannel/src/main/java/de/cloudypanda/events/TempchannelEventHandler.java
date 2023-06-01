package de.cloudypanda.events;

import de.cloudypanda.database.entities.TempChannelConfig;
import de.cloudypanda.database.repositories.TempChannelRepository;
import de.cloudypanda.models.TempChannelHandler;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TempchannelEventHandler extends ListenerAdapter {
    private final TempChannelRepository tempChannelRepository;
    private final TempChannelHandler tempChannelHandler;
    public TempchannelEventHandler(TempChannelRepository tempChannelRepository, TempChannelHandler tempChannelHandler){
        this.tempChannelRepository = tempChannelRepository;
        this.tempChannelHandler = tempChannelHandler;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event){
        TempChannelConfig config = tempChannelRepository.findTempChannelConfigByGuildId(event.getGuild().getIdLong());

        //Create temp channel logic
        if(config != null){
            if(event.getChannelJoined() != null){
                if(event.getChannelJoined().getIdLong() == config.getTempTriggerChannelId()
                        && event.getChannelJoined().getGuild().getIdLong() == config.getGuildId()){
                    tempChannelHandler.createTempChannelAndMoveMember(
                            event.getChannelJoined().getParentCategory(),
                            config.getTempChannelPrefix(),
                            event.getMember());
                }
            }

            if(event.getChannelLeft() != null){
                boolean shouldRemoveChannel = tempChannelHandler.checkAndRemoveTempChannel(
                        event.getChannelLeft().getIdLong());

                if(shouldRemoveChannel){
                    event.getChannelLeft().delete().queue();
                }
            }
        }
    }
}
