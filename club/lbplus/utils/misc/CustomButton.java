package club.lbplus.utils.misc;

import club.lbplus.utils.fonts.FontManager;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.render.TextureUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CustomButton extends GuiButton {
    public static float mainAlpha = 1F;

    public CustomButton(int id, int x, int y, int width, int height, float tl, float tr, float bl, float br, Type type) {
        super(id, x, y, width, height, type.getTypeName());
        this.topLeft = tl;
        this.topRight = tr;
        this.bottomLeft = bl;
        this.bottomRight = br;
        this.type = type;
    }

    private float hoverStrength = 0F;
    private final float topLeft;
    private final float topRight;
    private final float bottomLeft;
    private final float bottomRight;
    private final Type type;

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        Color currentState = new Color(enabled ? -1 : 10526880);
        if (mainAlpha < 1F) {
            currentState = new Color(currentState.getRed() / 255.0F, currentState.getGreen() / 255.0F, currentState.getBlue() / 255.0F, mainAlpha);
        }

        GlStateManager.resetColor();
        RenderUtils.customRoundedRect(xPosition, yPosition, xPosition + width, yPosition + height, topLeft, topRight, bottomRight, bottomLeft, true, new Color(hoverStrength, hoverStrength, hoverStrength, currentState.getAlpha() / 255.0F * 0.4F).getRGB());
        GlStateManager.disableAlpha();
        RenderUtils.drawImage(type.getResLoc(), xPosition + 4F, yPosition + 4F, height - 8F, height - 8F, currentState.getRed() / 255.0F, currentState.getGreen() / 255.0F, currentState.getBlue() / 255.0F, currentState.getAlpha() / 255.0F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        FontManager.getFont("ProductSans").drawCenteredString(type.getTypeName(), xPosition + width / 2 + 6F, yPosition + height / 2 - FontManager.getFont("ProductSans").getHeight() / 2, currentState.getRGB());
        GlStateManager.popMatrix();

        if (MouseUtils.isHoveredOn(mouseX, mouseY, xPosition, yPosition, width, height) && enabled)
            hoverStrength += 0.1F;
        else
            hoverStrength -= 0.1F;

        hoverStrength = MathHelper.clamp_float(hoverStrength, 0F, 0.6F);
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        SG("Singleplayer", TextureUtils.singlePlayer),
        MT("Multiplayer", TextureUtils.multiPlayer),
        ST("Options", TextureUtils.settingLogo),
        ALT("Alts", TextureUtils.altLogo),
        ADD("Add", TextureUtils.addLogo),
        REMOVE("Remove", TextureUtils.removeLogo),
        EDIT("Edit", TextureUtils.editLogo),
        CONNECT("Connect", TextureUtils.connectLogo),
        DIRECT("Direct", TextureUtils.directLogo),
        REFRESH("Refresh", TextureUtils.refreshLogo),
        BACK("Back", TextureUtils.returnLogo),
        BACKGROUND("Background", TextureUtils.hudIcon),
        QUIT("Quit", TextureUtils.errorIcon);

        private final String typeName;
        private final ResourceLocation resLoc;
    }
}
