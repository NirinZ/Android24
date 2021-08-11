package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Race;
import com.SharxNZ.GameFunctions.StartGame;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StartGameCommand extends Command {

    public StartGameCommand() {
        super.name = "startGame";
        super.aliases = new String[]{"sg", "s"};
        super.help = "Start the game";
        super.arguments = "[Your type]";

        OptionData optionData = new OptionData(OptionType.STRING, "race", "choose the race you want to play").setRequired(true);
        for (Race race: Race.values()){
            optionData.addChoice(race.name(), race.name());
        }
        Android24.commandListUpdateAction.addCommands(new CommandData("start_game", "Let's you start the game and choose your race")
                .addOptions(optionData));
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Race race;
        if(!commandEvent.getArgs().equals(""))
            race = Race.valueOf(commandEvent.getArgs());
        else
            race = Race.Saiyan;
        commandEvent.reply(StartGame.startGame(commandEvent.getAuthor().getIdLong(), race));
    }
}
