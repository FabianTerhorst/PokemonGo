package spacemadness.com.lunarconsole.console;

import java.util.Iterator;
import spacemadness.com.lunarconsole.utils.CycleArray;

public class LimitSizeList<T> implements Iterable<T> {
    private final CycleArray<T> internalArray;
    private final int trimSize;

    public LimitSizeList(Class<? extends T> cls, int capacity, int trimSize) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        this.internalArray = new CycleArray(cls, capacity);
        this.trimSize = trimSize;
    }

    public T objectAtIndex(int index) {
        return this.internalArray.get(this.internalArray.getHeadIndex() + index);
    }

    public void addObject(T object) {
        if (willOverflow()) {
            trimHead(this.trimSize);
        }
        this.internalArray.add(object);
    }

    public void trimHead(int count) {
        this.internalArray.trimHeadIndex(count);
    }

    public void clear() {
        this.internalArray.clear();
    }

    public Iterator<T> iterator() {
        return this.internalArray.iterator();
    }

    public int capacity() {
        return this.internalArray.getCapacity();
    }

    public int totalCount() {
        return this.internalArray.length();
    }

    public int count() {
        return this.internalArray.realLength();
    }

    public int getTrimSize() {
        return this.trimSize;
    }

    public int overflowCount() {
        return this.internalArray.getHeadIndex();
    }

    public boolean isOverfloating() {
        return this.internalArray.getHeadIndex() > 0 && willOverflow();
    }

    public boolean willOverflow() {
        return this.internalArray.realLength() == this.internalArray.getCapacity();
    }

    public boolean isTrimmed() {
        return trimmedCount() > 0;
    }

    public int trimmedCount() {
        return totalCount() - count();
    }
}
