package io.swvn.discordgaming;

import io.swvn.discordgaming.commands.test;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ListenerMain extends ListenerAdapter {

    private Command[] commands;

    public void startup(){

        commands = new Command[]{
             new test()
        };

        System.out.println("DONE");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        boolean isCommand = false;
        String prefix = Config.pull().getPrefix();
        String messageRaw;

        messageRaw = event.getMessage().getRawContent();
        isCommand = (messageRaw.startsWith(prefix));

        if(isCommand){
            System.out.println("test");
            String invoking;

            invoking = messageRaw
                    .replaceFirst(prefix,"")
                    .trim()
                    .split(" ")[0];

            for(Command command : commands){

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
