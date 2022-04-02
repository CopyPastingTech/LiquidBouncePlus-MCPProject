package club.lbplus;

import club.lbplus.cores.event.EventManager;
import club.lbplus.cores.misc.KeyHandler;
import club.lbplus.cores.misc.SoundHandler;
import club.lbplus.cores.module.ModManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiquidCore {
    public static final int BUILD_VERSION = 1;

    private static LiquidCore inst;
    public final Logger logger = LogManager.getLogger("LiquidBounce+ Reloaded");

    public ModManager modManager;
    public EventManager eventManager;
    public KeyHandler keyHandler;
    public SoundHandler soundHandler;

    private long startMS = 0L;
    public static boolean isStarting = true;

    public static final LiquidCore getCore() {
        return inst == null ? inst = new LiquidCore() : inst;
    }

    public void startInject() {
        startMS = System.currentTimeMillis();
        log("Starting the client.");

        log("Event Manager & Value Manager at " + getRunMS());
        eventManager = new EventManager();

        log("Module Manager at " + getRunMS());
        modManager = new ModManager();

        log("Key Handler & Sound Handler at " + getRunMS());
        keyHandler = new KeyHandler();
        eventManager.register(keyHandler);
        soundHandler = new SoundHandler();

        log("Font Cache Handler at " + getRunMS());

        log("Successfully loaded the client in " + getRunMS());
        isStarting = false;
    }

    public void log(String message) {
        logger.log(Level.INFO, message);
    }

    private String getRunMS() {
        return (System.currentTimeMillis() - startMS) + "ms.";
    }
}
