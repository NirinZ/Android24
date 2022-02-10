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
                By activity you will also get zeni, which is the currency of the game.
                                            """);
                generalEmbed.addField("Field");
                generalEmbed.setFooter("Footer");
            }
            case "power_points" -> {
                generalEmbed.setDescription("""
                Power Points are the games' stats point. That means you can use those points to control and change your stats,
                by investing Power Points in the stats you want to improve (i.e. Health).
                You can obtain Power Points by leveling up!
                """);
                generalEmbed.addField("Field");
                generalEmbed.setFooter("Footer");
            }
            case "inventory" -> {
                generalEmbed.setDescription("""
                All the items you have collected, (including skills and transformations) can be found here.
                You can choose to viewe them all at once as a collectoin `list`, or one-by-one `individual`.
                """);
                generalEmbed.addField("Field");
                generalEmbed.setFooter("Footer");
            }
        }

        return generalEmbed.build();
    }

}