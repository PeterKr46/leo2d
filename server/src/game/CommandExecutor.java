package game;


/**
 * Created by Peter on 18.11.2015.
 */
public class CommandExecutor {
    public static void onCommand(String command, String[] args, EntityManager.Entity origin) {
        switch (command) {
            case "alias":
                if(args.length >= 1) {
                    String alias = args[0];
                    if(alias.length() > 16) {
                        alias = alias.substring(0, 16);
                    }
                    origin.getClient().setAlias(alias);
                    origin.sendMessage("Your Alias was changed to '" + alias + "'.");
                } else {
                    origin.sendMessage("Usage: .alias <alias>");
                }
        }
    }
}
