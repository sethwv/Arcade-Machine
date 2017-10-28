/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import io.swvn.arcade.Bot;
import io.swvn.arcade.Command;
import net.dv8tion.jda.core.Permission;

/**
 * @author swvn9
 */
public class var extends Command{
    public var(){
        this.term = "var";
        this.description = "Temporarily set a variable of the bot. Persists until reboot.\n#(Restricted to Super Admins)";
        this.alias = new String[]{"setvar"};
        this.restricted = true;
    }

    @Override
    protected void command(){
        switch(args[0].toLowerCase()){
            default:
                break;
            case "offset":
                for(Command command:Bot.listener.commands){
                    if(command instanceof text){
                        Bot.log(this.getClass(),"info","VAR "+args[0].toUpperCase()+" SET TO "+args[1]+" (WAS "+((text)command).offset+")");
                        ((text)command).offset = Integer.parseInt(args[1]);
                    }
                }
        }
    }
}
