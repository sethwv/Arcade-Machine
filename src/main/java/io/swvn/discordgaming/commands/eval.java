/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming.commands;

import groovy.util.Eval;
import io.swvn.discordgaming.Command;
import io.swvn.discordgaming.Config;
import net.dv8tion.jda.core.EmbedBuilder;


/**
 * @author swvn9
 */
public class eval extends Command{
    public eval(){
        this.term = "ev";
        this.alias = new String[]{"e"};
        this.restricted = true;
    }

    @Override
    protected void command(){
        if(isSuperUser()){
            String content;
            content = message.getRawContent().replaceFirst(Config.prefix()+invoking,"").trim();
            EmbedBuilder out = new EmbedBuilder();
            out.setColor(colour(jda.getSelfUser().getAvatarUrl(),1,1));
            out.addField("Input","```java\n" + content + "```",false);
            try{
                String val = Eval.me("import io.swvn.discordgaming.*;\nimport net.dv8tion.jda.core.*;\nimport static io.swvn.discordgaming.Bot.*;\n"+content).toString();
                if(val!=null) out.addField("Output","```java\n"+val+"```",false);
            } catch (Exception e){
                if(!e.getClass().equals(NullPointerException.class))out.addField("Exception",("```exception\n" + e + "```"),false);
            }
            respond(out.build());
        }
    }
}
