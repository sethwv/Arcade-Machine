/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming.commands;

import groovy.util.Eval;
import io.swvn.discordgaming.Bot;
import io.swvn.discordgaming.Command;
import io.swvn.discordgaming.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

/**
 * @author swvn9
 */
public class eval extends Command{
    public eval(){
        this.term = "eval";
        this.description = "Evaluate a line or block of code through groovy's Eval.me\n#(Restricted to Super Admins)";
        this.usage = "<code>";
        this.alias = new String[]{"e"};
        this.restricted = true;
    }

    @Override
    protected void command(){
        if(isSuperUser()){
            String content;
            content = message
                    .getRawContent()
                    .replaceFirst(Config.prefix()+invoking,"")
                    .trim();
            EmbedBuilder out = new EmbedBuilder();
            out.setColor(colour(jda.getSelfUser().getAvatarUrl(),1,1));
            out.addField("Input","```java\n" + content + "```",false);
            try{
                String val = Eval.me(
                            "import io.swvn.discordgaming.*;\n" +
                            "import net.dv8tion.jda.core.*;\n" +
                            "import static io.swvn.discordgaming.Bot.*;\n" +
                            "import static io.swvn.discordgaming.Bot.jda;\n" +
                            "import static io.swvn.discordgaming.Bot.running;\n" +
                            "import static io.swvn.discordgaming.Bot.listener;\n" +
                            content).toString();
                if(val!=null)
                    out.addField("Output","```java\n"+val+"```",false);
            } catch (Exception e){
                if(!e.getClass().equals(NullPointerException.class))
                    out.addField("Exception",("```exception\n" + e + "```"),false);
            }
            respond(out.build());
        }
    }
}
