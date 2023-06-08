package de.cloudypanda.commands;

import de.cloudypanda.database.entities.ServerInfoConfig;
import de.cloudypanda.database.repositories.ServerinfoRepository;
import de.cloudypanda.util.NetworkHelper;
import de.cloudypanda.util.NetworkStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ServerinfoCommandHandler extends ListenerAdapter {

    private final ServerinfoRepository serverinfoRepository;

    public ServerinfoCommandHandler(ServerinfoRepository serverinfoRepository) {
        this.serverinfoRepository = serverinfoRepository;
    }

    @Override
    @Transactional
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        if(e.getName().equals("serverinfo")){
            if(e.getSubcommandName().equals("add")){
                handleServerinfoAddCommand(e);
                return;
            }

            if(e.getSubcommandName().equals("remove")){
                handleServerinfoRemoveCommand(e);
                return;
            }

            if(e.getSubcommandName().equals("list")){
                handleServerinfoListCommand(e);
            }
        }
    }

    private void handleServerinfoListCommand(SlashCommandInteractionEvent e) {
        List<ServerInfoConfig> servers = serverinfoRepository.findServerInfoConfigsByGuildId(e.getGuild().getIdLong());

        EmbedBuilder embed = new EmbedBuilder();

        e.deferReply().setEphemeral(true).queue();
        Instant beforeRequest = Instant.now();

        servers.forEach(server -> {
            NetworkStatus statusForServer = NetworkHelper.pingIp(server.getIpAddress());

            String status = switch (statusForServer){
                case REACHABLE -> "\uD83D\uDFE2";
                case UNREACHABLE -> "\uD83D\uDD34";
            };

            embed.addField(server.getServerName(), String.format("Hosted on _%s_ by %s is %s",
                    server.getIpAddress(),
                    e.getGuild().getMemberById(server.getAddedBy()).getAsMention(),
                    status
            ),false);
        });

        Instant afterRequest = Instant.now();
        embed.setColor(Color.cyan);
        embed.setTitle("Serverlist for " + e.getGuild().getName());
        embed.setFooter("Retrieving status took " + Duration.between(afterRequest, beforeRequest).toSeconds() + "s");

        e.getInteraction().getHook().editOriginalEmbeds(embed.build()).queue();
    }

    private void handleServerinfoRemoveCommand(SlashCommandInteractionEvent e) {
        String serverName = e.getInteraction().getOption("servername").getAsString();

        ServerInfoConfig found = serverinfoRepository.findServerInfoConfigByServerName(serverName);
        serverinfoRepository.deleteById(found.getId());

        e.reply("Deleted entry with name " + serverName).setEphemeral(true).queue();
    }

    private void handleServerinfoAddCommand(SlashCommandInteractionEvent e) {
        String serverName = e.getInteraction().getOption("servername").getAsString();
        String ipAdress = e.getInteraction().getOption("ipaddress").getAsString();

        if(serverinfoRepository.findServerInfoConfigByServerName(serverName) != null){
            e.reply("A Server with this name already exists").setEphemeral(true).queue();
            return;
        }


        serverinfoRepository.save(new ServerInfoConfig((long) 0,
                e.getGuild().getIdLong(),
                e.getMember().getIdLong(),
                serverName,
                ipAdress
                ));

        e.reply("Serverinfo successfully saved to database")
                .setEphemeral(true)
                .queue();
    }
}
