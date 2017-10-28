/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import groovy.util.Eval;
import io.swvn.arcade.Command;
import io.swvn.arcade.Config;
import net.dv8tion.jda.core.EmbedBuilder;

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
            out.setColor(colour(jda.getSelfUser().getAvatarUrl()));
            out.addField("Input","```java\n" + content + "```",false);
            try{
                String val = Eval.me(
                            "import io.swvn.arcade.*;\n" +
                            "import net.dv8tion.jda.core.*;\n" +
                            "import static io.swvn.arcade.Bot.*;\n" +
                            "import static io.swvn.arcade.Bot.jda;\n" +
                            "import static io.swvn.arcade.Bot.running;\n" +
                            "import static io.swvn.arcade.Bot.listener;\n" +
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
