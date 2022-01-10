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
        }

        return generalEmbed.build();
    }

}