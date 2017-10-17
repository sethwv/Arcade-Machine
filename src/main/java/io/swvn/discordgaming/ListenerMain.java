/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import io.swvn.discordgaming.commands.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author swvn9
 */
public class ListenerMain extends ListenerAdapter {

    public Command[] commands;

    public void startup(){

        commands = new Command[]{
                new help(),

                new test(),
                new eval(),
                new avatar(),

                //Empty space in the commands array
                null,
                null,
                null,
                null,
        };

        System.out.println("COMMANDS LOADED");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(!event.getChannel().getType().equals(ChannelType.TEXT)) return;
        
        boolean isCommand;
        String prefix = Config.pull().getPrefix();
        String messageRaw;

        messageRaw = event.getMessage().getRawContent();
        isCommand = (messageRaw.startsWith(prefix));

        if(isCommand){
            String invoking;

            invoking = messageRaw
                    .replaceFirst(prefix,"")
                    .trim()
                    .split(" ")[0];

            for(Command command : commands){

                if(null==command) continue;

                if(!command.disabled){

                    if(invoking.equals(command.term)){
                        command.run(event);
                        return;
                    }

                    for(String alias : command.alias){
                        if(invoking.equals(alias)){
                            command.run(event);
                            return;
                        }
                    }

                }

            }
        }
    }
}
