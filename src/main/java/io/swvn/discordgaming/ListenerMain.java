/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import io.swvn.discordgaming.commands.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.awt.Color.WHITE;
import static java.awt.Color.red;

/**
 * @author swvn9
 */
@SuppressWarnings("ConstantConditions")
public class ListenerMain extends ListenerAdapter {

    public Command[] commands;

    public void startup(){

        commands = new Command[]{
                new help(),

                new test(),
                new text(),

                new eval(),
                new avatar(),

                //Empty space in the commands array
                null,
                null,
                null,
                null,
        };

        System.out.println("COMMANDS LOADED");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        if(!event.getGuild().getId().equals("369683790404124674")) return;
        String content = "Welcome, "+event.getMember().getEffectiveName()+"#"+event.getUser().getDiscriminator();

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Helvetica", Font.PLAIN, 182);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(content);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(WHITE);
        g2d.drawString(content, 0, fm.getAscent());
        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File("Text.png"));
            //channel.sendFile(new File("Text.png"),null).queue();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        BufferedImage imageOne = null;
        BufferedImage imageTwo = null;
        try{
            imageOne = ImageIO.read(new File("iconbase1.png"));
            imageTwo = ImageIO.read(new File("Text.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert imageOne !=null;
        assert imageTwo != null;

        width = imageOne.getWidth() + imageTwo.getWidth()+50;
        height = Math.max(imageOne.getHeight(),imageTwo.getHeight());

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D canvas = combined.createGraphics();
        canvas.setPaint(red);
        //canvas.fillRect(0,0,width,height);
        canvas.drawImage(imageOne, null, 0, 0);
        canvas.drawImage(imageTwo, null, imageOne.getWidth(),0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(combined, "png", outputStream);
        } catch (Exception ignored) {}

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        TextChannel channel = event.getJDA().getTextChannelById("369683790827618305");
        channel.sendFile(inputStream, "Combined.png", null).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(!event.getChannel().getType().equals(ChannelType.TEXT)) return;
        
        boolean isCommand;
        String prefix = Config.pull().getPrefix();
        String messageRaw;

        messageRaw = event.getMessage().getRawContent();
        isCommand = (messageRaw.startsWith(prefix));

        if(isCommand){
            String invoking;

            invoking = messageRaw
                    .replaceFirst(prefix,"")
                    .trim()
                    .split(" ")[0];

            for(Command command : commands){

                if(null==command) continue;

                if(!command.disabled){

                    if(invoking.equals(command.term)){
                        command.run(event);
                        return;
                    }

                    for(String alias : command.alias){
                        if(invoking.equals(alias)){
                            command.run(event);
                            return;
                        }
                    }

                }

            }
        }
    }
}
