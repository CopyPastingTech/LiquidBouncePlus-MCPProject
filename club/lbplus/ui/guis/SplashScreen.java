package club.lbplus.ui.guis;

import club.lbplus.utils.GlobalInstances;
import club.lbplus.utils.animate.SmoothAnim;
import club.lbplus.utils.fonts.Texture;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.timer.SystemTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SharedDrawable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.opengl.GL11.*;
/*
 * @author Minecraft Forge Development Team.
 */
public class SplashScreen extends GlobalInstances {

    private static boolean isRunning = false;
    private static Thread drawThread = null;

    private static final Lock lock = new ReentrantLock(true);
    private static final Semaphore mutex = new Semaphore(1);

    private static Drawable drawable;

    private static int progress = 0;
    private static double progressAnim = 0;

    private static Texture logoTexture;

    public static void startDrawing() {
        logoTexture = new Texture(new ResourceLocation("lb+reloaded/icon/banner.png"));

        try {
            drawable = new SharedDrawable(Display.getDrawable());
            Display.getDrawable().releaseContext();
            drawable.makeCurrent();
        } catch (Exception e) {
            e.printStackTrace();
            return; // don't render anymore
        }

        drawThread = new Thread(() -> {
            try {
                lock.lock();
                Display.getDrawable().makeCurrent();
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }

            glClearColor(0, 0, 0, 1);
            glDisable(GL_LIGHTING);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            while (isRunning) {
                glClear(GL_COLOR_BUFFER_BIT);

                // matrix setup
                int w = Display.getWidth();
                int h = Display.getHeight();
                glViewport(0, 0, w, h);
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(320 - w/2, 320 + w/2, 240 + h/2, 240 - h/2, -1, 1);
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();

                // mojang logo
                setColor(-1);
                glEnable(GL_TEXTURE_2D);
                logoTexture.bind();
                glBegin(GL_QUADS);
                logoTexture.texCoord(0, 0, 0);
                glVertex2f(320 - 256 - 65, 240 - 256 + 120 - 40);
                logoTexture.texCoord(0, 0, 1);
                glVertex2f(320 - 256 - 65, 240 + 256 - 120 - 40);
                logoTexture.texCoord(0, 1, 1);
                glVertex2f(320 + 256 + 65, 240 + 256 - 120 - 40);
                logoTexture.texCoord(0, 1, 0);
                glVertex2f(320 + 256 + 65, 240 - 256 + 120 - 40);
                glEnd();
                glDisable(GL_TEXTURE_2D);

                int bw = 450;
                glPushMatrix();
                glTranslatef(320 - bw / 2, 390 - 60, 0);
                glColor3f(0.2F, 0.2F, 0.2F);
                RenderUtils.drawRoundedRectMask(0, 0, (float) bw, 32, 16F);
                glColor3f(1, 1, 1);
                RenderUtils.drawRoundedRectMask(0, 0, (float) (bw * (Math.max(progressAnim, 5F) / 100F)), 32, 16F);
                glPopMatrix();

                glEnable(GL_TEXTURE_2D);
                progressAnim = SmoothAnim.animate(progress, progressAnim, 0.05F);

                mutex.acquireUninterruptibly();
                Display.update();
                mutex.release();

                Display.sync(60);
            }

            mc.displayWidth = Display.getWidth();
            mc.displayHeight = Display.getHeight();
            mc.resize(mc.displayWidth, mc.displayHeight);
            glClearColor(1, 1, 1, 1);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
            glEnable(GL_ALPHA_TEST);
            glAlphaFunc(GL_GREATER, .1f);

            try
            {
                Display.getDrawable().releaseContext();
            }
            catch (LWJGLException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            finally
            {
                lock.unlock();
            }


        });
        isRunning = true;
        progress = 0;
        drawThread.start();
    }

    public static void stopDrawing() {
        isRunning = false;
        while (drawThread.isAlive()) {} // Wait for thread stop before rendering anything else.
    }

    public static void setStage(int progr) {
        progress = progr;
    }

    private static void setColor(int color)
    {
        glColor3ub((byte)((color >> 16) & 0xFF), (byte)((color >> 8) & 0xFF), (byte)(color & 0xFF));
    }

    private static int max_texture_size = -1;
    public static int getMaxTextureSize()
    {
        if (max_texture_size != -1) return max_texture_size;
        for (int i = 0x4000; i > 0; i >>= 1)
        {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            if (GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) != 0)
            {
                max_texture_size = i;
                return i;
            }
        }
        return -1;
    }

}
