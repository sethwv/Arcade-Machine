/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

/**
 * @author swvn9
 */
public class Bot {

    static JDA jda;

    public static void main(String[] args){

        try{

            jda = new JDABuilder(AccountType.BOT)
                    .setToken(args[0])
                    .buildAsync();

        } catch (LoginException | RateLimitedException exception) {


        }

    }

}
