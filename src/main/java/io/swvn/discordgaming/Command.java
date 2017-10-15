/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author swvn9
 */
public abstract class Command {

    //command information
    protected String term = "none";
    protected String[] alias = new String[0];

    //command-flow info
    User author;
    TextChannel channel;
    Category category;
    Guild guild;

    //command-flow methods
    void run(MessageReceivedEvent event){
        this.author  = event.getAuthor();
        this.channel = event.getTextChannel();
        this.guild   = event.getGuild();

        this.command();
        this.cleanup();
    }
    void command(){
        this.respond("success!");
    }
    void cleanup(){
        //nothing here yet
    }

    //command utility methods
    boolean permCheck(){
        return true;
    }

    void respond(String content){
        this.channel.sendMessageFormat("%s",content).queue();
    }
    void log(){

    }

}
