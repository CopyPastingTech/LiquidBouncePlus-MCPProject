package club.lbplus.utils.animate;

import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.timer.SystemTimer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class AnimBackground {

    private static final List<ResourceLocation> resLocs = new ArrayList<>();
    private static final SystemTimer animTimer = new SystemTimer();
    private static int level = 0;

    public static void initFunc() {
        for (int i = 0; i < 3; i++)
            resLocs.add(new ResourceLocation("lb+reloaded/animated/background" + i + ".png"));
    }

    public static void handleDraw(double x, double y, double x2, double y2) {
        if (x > x2) {
            double z = x2;
            x2 = x;
            x = z;
        }
        if (y > y2) {
            double z = y2;
            y2 = y;
            y = z;
        }

        GL11.glPushMatrix();
        RenderUtils.drawImage(resLocs.get(level), x, y, x2 - x, y2 - y);
        GL11.glPopMatrix();

        if (animTimer.hasTimePassed(80)) {
            level++;
            animTimer.reset();
        }

        if (level > 2)
            level = 0;
    }

}
