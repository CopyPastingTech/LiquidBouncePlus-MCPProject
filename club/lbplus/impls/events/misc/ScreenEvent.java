package club.lbplus.impls.events.misc;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

@Getter
@AllArgsConstructor
public class ScreenEvent extends Event {

    private final GuiScreen guiScreen;

}
