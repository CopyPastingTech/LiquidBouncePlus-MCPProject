package club.lbplus.utils.misc;

public class MouseUtils {

    public static boolean isHoveredOn(int mouseX, int mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

}
