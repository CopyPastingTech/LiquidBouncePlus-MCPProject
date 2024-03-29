package club.lbplus;

import club.lbplus.cores.event.EventManager;
import club.lbplus.cores.misc.KeyHandler;
import club.lbplus.cores.misc.SoundHandler;
import club.lbplus.cores.module.ModManager;
import club.lbplus.ui.guis.SplashScreen;
import club.lbplus.utils.animate.AnimBackground;
import club.lbplus.utils.render.TextureUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiquidCore {
    public static final int BUILD_VERSION = 1;

    private static LiquidCore inst = new LiquidCore();
    public final Logger logger = LogManager.getLogger("LiquidBounce+ Reloaded");

    public ModManager modManager;
    public EventManager eventManager;
    public KeyHandler keyHandler;
    public SoundHandler soundHandler;

    private long startMS = 0L;
    public static boolean isStarting = true;

    public static final LiquidCore getCore() {
        return inst;
    }

    public void startInject() {
        startMS = System.currentTimeMillis();
        log("Starting the client.");

        SplashScreen.setStage(70);

        log("Event Manager & Value Manager at " + getRunMS());
        eventManager = new EventManager();

        SplashScreen.setStage(80);

        log("Module Manager at " + getRunMS());
        modManager = new ModManager();

        SplashScreen.setStage(90);

        log("Key Handler & Sound Handler at " + getRunMS());
        keyHandler = new KeyHandler();
        eventManager.register(keyHandler);
        soundHandler = new SoundHandler();

        log("Anim Background at " + getRunMS());
        AnimBackground.initFunc();

        log("Texture Utils at " + getRunMS());
        TextureUtils.initFunc();

        SplashScreen.setStage(100);

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
