package club.lbplus.utils.render;

import net.minecraft.util.ResourceLocation;

public class TextureUtils {

    public static ResourceLocation banner;
    public static ResourceLocation singlePlayer;
    public static ResourceLocation multiPlayer;
    public static ResourceLocation settingLogo;
    public static ResourceLocation altLogo;
    public static ResourceLocation userLogo;
    public static ResourceLocation addLogo;
    public static ResourceLocation removeLogo;
    public static ResourceLocation refreshLogo;
    public static ResourceLocation returnLogo;
    public static ResourceLocation directLogo;
    public static ResourceLocation connectLogo;
    public static ResourceLocation editLogo;
    public static ResourceLocation downIcon;
    public static ResourceLocation alertIcon;
    public static ResourceLocation infoIcon;
    public static ResourceLocation successIcon;
    public static ResourceLocation errorIcon;
    public static ResourceLocation hudIcon;

    public static void initFunc() {
        banner = new ResourceLocation("lb+reloaded/icon/banner.png");
        singlePlayer = new ResourceLocation("lb+reloaded/material/singleplayer.png");
        multiPlayer = new ResourceLocation("lb+reloaded/material/multiplayer.png");
        settingLogo = new ResourceLocation("lb+reloaded/material/settings.png");
        altLogo = new ResourceLocation("lb+reloaded/material/alt.png");
        userLogo = new ResourceLocation("lb+reloaded/material/user.png");
        addLogo = new ResourceLocation("lb+reloaded/material/add.png");
        removeLogo = new ResourceLocation("lb+reloaded/material/remove.png");
        editLogo = new ResourceLocation("lb+reloaded/material/edit.png");
        refreshLogo = new ResourceLocation("lb+reloaded/material/refresh.png");
        connectLogo = new ResourceLocation("lb+reloaded/material/connect.png");
        returnLogo = new ResourceLocation("lb+reloaded/material/back.png");
        directLogo = new ResourceLocation("lb+reloaded/material/direct.png");
        downIcon = new ResourceLocation("lb+reloaded/material/direct.png");
        alertIcon = new ResourceLocation("lb+reloaded/material/alert.png");
        infoIcon = new ResourceLocation("lb+reloaded/material/info.png");
        successIcon = new ResourceLocation("lb+reloaded/material/success.png");
        errorIcon = new ResourceLocation("lb+reloaded/material/error.png");
        hudIcon = new ResourceLocation("lb+reloaded/gui/hud.png");
    }
    
}
