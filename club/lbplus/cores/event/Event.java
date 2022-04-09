package club.lbplus.cores.event;

import club.lbplus.LiquidCore;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {
	private boolean cancelled;
	private EventType type;

	public Event call() {
		this.cancelled = false;
		this.call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void cancelEvent() {
		this.cancelled = true;
	}

	public EventType getType() {
		return type;
	}

	public boolean isPre() {
		return type == null ? false : type == EventType.PRE;
	}

	public boolean isPost() {
		return type == null ? false : type == EventType.POST;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	private static void call(Event event) {
		if (LiquidCore.getCore().eventManager == null) return;

		ArrayHelper<Data> dataList = LiquidCore.getCore().eventManager.get(event.getClass());
		if (dataList != null) {
			for (Data data : dataList) {
				try {
					data.target.invoke(data.source, event);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
