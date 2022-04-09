package club.lbplus.utils.timer;

import lombok.Getter;

public class SystemTimer {

    @Getter
    private long lastMS = System.currentTimeMillis();

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimePassed(long ms) {
        return lastMS + ms < System.currentTimeMillis();
    }

    public long getElapsed(long ms) {
        return Math.min(0, lastMS + ms - System.currentTimeMillis());
    }

    public long getTimeLasted() {
        return System.currentTimeMillis() - lastMS;
    }

}
