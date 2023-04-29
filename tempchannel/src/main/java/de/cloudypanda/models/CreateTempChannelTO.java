package de.cloudypanda.models;

import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.Category;

@Getter
public class CreateTempChannelTO {
    Category category;
    String name;
}
