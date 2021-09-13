package com.SharxNZ.Buttons;

import com.SharxNZ.Commands.GameCommands.Shop;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShopButton extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent) {
        buttonClickEvent.replyEmbeds(Shop.shopBuy(buttonClickEvent.getComponentId(), buttonClickEvent.getUser().getIdLong()))
                .setEphemeral(true).queue();
    }
}
