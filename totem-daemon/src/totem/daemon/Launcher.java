package totem.daemon;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gmoreno on 29/09/2017.
 */
public class Launcher {

    private static final Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    private static WebContainer SERVER = new WebContainer();

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("start")) {
            new Thread() {

                @Override
                public void run() {
                    try {

                        SERVER.start();
                        LOGGER.log(Level.INFO, "Started");
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                        System.exit(1);
                    }
                }
            }.start();
        } else if (args.length > 0 && args[0].equals("stop")) {
            SERVER.stop();
            LOGGER.log(Level.INFO, "Stopped");
            System.exit(0);
        }
    }
}
