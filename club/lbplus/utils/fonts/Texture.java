package club.lbplus.utils.fonts;

import club.lbplus.ui.guis.SplashScreen;
import club.lbplus.utils.GlobalInstances;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

public class Texture extends GlobalInstances
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

    private static final IntBuffer buf = BufferUtils.createIntBuffer(4 * 1024 * 1024);

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
