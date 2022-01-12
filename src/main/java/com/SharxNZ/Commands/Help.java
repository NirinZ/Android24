package com.SharxNZ.Commands;

public abstract class Help{

    protected static SelectionMenu selectionMenu;

    public static SelectionMenu getMenu(){
        SelectionMenu.Builder smBuilder = SelectionMenu.create("help")
                    .setPlaceholder("Choose topic")
                    .setRequiredRange(1, 1);
        for(Choice choice : Android24.commands[0].getOptions().get(0).getChoices())
            smBuilder.addOption(choice.getName());
        return smBuilder.build();
    }

    public static MessageEmbed getEmbed(){
        return getEmbed("general");
    }

    public static MessageEmbed getEmbed(String topic){
        
        EmbedBuilder generalEmbed = new EmbedBuilder();
        generalEmbed.setTitle(topic);

        switch (topic){                    
            case "general" -> {
                generalEmbed.setDescription("Explaning the general");
                generalEmbed.addField("Field");
                generalEmbed.setFooter("Footer");
            }
            case "xp" -> {
                generalEmbed.setDescription("""
                The games' leveling system is based on XP.
                You can earn XP by activity on suppoted servers (text messages and VC time),
                or as a reword (i.e. from a battle).
                The amout of XP you have will determent your level. The more XP you have, the higher is your LVL!
                //צריך להמשיך
                                            """);
                generalEmbed.addField("Field");
                generalEmbed.setFooter("Footer");
            }
        }

        return generalEmbed.build();
    }

}