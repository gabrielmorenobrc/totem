package totem.daemon;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebContainer {

    private static final Logger logger = Logger.getLogger(WebContainer.class.getName());
    private static final Properties properties;

    private static final String WEBCONTAINER_PORT = "webcontainer_port";
    private static final String WEBCONTAINER_WEBAPP = "webcontainer_webapp";
    private static final String WEBCONTAINER_CONTEXT = "webcontainer_context";

    private static final String DEFAULT_WEBAPP = "webapp";
    private static final String DEFAULT_PORT = "5987";

    static {
        properties = new Properties();
        try {
            File propFile = new File("webservices.properties");
            if (propFile.exists()) {
                properties.load(new FileReader(propFile));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al leer configuración", e);
        }
    }

    private Server server;

    public static void main(String[] args) {
        try {
            WebContainer server = new WebContainer();
            server.start();
        } catch (Throwable t) {
            logger.log(Level.WARNING, null, t);
            System.exit(1);
        }
    }

    private void startWebServer() throws Exception {
        TicketPrinter.get().init();
        TicketPrinter.get().checkStatus();
        int port = new Integer(properties.getProperty(WEBCONTAINER_PORT, DEFAULT_PORT));
        server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();
        handler.addFilter(CORSFIlter.class, "/*", EnumSet.allOf(DispatcherType.class));
        handler.addFilter(CORSFIlter.class, "/ticket/*", EnumSet.allOf(DispatcherType.class));
        handler.addFilter(CORSFIlter.class, "/config/*", EnumSet.allOf(DispatcherType.class));
        handler.addServlet(TicketServlet.class, "/ticket");
        handler.addServlet(ConfigServlet.class, "/config");

        server.setHandler(handler);

        server.setStopAtShutdown(true);
        server.start();
    }

    public void start() throws Exception {
        startWebServer();
    }

    public void stop() throws Exception {
        server.stop();
    }
}

