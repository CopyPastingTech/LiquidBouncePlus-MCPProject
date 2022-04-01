package club.lbplus.cores.module;

import lombok.Getter;

import java.awt.*;

public enum Category {
    COMBAT("Combat", 0),
    MOVEMENT("Movement", 0),
    VISUALS("Visuals", 0),
    PLAYER("Player", 0),
    WORLD("World", 0),
    EXPLOIT("Exploit", 0),
    MISC("Misc", 0);

    @Getter
    private final String categoryName;
    private final int categoryColor;

    Category(String cName, int cColor) {
        categoryName = cName;
        categoryColor = cColor;
    }

    public int getCategoryHex() {
        return categoryColor;
    }

    public Color getCategoryColor() {
        return new Color(categoryColor);
    }
}
