package club.lbplus.utils.fonts;

import club.lbplus.utils.GlobalInstances;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;

public class FontManager extends GlobalInstances {

    private static final HashMap<String, TTFFontRenderer> fonts = new HashMap<>();

    public static void registerFont(String name, Font font, boolean antiAliasing) {
        fonts.put(name, new TTFFontRenderer(font, antiAliasing, true));
    }

    public static void registerFont(Font font, boolean antiAliasing) {
        registerFont(font.getFontName(Locale.ENGLISH).replaceAll(" ", ""), font, antiAliasing);
    }

    public static void injectFontInstances() {
        for (int size : new int[]{18, 16})
            registerFont("Exhi" + size, FontUtils.getFontFromTTF(new ResourceLocation("lb+reloaded/fonts/tahoma.ttf"), size, Font.PLAIN), true);

        registerFont("ExhiBold", FontUtils.getFontFromTTF(new ResourceLocation("lb+reloaded/fonts/tahomabold.ttf"), 12, Font.PLAIN), true);
        registerFont(FontUtils.getFontFromTTF(new ResourceLocation("lb+reloaded/fonts/productsans.ttf"), 18, Font.PLAIN), true);
        registerFont("Comfortaa", FontUtils.getFontFromTTF(new ResourceLocation("lb+reloaded/fonts/comfortaa.ttf"), 18, Font.PLAIN), true);
        registerFont("ComfortaaLarge", FontUtils.getFontFromTTF(new ResourceLocation("lb+reloaded/fonts/comfortaa.ttf"), 80, Font.PLAIN), true);
    }

    public static TTFFontRenderer getFont(String name) {
        return fonts.get(name);
    }

}
