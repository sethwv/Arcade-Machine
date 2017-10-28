/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.arcade.commands;

import io.swvn.arcade.Command;
import net.dv8tion.jda.core.Permission;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author swvn9
 */
public class arcadeinfo extends Command{
    public arcadeinfo(){
        this.term = "arcadeinfo";
        this.description = "";
        this.alias = new String[]{};
        this.restricted = true;
        this.perms = new Permission[]{};
    }

    @Override
    protected void command(){
        File guidelinesImage = new File("headings"+File.separator+"guidelines.png");
        File infoImage = new File("headings"+File.separator+"bothelp.png");
        File bothelpImage = new File("headings"+File.separator+"information.png");

        try{

            channel.sendFile(guidelinesImage,null).queueAfter(0, TimeUnit.SECONDS);
            channel.sendMessage(
                    "[ <:controller:369274326312026112> ] **__What are the rules of this guild?__** \n\n⁍ #1 ~ **__No Spamming__**\n         ⁃ Spamming is a randomly repeated phrase or word that causes chaos. If you spam, it will land you a 10 minute mute. \n\n⁍ #2 ~ **__No Advertisements__**\n          ⁃ This includes DM Advertising. If you do & it gets reported, you will get an instant kick. \n          ⁃ DM Advertisers include: Discord Links, Youtube Videos, & any other links. \n\n⁍ #3 ~ No **__NSFW Content__**\n          ⁃ a) No NSFW Content is Tolerated in this server due to people being of younger ages. \n          ⁃ b) This is a gaming server, not an NSFW Server. If you want to see NSFW join a server that has one.\n          ⁃ c) No innapropriate profile pictures\n\n⁍ #4 ~ **__No Harassing__**\n          ⁃ Hurtful jokes are not tolerated & will land you an instant kick if provided with proof. \n          ⁃ Bullying is not at all tolerated in this server. If a user reports another user bullying with proof, the user will be banned instantly. \n\n⁍ #5 ~ **__Use All Channels Appropriately__**\n          ⁃ Please use all channels appropriately. \n          ⁃ No bot commands are permitted in #general & must be used in #controller-spam. The channel #general is mainly used to talk about General Magic & Off-Topic Discussions. \n\n⁍ #6 ~ **__The Staff Team Has The Final Say In All Decisions__**\n          ⁃ If a staff team member tells you to stop doing something, listen to them.\n          ⁃ Higher staff members may override the opinions or judgements of lower staff team members.\n\n⁍ #7 ~ **__No Loopholing these Guidelines or Discord ToS__**\n          ⁃ Loopholing of these Guidelines is not allowed & will land you a instant kick.\n          ⁃ Activities such as leaking personal information or DDoSing another member will get you instantly banned.\n          ⁃ If you fail to follow the Discord ToS, you will be instantly banned from this server. Please read the Discord ToS at <https://discordapp.com/terms>"
            ).queueAfter(1, TimeUnit.SECONDS);

            channel.sendFile(bothelpImage,null).queueAfter(2, TimeUnit.SECONDS);
            channel.sendMessage(
                    "[ <:controller:369274326312026112> ] **__What ranks can I earn?__**\n\n```Special Ranks\n\n- Arcade Operator: \nBelieved to be a direct descendent of Yemper himself, this team is in charge of managing the server in his absense.\n\n\n- Arcade Manager: \nForged from the deepest caverns of an MMO fortress, these cave hugging hoolagins are responsible for maintaining order in a land of chaos.\n\n- Arcade Staff: \nRigorous honesty and mature activity will be the primary selection process for new candidates in this secretive organization.\n\n- Intern:\nHave you ever wanted to shoot lightning bolts from your fingertips like a Sith Lord or a god? Well NOW YOU CAN (figuratively, you can)! Active users and demolished reputations will earn you.... just kidding you need a solid reputation to shoot lightning out of your hands like a god, I mean who doesn't? Protect the land with initiation into the sacred order of Moderators.```\n\nMore to Come Soon:tm:!"
            ).queueAfter(3, TimeUnit.SECONDS);

            channel.sendFile(infoImage,null).queueAfter(4, TimeUnit.SECONDS);
            channel.sendMessage(
                    "[ <:controller:369274326312026112> ] **__How can I use these wonderful bots?__**\n\n⁍ [ <:Arcade:371330364590784512>  ] <@!369176187638906882> \n            ⁃ This is a premium utility bot that is only in this server. \n            ⁃ It has many cool features which you can access using the command `>help`.\n                      ⁃ Developer(s) ~ <@111592329424470016> \n\n⁍ [ <:SpeedBoat:371330364263628811> ] <@!351776065477279745> \n            ⁃ Speedboat is a private bot that is a rowboat clone. \n            ⁃ You can find all the commands at: https://rowboat.party\n                      ⁃ Developer(s) ~  <@80351110224678912>\n                      ⁃ Maintaineer(s) ~  <@111592329424470016> and <@194861788926443520>\n\n⁍ [ <:Ikari:371330364926590986> ] <@!339612757324333057> \n            ⁃ Ikari is a moderation / utility bot that has plenty of features."
            ).queueAfter(5, TimeUnit.SECONDS);

        } catch (Exception ignored){
            respond("Could not find information assets, are you running this from the IDE instance?");
            ignored.printStackTrace();
        }

    }
}
