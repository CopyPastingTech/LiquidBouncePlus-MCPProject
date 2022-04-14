package net.minecraft.client.gui;

import club.lbplus.utils.fonts.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        //int i = y + slotHeight / 2 - FontManager.getFont("ProductSans").getHeight() / 2;
        //FontManager.getFont("ProductSans").drawCenteredString("LAN Scanning...", this.mc.currentScreen.width / 2, i, 16777215);
        /*String s;

        switch ((int)(Minecraft.getSystemTime() / 300L % 4L))
        {
            case 0:
            default:
                s = ".";
                break;

            case 1:
            case 3:
                s = "..";
                break;

            case 2:
                s = "...";
        }

        this.mc.fontRendererObj.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, i + this.mc.fontRendererObj.FONT_HEIGHT, 8421504);*/
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }
}
