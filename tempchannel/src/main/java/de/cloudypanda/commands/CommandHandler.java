package de.cloudypanda.commands;

import de.cloudypanda.database.repositories.TempChannelRepository;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {

    private TempChannelRepository tempChannelRepository;

    public CommandHandler(){

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        if(e.getName().equals("tempchannel")){
            Channel selectedChannel = e.getOption("voicechannel").getAsChannel();

            if(!(selectedChannel instanceof VoiceChannel)){
                e.reply("Only voicechannels can be the source of tempchannels")
                        .setEphemeral(true)
                        .queue();
                return;
            }
            tempChannelRepository.updateTempTriggerChannelIdByGuildId(
                    e.getGuild().getIdLong(),
                    selectedChannel.getIdLong());
        }
    }
}
