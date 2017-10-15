/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming.commands;

import io.swvn.discordgaming.Command;
import net.dv8tion.jda.core.Permission;

/**
 * @author swvn9
 */
public class test extends Command{
    public test(){
        this.term = "test";
        this.alias = new String[]{"test2"};
        this.perms = new Permission[]{
                Permission.ADMINISTRATOR
        };
    }
}
