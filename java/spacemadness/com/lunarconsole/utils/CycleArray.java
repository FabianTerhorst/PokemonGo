package spacemadness.com.lunarconsole.utils;

import java.lang.reflect.Array;
import java.util.Iterator;

public class CycleArray<E> implements Iterable<E> {
    private final Class<? extends E> componentType;
    private int headIndex;
    private E[] internalArray;
    private int length;

    private class CycleIterator implements Iterator<E> {
        private int index;

        public CycleIterator() {
            this.index = CycleArray.this.getHeadIndex();
        }

        public boolean hasNext() {
            return this.index < CycleArray.this.length();
        }

        public E next() {
            CycleArray cycleArray = CycleArray.this;
            int i = this.index;
            this.index = i + 1;
            return cycleArray.get(i);
        }

        public void remove() {
            throw new NotImplementedException();
        }
    }

    public CycleArray(Class<? extends E> componentType, int capacity) {
        if (componentType == null) {
            throw new NullPointerException("Component type is null");
        }
        this.componentType = componentType;
        this.internalArray = (Object[]) Array.newInstance(componentType, capacity);
    }

    public E add(E e) {
        int arrayIndex = toArrayIndex(this.length);
        E oldItem = this.internalArray[arrayIndex];
        this.internalArray[arrayIndex] = e;
        this.length++;
        if (this.length - this.headIndex <= this.internalArray.length) {
            return null;
        }
        this.headIndex++;
        return oldItem;
    }

    public void clear() {
        for (int i = 0; i < this.internalArray.length; i++) {
            this.internalArray[i] = null;
        }
        this.length = 0;
        this.headIndex = 0;
    }

    public void trimLength(int trimSize) {
        trimToLength(this.length - trimSize);
    }

    public void trimToLength(int trimmedLength) {
        if (trimmedLength < this.headIndex || trimmedLength > this.length) {
            throw new IllegalArgumentException("Trimmed length " + trimmedLength + " should be between head index " + this.headIndex + " and length " + this.length);
        }
        this.length = trimmedLength;
    }

    public void trimHeadIndex(int trimSize) {
        trimToHeadIndex(this.headIndex + trimSize);
    }

    public void trimToHeadIndex(int trimmedHeadIndex) {
        if (trimmedHeadIndex < this.headIndex || trimmedHeadIndex > this.length) {
            throw new IllegalArgumentException("Trimmed head index " + trimmedHeadIndex + " should be between head index " + this.headIndex + " and length " + this.length);
        }
        this.headIndex = trimmedHeadIndex;
    }

    public E get(int index) {
        return this.internalArray[toArrayIndex(index)];
    }

    public void set(int index, E value) {
        this.internalArray[toArrayIndex(index)] = value;
    }

    public int toArrayIndex(int i) {
        return i % this.internalArray.length;
    }

    public boolean isValidIndex(int index) {
        return index >= this.headIndex && index < this.length;
    }

    private int toArrayIndex(E[] array, int i) {
        return i % array.length;
    }

    public int getCapacity() {
        return this.internalArray.length;
    }

    public void setCapacity(int value) {
        if (value > getCapacity()) {
            Object[] data = (Object[]) ((Object[]) Array.newInstance(this.componentType, value));
            int totalCopyLength = realLength();
            int fromIndex = toArrayIndex(this.internalArray, this.headIndex);
            int toIndex = toArrayIndex(data, this.headIndex);
            while (totalCopyLength > 0) {
                int copyLength = Math.min(totalCopyLength, Math.min(this.internalArray.length - fromIndex, data.length - toIndex));
                System.arraycopy(this.internalArray, fromIndex, data, toIndex, copyLength);
                totalCopyLength -= copyLength;
                fromIndex = toArrayIndex(this.internalArray, fromIndex + copyLength);
                toIndex = toArrayIndex(data, toIndex + copyLength);
            }
            this.internalArray = data;
        } else if (value < getCapacity()) {
            throw new NotImplementedException();
        }
    }

    public boolean contains(Object element) {
        for (int i = this.headIndex; i < this.length; i++) {
            if (ObjectUtils.areEqual(this.internalArray[toArrayIndex(i)], element)) {
                return true;
            }
        }
        return false;
    }

    public int getHeadIndex() {
        return this.headIndex;
    }

    public int length() {
        return this.length;
    }

    public int realLength() {
        return this.length - this.headIndex;
    }

    public E[] internalArray() {
        return this.internalArray;
    }

    public Iterator<E> iterator() {
        return new CycleIterator();
    }
}
