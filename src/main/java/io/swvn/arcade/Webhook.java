/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/


package io.swvn.arcade;

import com.mashape.unirest.http.Unirest;
import io.sentry.Sentry;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Webhook {

    public static void start(){
        Timer timer = new Timer();
        Bot.log(Webhook.class,"hook","Starting webhooks");

        timer.schedule( new TimerTask() {
            public void run() {
                Bot.log(Webhook.class,"hook","Starting RSS update...");

                //Mobile articles
                Bot.log(Webhook.class,"hook","Pulling Mobile RSS Feeds");
                publishArticle(Bot.jda.getTextChannelById(363702337572831233L),"http://feeds.ign.com/ign/wireless-articles");

                //PS articles
                Bot.log(Webhook.class,"hook","Pulling Playstation RSS Feeds");
                publishArticle(Bot.jda.getTextChannelById(363702371181789185L),"http://feeds.ign.com/IGNPS4Articles");
                publishArticle(Bot.jda.getTextChannelById(363702371181789185L),"http://feeds.ign.com/ign/ps3-articles");

                //MS articles
                Bot.log(Webhook.class,"hook","Pulling Microsoft RSS Feeds");
                publishArticle(Bot.jda.getTextChannelById(363701823820791809L),"http://feeds.ign.com/IGNXboxOneArticles");
                publishArticle(Bot.jda.getTextChannelById(363701823820791809L),"http://feeds.ign.com/ign/xbox-360-articles");

                //PC articles
                Bot.log(Webhook.class,"hook","Pulling PC RSS Feeds");
                publishArticle(Bot.jda.getTextChannelById(363702083830153234L),"http://feeds.ign.com/ign/pc-articles");

                Bot.log(Webhook.class,"hook","Done RSS update.");
            }
        }, 0, 15*(60*1000));
    }

    public static List<Article> readRSSFeed(String urlAddress){
        try{
            List<Article> articles = new ArrayList<>();

            URL rssUrl = new URL(urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
            String sourceCode = "";
            String line;

            String title = null;
            String name = null;
            String link = null;
            String description = null;
            String media = "";

            while((line=in.readLine())!=null){
                if(line.contains("<title>")){
                    //System.out.println(line);
                    int firstPos = line.indexOf("<title>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<title>","");
                    int lastPos = temp.indexOf("</title>");
                    temp = temp.substring(0,lastPos);
                    title = temp;
                }
                if(line.contains("<dc:creator>")){
                    //System.out.println(line);
                    int firstPos = line.indexOf("<dc:creator>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<dc:creator>","");
                    int lastPos = temp.indexOf("</dc:creator>");
                    temp = temp.substring(0,lastPos);
                    name = temp;
                }
                if(line.contains("media:content")){
                    Pattern url = Pattern.compile("(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_,\\+.~#?&//=]*).png");
                    Matcher matcher = url.matcher(line);
                    while(matcher.find())
                        media = matcher.group(0);
                }
                if(line.contains("<link>")){
                    //System.out.println(line);
                    int firstPos = line.indexOf("<link>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<link>","");
                    int lastPos = temp.indexOf("</link>");
                    temp = temp.substring(0,lastPos);
                    link = temp;
                }
                if(line.contains("<description>")){
                    //System.out.println(line);
                    int firstPos = line.indexOf("<description>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<description>","");
                    int lastPos = temp.indexOf("</description>");
                    temp = temp.substring(0,lastPos);
                    description = temp;
                }


                if(line.contains("<dc:date>")){
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    //System.out.println(line);
                    int firstPos = line.indexOf("<dc:date>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<dc:date>","");
                    int lastPos = temp.indexOf("</dc:date>");
                    temp = temp.substring(0,lastPos);
                    Date date = null;

                    try {
                        date = format.parse(temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    assert date != null;
                    LocalDateTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                    if(time.isBefore(LocalDateTime.now().minusMinutes(15L)))
                        break;
                    else
                        articles.add(new Article(title,name,link,description,media));
                }
            }
            in.close();
            return articles;
        } catch (Exception exception){
            Sentry.capture(exception);
        }
        return null;
    }

    public static void sendWebhook(String url,String name,String content){
        JSONObject bodyObject = new JSONObject();

        bodyObject.put("username",name)
                .put("content",content)
                .put("avatar_url","https://u.swvn9.net/2017/ca395.png");

        try{

            Unirest.post(url)
                    .header("content-type", "application/json")
                    .body(bodyObject.toString()).asString();

        } catch (Exception exception){
            Sentry.capture(exception);
        }
    }

    public static void publishArticle(TextChannel channel,String address){
        channel.createWebhook("Updating").reason("Getting new articles.").queue(w->{
            for(Article article : readRSSFeed(address)){
                sendWebhook(w.getUrl(),article.getName(),
                        "<@&373337631498698753> **"+article.getTitle()+"**\n"+ article.getDescription()+ "\n[Read More](<"+article.getLink()+">)"
                );
            }
            w.delete().queue();
        });
    }


}

class Article{

    Article(String title,String name, String link, String description, String media){
        this.title = title;
        this.name = name;
        this.link = link;
        this.description = description;
        this.media = media;
    }

    final String title;
    final String name;
    final String link;
    final String description;
    final String media;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getMedia() {
        return media;
    }
}
