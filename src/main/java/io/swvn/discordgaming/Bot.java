/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import io.sentry.Sentry;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.util.EventListener;

/**
 * @author swvn9
 */
public class Bot {

    public static JDA jda;

    public static Command running;
    public static ListenerMain listener;

    private static String ENV;
    private static String DSN;
    private static String TOKEN;

    public static void main(String[] args){

        TOKEN = Config.pull().getToken();
        DSN = Config.pull().getDsn();
        ENV = Config.pull().getEnv();

        try{

            Sentry.init(DSN);

            listener = new ListenerMain();
            listener.startup();

            jda = new JDABuilder(AccountType.BOT)
                    .setToken(TOKEN)
                    .addEventListener(listener)
                    .buildBlocking();

            jda.getPresence().setGame(Game.of(
                    "\uD83C\uDFAE","http://twitch.tv/discordapp"
            ));

        } catch (Exception exception) {

            exception.printStackTrace();
            Sentry.capture(exception);

        }

    }

}
