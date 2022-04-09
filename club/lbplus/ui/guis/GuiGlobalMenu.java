package club.lbplus.ui.guis;

import club.lbplus.utils.fonts.FontManager;
import club.lbplus.utils.math.RandomUtils;
import club.lbplus.utils.misc.Changelog;
import club.lbplus.utils.misc.ColorUtils;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.render.Stencil;
import club.lbplus.utils.timer.SystemTimer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;

public class GuiGlobalMenu extends GuiScreen implements GuiYesNoCallback {

    private ArrayList<Particle> particles = new ArrayList<>();
    private final SystemTimer timer = new SystemTimer();

    @Override
    public void initGui() {
        super.initGui();
        particles.clear();
        timer.reset();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        particles.removeIf(it -> it.deleted);

        if (timer.hasTimePassed(600L))
            particles.add(new Particle(RandomUtils.randomRange(0, this.width), this.height + 20));

        mc.getFramebuffer().setFramebufferColor(0, 0, 0, 1);
        mc.getFramebuffer().framebufferClear();

        Stencil.write(false);
        for (Particle p : particles) {
            p.render();
        }
        Stencil.erase(true);

        for (int i = 0; i < 27; ++i) {
            RenderUtils.drawGradientSideways(
                    this.width / 25 * i,
                    0,
                    this.width / 25 * (i + 1),
                    this.height,
                    ColorUtils.getCustomRainbow(2, 0.6F, 1, i * 20).getRGB(),
                    ColorUtils.getCustomRainbow(2, 0.6F, 1, (i + 1) * 20).getRGB());
        }

        Stencil.dispose();

        GlStateManager.resetColor();

        int index = 0;
        for (String line : Changelog.changelogLines) {
            FontManager.getFont("ProductSans").drawString(line, 10, 10 + index * 10, -1);
            index++;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class Particle {

        private final double posX;
        private double posY;
        private final double beginY;
        private final double radius = RandomUtils.randomRange(5, 8);
        private final double moveSpeed = RandomUtils.randomRange(2.0, 4.0);
        private boolean deleted = false;

        public Particle(final double x, final double y) {
            this.posX = x;
            this.posY = y;
            this.beginY = y;
        }

        public void render() {
            if (posY <= -radius) {
                deleted = true;
                return;
            }
            if (beginY - posY >= radius) RenderUtils.drawFilledCircle(posX, posY, radius * (posY / beginY));
            posY -= moveSpeed * RenderUtils.deltaTime * 0.05;
        }

    }

}
