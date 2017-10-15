/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import io.sentry.Sentry;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author swvn9
 */
public abstract class Command {

    //command information
    protected String term = "none";
    protected String[] alias = new String[0];
    protected Permission[] perms = new Permission[0];
    protected boolean restricted = true;

    //command-flow info
    protected Message message;
    protected User author;
    protected TextChannel channel;
    protected Guild guild;
    protected String invoking;
    protected JDA jda;

    //command-flow methods
    protected void run(MessageReceivedEvent event){
        this.message    = event.getMessage();
        this.author     = event.getAuthor();
        this.channel    = event.getTextChannel();
        this.guild      = event.getGuild();
        this.invoking   = message.getRawContent()
                .replaceFirst(Config.prefix(),"")
                .trim()
                .split(" ")[0];
        this.jda        = event.getJDA();

        if(this.permCheck())
            this.command();

        this.cleanup();
    }
    protected void command(){
        this.respond("This command is empty!");
    }
    protected void cleanup(){
        //nothing here yet
    }

    //command utility methods
    protected boolean permCheck(){
        Member member = guild.getMember(author);
        return (member.hasPermission(perms) && !restricted) || isSuperUser();
    }
    protected boolean canDo(Member target){
        Member member = guild.getMember(author);
        return member.canInteract(target) || isSuperUser();
    }
    protected boolean isSuperUser(){
        Long[] superUser = new Long[]{
                111592329424470016L,
                269904635337113603L
        };
        return Arrays.asList(superUser).contains(author.getIdLong());
    }

    protected void respond(String content){
        this.channel.sendMessageFormat("%s",content).queue();
    }
    protected void respond(MessageEmbed content){
        this.channel.sendMessage(content).queue();
    }
    void log(){

    }
    protected static Color colour(String in, int x, int y) {
        try{
            File file = new File("temp/sample.png");
            URL url = new URL(in);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            conn.connect();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
            BufferedImage image = ImageIO.read(file);
            int c = image.getRGB(x,y);
            int  red = (c & 0x00ff0000) >> 16;
            int  green = (c & 0x0000ff00) >> 8;
            int  blue = c & 0x000000ff;
            return new Color(red,green,blue);
        }catch(Exception ex){
            Sentry.capture(ex);
            return Color.black;
        }
    }

}
