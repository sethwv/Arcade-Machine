/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import io.swvn.arcade.Command;
import io.swvn.arcade.Config;
import net.dv8tion.jda.core.Permission;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;

import static java.awt.Color.red;
import static java.awt.Color.white;

/**
 * @author swvn9
 */
public class text extends Command{

    public int offset = 20;

    public text(){
        this.term = "text";
        this.usage = "[hex] <message>";
        this.alias = new String[]{};
        this.description = "Generate an image from a string of text";
        this.restricted = false;
        this.perms = new Permission[]{
                Permission.ADMINISTRATOR
        };
    }

    @Override
    protected void command(){
        content = message.getStrippedContent().replaceFirst(Config.prefix()+invoking, "").trim();
        String text = content.replaceFirst(args[0],"").trim();
        Color selectedColor;

        String[] contentArr = text.split("%n");

        try{
            selectedColor = Color.decode(args[0]);
        } catch (Exception ignored){
            contentArr = content.split("%n");
            selectedColor = white;
        }
        /*
           I was far too lazy to write this text-generation myself,
           source: https://stackoverflow.com/questions/18800717/convert-text-content-to-image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Uni Sans Heavy CAPS", Font.PLAIN, 182);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int height;
        if(contentArr.length==1)
            height = 152*contentArr.length;
        else
            height = fm.getHeight()*contentArr.length;
        int width = fm.stringWidth(getLongestString(contentArr)+20);

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
        g2d.setColor(selectedColor);
        int lines = 0;
        for(String s: contentArr){
            g2d.drawString(s,0,fm.getAscent()+(182*lines));
            lines++;
        }
        //g2d.drawString(text, 0, fm.getAscent());
        //g2d.drawString(contentArr[1],0,fm.getAscent()+182);
        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File("temp"+File.separator+"Text.png"));
            //channel.sendFile(new File("Text.png"),null).queue();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        BufferedImage imageOne = null;
        BufferedImage imageTwo = null;
        try{
            imageOne = ImageIO.read(new File("iconbase1.png"));
            imageTwo = ImageIO.read(new File("temp"+File.separator+"Text.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert imageOne !=null;
        assert imageTwo != null;

        width = imageOne.getWidth() + imageTwo.getWidth()+50;
        height = Math.max(imageOne.getHeight()-30,imageTwo.getHeight());

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D canvas = combined.createGraphics();
        canvas.setPaint(red);
        //canvas.fillRect(0,0,width,height);
        canvas.drawImage(imageOne, null, 0, -30);
        canvas.drawImage(imageTwo, null, imageOne.getWidth(),offset);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(combined, "png", outputStream);
        } catch (Exception ignored) {}

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        //channel.sendFile(inputStream, "Combined.png", null).queue();
        try {
            ImageIO.write(colorImage(ImageIO.read(inputStream),selectedColor), "png", new File("temp"+File.separator+"combined2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(new File("temp"+File.separator+"combined2.png"), null).queue();

    }

    private static BufferedImage colorImage(BufferedImage image, Color toColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = toColor.getRed();
                pixels[1] = toColor.getGreen();
                pixels[2] = toColor.getBlue();
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }

    private static String getLongestString(String[] array) {
        int maxLength = 0;
        String longestString = null;
        for (String s : array) {
            if (s.length() > maxLength) {
                maxLength = s.length();
                longestString = s;
            }
        }
        return longestString;
    }

}
