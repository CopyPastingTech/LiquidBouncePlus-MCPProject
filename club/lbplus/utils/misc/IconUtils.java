package club.lbplus.utils.misc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/*
 * @author CCBlueX
 */
public final class IconUtils {

    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[] {readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/lb+reloaded/icon/icon16.png")), readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/lb+reloaded/icon/icon32.png"))};
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        if(imageStream == null)
            return null;

        final BufferedImage bufferedImage = ImageIO.read(imageStream);
        final int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for(int i : rgb)
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        byteBuffer.flip();
        return byteBuffer;
    }
}
