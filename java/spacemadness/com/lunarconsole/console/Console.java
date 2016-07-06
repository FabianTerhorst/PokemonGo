package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.console.ConsoleAdapter.DataSource;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;

public class Console implements DataSource, Destroyable {
    private static final LunarConsoleListener NULL_LISTENER = new LunarConsoleListener() {
        public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered) {
        }

        public void onRemoveEntries(Console console, int start, int length) {
        }

        public void onClearEntries(Console console) {
        }
    };
    private LunarConsoleListener consoleListener;
    private final ConsoleEntryList entries;
    private final Options options;

    public static class Options {
        private final int capacity;
        private int trimCount;

        public Options(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Invalid capacity: " + capacity);
            }
            this.capacity = capacity;
            this.trimCount = 1;
        }

        public Options clone() {
            Options options = new Options(this.capacity);
            options.trimCount = this.trimCount;
            return options;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public int getTrimCount() {
            return this.trimCount;
        }

        public void setTrimCount(int count) {
            if (count <= 0 || count > this.capacity) {
                throw new IllegalArgumentException("Illegal trim count: " + count);
            }
            this.trimCount = count;
        }
    }

    public Console(Options options) {
        if (options == null) {
            throw new NullPointerException("Options is null");
        }
        this.options = options.clone();
        this.entries = new ConsoleEntryList(options.getCapacity(), options.getTrimCount());
        this.consoleListener = NULL_LISTENER;
    }

    public ConsoleEntry entryAtIndex(int index) {
        return this.entries.getEntry(index);
    }

    public void logMessage(String message, String stackTrace, byte type) {
        logMessage(new ConsoleEntry(type, message, stackTrace));
    }

    void logMessage(ConsoleEntry entry) {
        int oldTrimmedCount = this.entries.trimmedCount();
        entry.index = this.entries.totalCount();
        boolean filtered = this.entries.filterEntry(entry);
        this.entries.addEntry(entry);
        int trimmedCount = this.entries.trimmedCount() - oldTrimmedCount;
        if (trimmedCount > 0) {
            notifyRemoveEntries(0, trimmedCount);
        }
        notifyEntryAdded(entry, filtered);
    }

    public void clear() {
        this.entries.clear();
        notifyEntriesCleared();
    }

    public void destroy() {
        this.entries.clear();
    }

    private void notifyEntryAdded(ConsoleEntry entry, boolean filtered) {
        try {
            this.consoleListener.onAddEntry(this, entry, filtered);
        } catch (Throwable e) {
            Log.e(e, "Error while notifying delegate", new Object[0]);
        }
    }

    private void notifyRemoveEntries(int start, int length) {
        try {
            this.consoleListener.onRemoveEntries(this, start, length);
        } catch (Throwable e) {
            Log.e(e, "Error while notifying delegate", new Object[0]);
        }
    }

    private void notifyEntriesCleared() {
        try {
            this.consoleListener.onClearEntries(this);
        } catch (Throwable e) {
            Log.e(e, "Error while notifying delegate", new Object[0]);
        }
    }

    public String getText() {
        return this.entries.getText();
    }

    public LunarConsoleListener getConsoleListener() {
        return this.consoleListener;
    }

    public void setConsoleListener(LunarConsoleListener listener) {
        if (listener == null) {
            listener = NULL_LISTENER;
        }
        this.consoleListener = listener;
    }

    public ConsoleEntryList entries() {
        return this.entries;
    }

    public int entriesCount() {
        return this.entries.count();
    }

    public int trimmedCount() {
        return this.entries.trimmedCount();
    }

    public boolean isTrimmed() {
        return this.entries.isTrimmed();
    }

    public ConsoleEntry getEntry(int position) {
        return this.entries.getEntry(position);
    }

    public int getEntryCount() {
        return this.entries.count();
    }
}
