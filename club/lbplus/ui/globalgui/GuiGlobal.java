package club.lbplus.ui.globalgui;

import net.minecraft.client.gui.GuiScreen;

public class GuiGlobal extends GuiScreen {

    private static GuiGlobal instance;
    public static final GuiGlobal getInstance() {
        return instance == null ? instance = new GuiGlobal() : instance;
    }

    private boolean isClosing = false;

    public void initGui() {

    }

}
