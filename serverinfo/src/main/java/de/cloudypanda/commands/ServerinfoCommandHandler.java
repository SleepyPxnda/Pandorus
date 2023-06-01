package de.cloudypanda.commands;

import de.cloudypanda.database.entities.ServerInfoConfig;
import de.cloudypanda.database.repositories.ServerinfoRepository;
import de.cloudypanda.util.NetworkHelper;
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

        StringBuilder serverNames = new StringBuilder();
        StringBuilder ipAdresses = new StringBuilder();
        StringBuilder statuses = new StringBuilder();

        e.deferReply().queue();
        Instant beforeRequest = Instant.now();

        servers.forEach(server -> {
            serverNames.append(server.getServerName()).append("\n");
            ipAdresses.append(server.getIpAddress()).append("\n");
            statuses.append(NetworkHelper.pingIp(server.getIpAddress())).append("\n");
        });

        Instant afterRequest = Instant.now();
        embed.setColor(Color.cyan);
        embed.setTitle("Serverlist for " + e.getGuild().getName());
        embed.setFooter("Retrieving status took " + Duration.between(afterRequest, beforeRequest).toSeconds() + "s");

        embed.addField("Name", serverNames.toString(), true);
        embed.addField("Address", ipAdresses.toString(), true);
        embed.addField("Status", statuses.toString(), true);

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
