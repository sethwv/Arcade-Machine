/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming.commands;

import io.swvn.discordgaming.Bot;
import io.swvn.discordgaming.Command;
import io.swvn.discordgaming.Config;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.Objects;

/**
 * @author swvn9
 */
public class help extends Command{
    public help(){
        this.term = "help";
        this.description = "A command which shows all of the bot's available commands regardless of permission.";
        this.alias = new String[]{"?","h","commands"};
        this.restricted = false;
        this.perms = new Permission[]{
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ
        };
    }

    @Override
    protected void command(){
        StringBuilder response = new StringBuilder();
        response.append("**Loaded Commands**");
        response.append("```markdown\n");
        for(Command command : Bot.listener.commands){
            if(command!=null){
                StringBuilder aliases = new StringBuilder();
                for(String alias : command.alias)
                    aliases.append("|")
                            .append(alias);
                if(!command.disabled)
                    response.append(Config.prefix())
                            .append(command.term)
                            .append(aliases.toString())
                            .append(" ")
                            .append(command.usage)
                            .append("\n#")
                            .append(command.description)
                            .append("\n\n");
            }
        }

        response.append("\n```");
        respond(response.toString());
    }
}
