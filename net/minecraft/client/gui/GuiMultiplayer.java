package net.minecraft.client.gui;

import club.lbplus.utils.misc.CustomButton;
import club.lbplus.utils.render.RenderUtils;
import club.lbplus.utils.render.Stencil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback
{
    private static final Logger logger = LogManager.getLogger();
    private final OldServerPinger oldServerPinger = new OldServerPinger();
    private GuiScreen parentScreen;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;

    /**
     * The text to be displayed when the player's cursor hovers over a server listing.
     */
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;

    public GuiMultiplayer(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        final int distTop = 40;//this.height - (this.height / 2 + 160);

        if (!this.initialized)
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try
            {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception exception)
            {
                logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }

            this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, distTop, this.height - distTop, 36);
            this.serverListSelector.func_148195_a(this.savedServerList);
            this.serverListSelector.setSlotXBoundsFromLeft(40);
        }
        else
        {
            this.serverListSelector.setDimensions(this.width, this.height, distTop, this.height - distTop);
            this.serverListSelector.setSlotXBoundsFromLeft(40);
        }

        this.createButtons();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }

    public void createButtons()
    {
        /*this.buttonList.add(this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0])));
        this.buttonList.add(this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0])));
        this.buttonList.add(this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));*/
        this.buttonList.add(this.btnSelectServer = new CustomButton(1, this.width / 2 - 220, this.height / 2 - 84, 80, 24, 3F, 3F, 0F, 0F, CustomButton.Type.CONNECT));
        this.buttonList.add(new CustomButton(4, this.width / 2 - 220, this.height / 2 - 60, 80, 24, 0F, 0F, 0F, 0F, CustomButton.Type.DIRECT));
        this.buttonList.add(new CustomButton(3, this.width / 2 - 220, this.height / 2 - 36, 80, 24, 0F, 0F, 0F, 0F, CustomButton.Type.ADD));
        this.buttonList.add(this.btnDeleteServer = new CustomButton(2, this.width / 2 - 220, this.height / 2 - 12, 80, 24, 0F, 0F, 0F, 0F, CustomButton.Type.REMOVE));
        this.buttonList.add(this.btnEditServer = new CustomButton(7, this.width / 2 - 220, this.height / 2 + 12, 80, 24, 0F, 0F, 0F, 0F, CustomButton.Type.EDIT));
        this.buttonList.add(new CustomButton(8, this.width / 2 - 220, this.height / 2 + 36, 80, 24, 0F, 0F, 0F, 0F, CustomButton.Type.REFRESH));
        this.buttonList.add(new CustomButton(0, this.width / 2 - 220, this.height / 2 + 60, 80, 24, 0F, 0F, 3F, 3F, CustomButton.Type.BACK));
        this.selectServer(this.serverListSelector.func_148193_k());
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.lanServerList.getWasUpdated())
        {
            List<LanServerDetector.LanServer> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.func_148194_a(list);
        }

        this.oldServerPinger.pingPendingNetworks();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (this.lanServerDetector != null)
        {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }

        this.oldServerPinger.clearPendingNetworks();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());

            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;

                if (s4 != null)
                {
                    this.deletingServer = true;
                    String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
                    String s1 = "\'" + s4 + "\' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                    String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
                    String s3 = I18n.format("gui.cancel", new Object[0]);
                    GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1)
            {
                this.connectToSelected();
            }
            else if (button.id == 4)
            {
                this.directConnect = true;
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
            }
            else if (button.id == 3)
            {
                this.addingServer = true;
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
            }
            else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.editingServer = true;
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
                this.selectedServer.copyFrom(serverdata);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 8)
            {
                this.refreshServerList();
            }
        }
    }

    private void refreshServerList()
    {
        this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }

    public void confirmClicked(boolean result, int id)
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());

        if (this.deletingServer)
        {
            this.deletingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.directConnect)
        {
            this.directConnect = false;

            if (result)
            {
                this.connectToServer(this.selectedServer);
            }
            else
            {
                this.mc.displayGuiScreen(this);
            }
        }
        else if (this.addingServer)
        {
            this.addingServer = false;

            if (result)
            {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer)
        {
            this.editingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.func_148195_a(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        int i = this.serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);

        if (keyCode == 63)
        {
            this.refreshServerList();
        }
        else
        {
            if (i >= 0)
            {
                if (keyCode == 200)
                {
                    if (isShiftKeyDown())
                    {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
                        {
                            this.savedServerList.swapServers(i, i - 1);
                            this.selectServer(this.serverListSelector.func_148193_k() - 1);
                            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            this.serverListSelector.func_148195_a(this.savedServerList);
                        }
                    }
                    else if (i > 0)
                    {
                        this.selectServer(this.serverListSelector.func_148193_k() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.func_148193_k() > 0)
                            {
                                this.selectServer(this.serverListSelector.getSize() - 1);
                                this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            }
                            else
                            {
                                this.selectServer(-1);
                            }
                        }
                    }
                    else
                    {
                        this.selectServer(-1);
                    }
                }
                else if (keyCode == 208)
                {
                    if (isShiftKeyDown())
                    {
                        if (i < this.savedServerList.countServers() - 1)
                        {
                            this.savedServerList.swapServers(i, i + 1);
                            this.selectServer(i + 1);
                            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            this.serverListSelector.func_148195_a(this.savedServerList);
                        }
                    }
                    else if (i < this.serverListSelector.getSize())
                    {
                        this.selectServer(this.serverListSelector.func_148193_k() + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1)
                            {
                                this.selectServer(this.serverListSelector.getSize() + 1);
                                this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            }
                            else
                            {
                                this.selectServer(-1);
                            }
                        }
                    }
                    else
                    {
                        this.selectServer(-1);
                    }
                }
                else if (keyCode != 28 && keyCode != 156)
                {
                    super.keyTyped(typedChar, keyCode);
                }
                else
                {
                    this.actionPerformed((GuiButton)this.buttonList.get(2));
                }
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.hoveringText = null;
        this.drawDefaultBackground();
        //this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        //this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), this.width / 2, 20, 16777215);

        //Stencil.write(true);
        RenderUtils.drawRoundedRect(this.width / 2 - 235, 35, this.width / 2 + 235, this.height - 35, 6F, true, 0xA0000000);
        //Stencil.erase(true);

        RenderUtils.makeScissorBox(this.width / 2 - 235, 35, this.width / 2 + 235, this.height - 35);
        GL11.glEnable(3089);

        if (this.serverListSelector.field_178041_q)
        {
            this.serverListSelector.mouseX = mouseX;
            this.serverListSelector.mouseY = mouseY;
            this.serverListSelector.drawBackground();
            int i = this.serverListSelector.getScrollBarX();
            int j = i + 6;
            this.serverListSelector.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            //this.serverListSelector.drawContainerBackground(tessellator);
            int k = this.serverListSelector.left + this.serverListSelector.width / 2 - this.serverListSelector.getListWidth() / 2 + 2;
            int l = this.serverListSelector.top + 4 - (int)this.serverListSelector.amountScrolled;

            if (this.serverListSelector.hasListHeader)
            {
                this.serverListSelector.drawListHeader(k, l, tessellator);
            }

            this.serverListSelector.drawSelectionBox(k, l, mouseX, mouseY);
            GlStateManager.disableDepth();
            int i1 = 4;
            //this.serverListSelector.overlayBackground(0, this.serverListSelector.top, 255, 255);
            //this.serverListSelector.overlayBackground(this.serverListSelector.bottom, this.serverListSelector.height, 255, 255);
            /*GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(this.serverListSelector.left, (this.serverListSelector.top + i1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.serverListSelector.right, (this.serverListSelector.top + i1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.serverListSelector.right, this.serverListSelector.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.serverListSelector.left, this.serverListSelector.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(this.serverListSelector.left, this.serverListSelector.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.serverListSelector.right, this.serverListSelector.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.serverListSelector.right, (this.serverListSelector.bottom - i1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.serverListSelector.left, (this.serverListSelector.bottom - i1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();*/
            int j1 = this.serverListSelector.func_148135_f();

            if (j1 > 0)
            {
                int k1 = (this.serverListSelector.bottom - this.serverListSelector.top) * (this.serverListSelector.bottom - this.serverListSelector.top) / this.serverListSelector.getContentHeight();
                k1 = MathHelper.clamp_int(k1, 32, this.serverListSelector.bottom - this.serverListSelector.top - 8);
                int l1 = (int)this.serverListSelector.amountScrolled * (this.serverListSelector.bottom - this.serverListSelector.top - k1) / j1 + this.serverListSelector.top;

                if (l1 < this.serverListSelector.top)
                {
                    l1 = this.serverListSelector.top;
                }

                /*worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(i, this.serverListSelector.bottom, 0.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j, this.serverListSelector.bottom, 0.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j, this.serverListSelector.top, 0.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(i, this.serverListSelector.top, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(i, (l1 + k1), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j, (l1 + k1), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j, l1, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(i, l1, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(i, (l1 + k1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((j - 1), (l1 + k1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((j - 1), l1, 0.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos(i, l1, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();*/
                RenderUtils.drawRoundedRect(i, this.serverListSelector.top + 4, j, this.serverListSelector.bottom - 4, (j - i) / 2, true, 0xA0202020);
                RenderUtils.drawRoundedRect(i, l1 + 4, j, l1 + k1 - 4, (j - i) / 2, true, 0xFF999999);
            }


            this.serverListSelector.func_148142_b(mouseX, mouseY);
            /*GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();*/
        }

        GL11.glDisable(3089);
        //Stencil.dispose();
        super.drawScreen(mouseX, mouseY, partialTicks);
/*
        if (this.hoveringText != null)
        {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
        }*/
    }

    public void connectToSelected()
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());

        if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
        {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected)
        {
            LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getLanServer();
            this.connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
        }
    }

    private void connectToServer(ServerData server)
    {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }

    public void selectServer(int index)
    {
        this.serverListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;

        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan))
        {
            this.btnSelectServer.enabled = true;

            if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
        }
    }

    public OldServerPinger getOldServerPinger()
    {
        return this.oldServerPinger;
    }

    public void setHoveringText(String p_146793_1_)
    {
        this.hoveringText = p_146793_1_;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }

    public ServerList getServerList()
    {
        return this.savedServerList;
    }

    public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_)
    {
        return p_175392_2_ > 0;
    }

    public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_)
    {
        return p_175394_2_ < this.savedServerList.countServers() - 1;
    }

    public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_)
    {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        this.savedServerList.swapServers(p_175391_2_, i);

        if (this.serverListSelector.func_148193_k() == p_175391_2_)
        {
            this.selectServer(i);
        }

        this.serverListSelector.func_148195_a(this.savedServerList);
    }

    public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_)
    {
        int i = p_175393_3_ ? this.savedServerList.countServers() - 1 : p_175393_2_ + 1;
        this.savedServerList.swapServers(p_175393_2_, i);

        if (this.serverListSelector.func_148193_k() == p_175393_2_)
        {
            this.selectServer(i);
        }

        this.serverListSelector.func_148195_a(this.savedServerList);
    }
}
