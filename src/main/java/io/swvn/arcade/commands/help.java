/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import io.swvn.arcade.Bot;
import io.swvn.arcade.Command;
import io.swvn.arcade.Config;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

/**
 * @author swvn9
 */
@SuppressWarnings("ALL")
public class help extends Command{
    public help(){
        this.term = "help";
        this.description = "A command which shows all of the bot's available commands";
        this.alias = new String[]{"?","h","commands"};
        this.restricted = false;
        this.perms = new Permission[]{
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ
        };
    }

    protected boolean permCheck(User target, Permission[] permissions, Boolean restricted) {
        Member member = guild.getMember(target);
        return (member.hasPermission(permissions) && !restricted) || isSuperUser();
    }

    @Override
    protected void command(){
        StringBuilder response = new StringBuilder();
        response.append("**Loaded commands that you can use**");
        response.append("```markdown\n");
        for(Command command : Bot.listener.commands){
            if(command!=null){
                if(permCheck(author, command.perms, command.restricted)){
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
        }

        response.append("\n```");
        respond(response.toString());
    }
}
