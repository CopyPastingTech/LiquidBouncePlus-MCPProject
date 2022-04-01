package net.minecraft.util;

public abstract class LazyLoadBase<T>
{
    private T value;
    private boolean isLoaded = false;

    /**
     * @author LlamaLad7
     * @reason Fix race condition
     */
    public T getValue()
    {
        if (!this.isLoaded) {
            synchronized (this) {
                if (!this.isLoaded) {
                    this.value = this.load();
                    this.isLoaded = true;
                }
            }
        }

        return this.value;
    }

    protected abstract T load();
}
