/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade;

import io.sentry.Sentry;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author swvn9
 */
@SuppressWarnings("FieldCanBeLocal")
public class Bot {

    @SuppressWarnings("WeakerAccess")
    public static JDA jda;

    public static Command running;
    public static ListenerMain listener;

    protected static String ENV;
    private static String DSN;
    private static String TOKEN;

    public static void main(String[] args){

        TOKEN = Config.pull().getToken();
        DSN = Config.pull().getDsn();
        ENV = Config.pull().getEnv();

        Sentry.getContext().addExtra("ENV",ENV);

        System.setProperty("java.awt.headless", "true");

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

    public static void log(Class cl,String type,String message){
        final String RESET = "\u001B[0m";

        String COLOR = "\u001B[44m\u001B[30m";

        if(type.toLowerCase().equals("info"))
            COLOR = "\u001B[0m";

        if(type.toLowerCase().equals("error"))
            COLOR = "\u001B[41m\u001B[30m";

        if(type.toLowerCase().equals("init"))
            COLOR = "\u001B[42m\u001B[30m";

        if(type.toLowerCase().equals("hook"))
            COLOR = "\u001B[45m\u001B[30m";

        if(type.toLowerCase().equals("cmd")){
            COLOR = "\u001B[46m\u001B[30m";
            type = "CMD ";
        }

        System.out.println(COLOR+"["+LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm:ss"))+"] ["+type.toUpperCase()+"] ["+cl.getName()+"]: "+message+RESET);
    }

}
