package com.SharxNZ.Slash;

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SelectMenuEvents extends ListenerAdapter {

    @Override
    public void onSelectionMenu(SelectionMenuEvent selectMenuEvent){
        System.out.println(selectMenuEvent.getComponent().getOptions().get(0).getLabel());
        System.out.println(selectMenuEvent.getComponent().getOptions().get(0).getValue());
        System.out.println(selectMenuEvent.getComponent().getOptions().get(0).getDescription());
        System.out.println(selectMenuEvent.getSelectedOptions().get(0).getValue());
        System.out.println(selectMenuEvent.getInteraction().getSelectionMenu().getOptions().get(0).getValue());
        System.out.println(selectMenuEvent.getComponentId());
        System.out.println();
    }
}
