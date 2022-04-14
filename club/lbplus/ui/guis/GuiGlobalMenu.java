package club.lbplus.ui.guis;

import club.lbplus.LiquidCore;
import club.lbplus.utils.animate.AnimBackground;
import club.lbplus.utils.animate.SmoothAnim;
import club.lbplus.utils.fonts.FontManager;
import club.lbplus.utils.misc.CustomButton;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.render.TextureUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
public class GuiGlobalMenu extends GuiScreen implements GuiYesNoCallback {

    private float scaling = 0F;

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new CustomButton(0, width / 2 - 100, height / 2 + 20, 100, 24, 3F, 0F, 0F, 0F, CustomButton.Type.SG));
        this.buttonList.add(new CustomButton(1, width / 2, height / 2 + 20, 100, 24, 0F, 3F, 0F, 0F, CustomButton.Type.MT));
        this.buttonList.add(new CustomButton(3, width / 2 - 100, height / 2 + 44, 100, 24, 0F, 0F, 0F, 0F, CustomButton.Type.ALT));
        this.buttonList.add(new CustomButton(2, width / 2, height / 2 + 44, 100, 24, 0F, 0F, 0F, 0F, CustomButton.Type.ST));
        this.buttonList.add(new CustomButton(4, width / 2 - 100, height / 2 + 68, 100, 24, 0F, 0F, 3F, 0F, CustomButton.Type.BACKGROUND));
        this.buttonList.add(new CustomButton(5, width / 2, height / 2 + 68, 100, 24, 0F, 0F, 0F, 3F, CustomButton.Type.QUIT));
        scaling = 0F;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        AnimBackground.handleDraw(0, 0, width, height);

        float fullStrength = 0.7F + 0.3F * (scaling / 100F);
        String s2 = "Copyright Mojang AB. Do not distribute!";
        FontManager.getFont("ProductSans").drawString(s2, this.width - FontManager.getFont("ProductSans").getStringWidth(s2) - 2, this.height - 10, -1);
        FontManager.getFont("ProductSans").drawString("Minecraft 1.8.9, OF HD U M5", 2, this.height - 20, -1);
        FontManager.getFont("ProductSans").drawString("LiquidBounce+ Reloaded b" + LiquidCore.BUILD_VERSION, 2, this.height - 10, -1);
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2 * (1F - fullStrength), height / 2 * (1F - fullStrength), 0F);
        GlStateManager.scale(fullStrength, fullStrength, fullStrength);
        CustomButton.mainAlpha = scaling / 100F;
        super.drawScreen(mouseX, mouseY, partialTicks);
        CustomButton.mainAlpha = 1F;
        GlStateManager.disableAlpha();
        RenderUtils.drawImage(TextureUtils.banner, width / 2 - 1250 / 8, height / 2 - 518 / 5, 1250 / 4, 518 / 4, 1F, 1F, 1F, scaling / 100F);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        scaling = (float) SmoothAnim.animate(100F, scaling, 0.025F * 0.5F * RenderUtils.deltaTime);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 5)
        {
            this.mc.shutdown();
        }
    }

}
