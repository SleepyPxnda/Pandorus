package de.cloudypanda.commands;

import de.cloudypanda.database.entities.TempChannelConfig;
import de.cloudypanda.database.repositories.TempChannelRepository;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {

    private final TempChannelRepository tempChannelRepository;

    public CommandHandler(TempChannelRepository tempChannelRepository) {
        this.tempChannelRepository = tempChannelRepository;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        if(e.getName().equals("tempchannel")){
            Channel selectedChannel = e.getOption("voicechannel").getAsChannel();

            if(!(selectedChannel instanceof VoiceChannel)){
                e.getInteraction().reply("Only voicechannels can be the source of tempchannels")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            e.getInteraction().reply("Saved configuration")
                    .setEphemeral(true)
                    .queue();

            Long currentGuild = e.getGuild().getIdLong();
            Long selectedChannelId = selectedChannel.getIdLong();

            if(tempChannelRepository.findTempChannelConfigByGuildId(currentGuild) != null){
                tempChannelRepository.updateTempTriggerChannelIdByGuildId(currentGuild,selectedChannelId);
            } else {
                tempChannelRepository.save(new TempChannelConfig((long) 0, currentGuild, selectedChannelId));
            }


        }
    }
}
