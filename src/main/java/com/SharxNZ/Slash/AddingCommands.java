package com.SharxNZ.Slash;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface AddingCommands {

    static void AddingCommands(){
        Android24.addCommands(
                // Power Points
                new CommandData("get_power_points", "Display and let you edit your power points")
                .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your stats"))
                .addOptions(new OptionData(OptionType.BOOLEAN, "refresh",
                        "Refreshes the image for every change. (Might by slow if display is false)")),
                // Shop
                new CommandData("shop", "All the operations that you can do in the Shop")
                        .addSubcommands(new SubcommandData("view", "View the items in the Shop")
                                .addOptions(new OptionData(OptionType.STRING, "of", "The type of the Shop you want to view")
                                        .addChoice("Special Attacks", "Special Attacks")
                                        .addChoice("Transformations", "Transformations")
                                        //.addChoice("Others", "Others")
                                        .setRequired(true))
                                .addOptions(new OptionData(OptionType.STRING, "type", "List or individual")
                                        .addChoice("List", "List")
                                        .addChoice("Individual", "Individual")))
                        .addSubcommands(new SubcommandData("buy", "Buy items from the Shop")
                                .addOptions(new OptionData(OptionType.STRING, "item", "The name of the item you want to buy (Can be the abbreviated name)")
                                        .setRequired(true))),
                // Inventory
                new CommandData("inventory", "Display your inventory")
                        .addOptions(new OptionData(OptionType.STRING, "of", "The type of the inventory")
                                .addChoice("Special Attacks", "Special Attacks")
                                .addChoice("Transformations", "Transformations")
                                .setRequired(true))
                        .addOptions(new OptionData(OptionType.STRING, "type", "List or individual")
                                .addChoice("List", "List")
                                .addChoice("Individual", "Individual"))

        );
    }
}
