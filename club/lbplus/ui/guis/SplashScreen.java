package club.lbplus.ui.guis;

import club.lbplus.LiquidCore;
import club.lbplus.utils.GlobalInstances;
import club.lbplus.utils.animate.SmoothAnim;
import club.lbplus.utils.fonts.FontManager;
import club.lbplus.utils.misc.ColorUtils;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.timer.SystemTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SharedDrawable;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

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
    private static String stageName = "";
    private static double progressAnim = 0;

    private static final List<Texture> textures = new ArrayList<>();
    private static final SystemTimer animTimer = new SystemTimer();

    private static Texture fontTexture;
    private static SplashFontRenderer fontRenderer;

    private static final SystemTimer workingTimer = new SystemTimer();

    private static class SplashFontRenderer extends FontRenderer
    {
        public SplashFontRenderer()
        {
            super(Minecraft.getMinecraft().gameSettings, fontTexture.getLocation(), null, false);
            super.onResourceManagerReload(null);
        }

        @Override
        protected void bindTexture(ResourceLocation location)
        {
            if(location != locationFontTexture) throw new IllegalArgumentException();
            fontTexture.bind();
        }

        @Override
        protected InputStream getResourceInputStream(ResourceLocation location) throws IOException
        {
            return Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(location);
        }
    }

    private static int animIndex = 0;

    public static void startDrawing() {
        ScaledResolution scaledresolution = new ScaledResolution(mc);

        textures.add(new Texture(new ResourceLocation("lb+reloaded/animated/background0.png")));
        textures.add(new Texture(new ResourceLocation("lb+reloaded/animated/background1.png")));
        textures.add(new Texture(new ResourceLocation("lb+reloaded/animated/background2.png")));

        fontTexture = new Texture(new ResourceLocation("textures/font/ascii.png"));
        fontRenderer = new SplashFontRenderer();

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

                glColor3f(1, 1, 1);
                glEnable(GL_TEXTURE_2D);
                Texture logoTexture = textures.get(animIndex);
                logoTexture.bind();
                glBegin(GL_QUADS);
                logoTexture.texCoord(0, 0, 0);
                glVertex2f(-107, 0);
                logoTexture.texCoord(0, 0, 1);
                glVertex2f(-107, h);
                logoTexture.texCoord(0, 1, 1);
                glVertex2f(w - 100, h);
                logoTexture.texCoord(0, 1, 0);
                glVertex2f(w - 100, 0);
                glEnd();
                glDisable(GL_TEXTURE_2D);

                glColor3f(1, 1, 1);
                glBegin(GL_QUADS);
                glVertex2d(-107, h - 2);
                glVertex2d(-107 + w * (progressAnim / 100F), h - 2);
                glVertex2d(-107 + w * (progressAnim / 100F), h);
                glVertex2d(-107, h);
                glEnd();

                glEnable(GL_TEXTURE_2D);

                glPushMatrix();
                glTranslatef(-107 + w - fontRenderer.getStringWidth((int)progressAnim + "%") * 2F - 10F, h - 25, 0);
                glScalef(2, 2, 2);
                fontRenderer.drawStringWithShadow((int)progressAnim + "%", 0, 0, -1);
                glScalef(1, 1, 1);
                glPopMatrix();

                glPushMatrix();
                glTranslatef(-95, 10, 0);
                glScalef(2, 2, 2);
                fontRenderer.drawStringWithShadow("LiquidBounce+ Reloaded b" + LiquidCore.BUILD_VERSION, 0, 0, -1);
                glScalef(1, 1, 1);
                glPopMatrix();

                String estimated = String.format("%.1f", (float)workingTimer.getTimeLasted() / 1000F) + "s";

                glPushMatrix();
                glTranslatef(-107 + w - fontRenderer.getStringWidth(estimated) * 2F - 10F, 10, 0);
                glScalef(2, 2, 2);
                fontRenderer.drawStringWithShadow(estimated, 0, 0, -1);
                glScalef(1, 1, 1);
                glPopMatrix();

                glPushMatrix();
                glTranslatef(-95, h - 25, 0);
                glScalef(2, 2, 2);
                fontRenderer.drawStringWithShadow(stageName, 0, 0, -1);
                glScalef(1, 1, 1);
                glPopMatrix();

                progressAnim = SmoothAnim.animate(progress, progressAnim, 0.05F);

                if (animTimer.hasTimePassed(80)) {
                    animIndex++;
                    animTimer.reset();
                }

                if (animIndex > 2) animIndex = 0;

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
        stageName = "";
        animIndex = 0;
        animTimer.reset();
        workingTimer.reset();
        drawThread.start();
    }

    public static void stopDrawing() {
        isRunning = false;
        while (drawThread.isAlive()) {} // Wait for thread stop before rendering anything else.
    }

    public static void setStage(int progr, String name) {
        progress = progr;
        stageName = name;
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

    private static final IntBuffer buf = BufferUtils.createIntBuffer(4 * 1024 * 1024);

    private static class Texture
    {
        private final ResourceLocation location;
        private final int name;
        private final int width;
        private final int height;
        private final int frames;
        private final int size;

        public Texture(ResourceLocation location)
        {
            InputStream s = null;
            try
            {
                this.location = location;
                s = open(location);
                ImageInputStream stream = ImageIO.createImageInputStream(s);
                Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
                if(!readers.hasNext()) throw new IOException("No suitable reader found for image" + location);
                ImageReader reader = readers.next();
                reader.setInput(stream);
                frames = reader.getNumImages(true);
                BufferedImage[] images = new BufferedImage[frames];
                for(int i = 0; i < frames; i++)
                {
                    images[i] = reader.read(i);
                }
                reader.dispose();
                int size = 1;
                width = images[0].getWidth();
                height = images[0].getHeight();
                while((size / width) * (size / height) < frames) size *= 2;
                this.size = size;
                glEnable(GL_TEXTURE_2D);
                synchronized(SplashScreen.class)
                {
                    name = glGenTextures();
                    glBindTexture(GL_TEXTURE_2D, name);
                }
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
                checkGLError("Texture creation");
                for(int i = 0; i * (size / width) < frames; i++)
                {
                    for(int j = 0; i * (size / width) + j < frames && j < size / width; j++)
                    {
                        buf.clear();
                        BufferedImage image = images[i * (size / width) + j];
                        for(int k = 0; k < height; k++)
                        {
                            for(int l = 0; l < width; l++)
                            {
                                buf.put(image.getRGB(l, k));
                            }
                        }
                        buf.position(0).limit(width * height);
                        glTexSubImage2D(GL_TEXTURE_2D, 0, j * width, i * height, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buf);
                        checkGLError("Texture uploading");
                    }
                }
                glBindTexture(GL_TEXTURE_2D, 0);
                glDisable(GL_TEXTURE_2D);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            finally
            {
                IOUtils.closeQuietly(s);
            }
        }

        public ResourceLocation getLocation()
        {
            return location;
        }

        public int getName()
        {
            return name;
        }

        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }

        public int getFrames()
        {
            return frames;
        }

        public int getSize()
        {
            return size;
        }

        public void bind()
        {
            glBindTexture(GL_TEXTURE_2D, name);
        }

        public void delete()
        {
            glDeleteTextures(name);
        }

        public float getU(int frame, float u)
        {
            return width * (frame % (size / width) + u) / size;
        }

        public float getV(int frame, float v)
        {
            return height * (frame / (size / width) + v) / size;
        }

        public void texCoord(int frame, float u, float v)
        {
            glTexCoord2f(getU(frame, u), getV(frame, v));
        }
    }

    public static void checkGLError(String where)
    {
        int err = GL11.glGetError();
        if (err != 0)
        {
            throw new IllegalStateException(where + ": " + GLU.gluErrorString(err));
        }
    }

    private static InputStream open(ResourceLocation loc) throws IOException
    {
        return mc.mcDefaultResourcePack.getInputStream(loc);
    }

}
