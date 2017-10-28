/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import io.swvn.arcade.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author swvn9
 */
public class edit extends Command{
    public edit(){
        this.term = "edit";
        this.description = "Edit a message from the bot";
        this.alias = new String[]{};
        this.restricted = false;
        this.perms = new Permission[]{
                Permission.ADMINISTRATOR
        };
    }

    @Override
    protected void command(){
        if(args.length<2) return;
        Pattern toEdit = Pattern.compile("(?s)(```(.*)```)");
        Matcher newMessage = toEdit.matcher(content);

        while(newMessage.find())
            for(TextChannel textChannel: guild.getTextChannels()){
                try{
                    if(null!=textChannel.editMessageById(args[0],newMessage.group(2)).complete())
                        break;
                } catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        message.addReaction("\uD83D\uDC4D").queue();
    }
}
