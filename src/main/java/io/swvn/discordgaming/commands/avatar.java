/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming.commands;

import io.swvn.discordgaming.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Icon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author swvn9
 */
public class avatar extends Command{
    public avatar(){
        this.term = "avatar";
        this.usage = "[true] [color hex]";
        this.alias = new String[]{"avatar2","icon"};
        this.description = "Match the bot's avatar to the highest granted role's colour." +
                "\n#(Restricted to Super Admins)";
        this.restricted = true;
    }

    @Override
    protected void command(){
        BufferedImage baseImage = null;
        BufferedImage newImage;
        Color selectedColor = null;

        if(args.length>=2)
            selectedColor = Color.decode(args[1]);
        else
            selectedColor = guild.getMember(jda.getSelfUser()).getColor();

        File baseImageFile = new File("base.png");
        if(invoking.contains("icon"))
            baseImageFile = new File("base2.png");
        if(invoking.contains("avatar2"))
            baseImageFile = new File("base3.png");

        try{
            baseImage = ImageIO.read(baseImageFile);
        } catch (Exception ignored) {}

        assert baseImage != null;
        newImage = new BufferedImage(
                baseImage.getWidth(),
                baseImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = newImage.createGraphics();

        canvas.setPaint(selectedColor);
        canvas.fillRect(0,0,baseImage.getWidth(),baseImage.getHeight());
        canvas.drawImage(baseImage, null, 0, 0);
        canvas.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(newImage, "png", outputStream);
        } catch (Exception ignored) {}

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        channel.sendFile(inputStream, "newAvatar.png", null).queue();

        if(args.length>=1)
                if(Boolean.parseBoolean(args[0]))
                    try {
                        ImageIO.write(newImage, "png", new File("newAvatar.png"));
                        jda.getSelfUser()
                                .getManager()
                                .setAvatar(Icon.from(new File("newAvatar.png")))
                                .queue();
                        new File("newAvatar.png").deleteOnExit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    }
}
