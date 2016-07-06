package spacemadness.com.lunarconsole.console;

import java.util.Iterator;
import spacemadness.com.lunarconsole.utils.ObjectUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class ConsoleEntryList {
    private LimitSizeEntryList currentEntries = this.entries;
    private final LimitSizeEntryList entries;
    private int errorCount;
    private String filterText;
    private LimitSizeEntryList filteredEntries;
    private int logCount;
    private int logDisabledTypesMask = 0;
    private int warningCount;

    private static class LimitSizeEntryList extends LimitSizeList<ConsoleEntry> {
        public LimitSizeEntryList(int capacity, int trimSize) {
            super(ConsoleEntry.class, capacity, trimSize);
        }
    }

    public ConsoleEntryList(int capacity, int trimSize) {
        this.entries = new LimitSizeEntryList(capacity, trimSize);
    }

    public boolean filterEntry(ConsoleEntry entry) {
        if (!isFiltering()) {
            return true;
        }
        if (!isFiltered(entry)) {
            return false;
        }
        this.filteredEntries.addObject(entry);
        return true;
    }

    public void addEntry(ConsoleEntry entry) {
        this.entries.addObject(entry);
        int entryType = entry.type;
        if (entryType == 3) {
            this.logCount++;
        } else if (entryType == 2) {
            this.warningCount++;
        } else if (ConsoleLogType.isErrorType(entryType)) {
            this.errorCount++;
        }
    }

    public ConsoleEntry getEntry(int index) {
        return (ConsoleEntry) this.currentEntries.objectAtIndex(index);
    }

    public void trimHead(int count) {
        this.entries.trimHead(count);
    }

    public void clear() {
        this.entries.clear();
        if (this.filteredEntries != null) {
            this.filteredEntries.clear();
        }
        this.logCount = 0;
        this.warningCount = 0;
        this.errorCount = 0;
    }

    public boolean setFilterByText(String text) {
        if (ObjectUtils.areEqual(this.filterText, text)) {
            return false;
        }
        String oldFilterText = this.filterText;
        this.filterText = text;
        if (StringUtils.length(text) <= StringUtils.length(oldFilterText) || (StringUtils.length(oldFilterText) != 0 && !StringUtils.hasPrefix(text, oldFilterText))) {
            return applyFilter();
        }
        return appendFilter();
    }

    public boolean setFilterByLogType(int logType, boolean disabled) {
        return setFilterByLogTypeMask(ConsoleLogType.getMask(logType), disabled);
    }

    public boolean setFilterByLogTypeMask(int logTypeMask, boolean disabled) {
        int oldDisabledTypesMask = this.logDisabledTypesMask;
        if (disabled) {
            this.logDisabledTypesMask |= logTypeMask;
        } else {
            this.logDisabledTypesMask &= logTypeMask ^ -1;
        }
        if (oldDisabledTypesMask != this.logDisabledTypesMask) {
            return disabled ? appendFilter() : applyFilter();
        } else {
            return false;
        }
    }

    public boolean isFilterLogTypeEnabled(int type) {
        return (this.logDisabledTypesMask & ConsoleLogType.getMask(type)) == 0;
    }

    private boolean appendFilter() {
        if (!isFiltering()) {
            return applyFilter();
        }
        useFilteredFromEntries(this.filteredEntries);
        return true;
    }

    private boolean applyFilter() {
        boolean filtering = StringUtils.length(this.filterText) > 0 || hasLogTypeFilters();
        if (!filtering) {
            return removeFilter();
        }
        useFilteredFromEntries(this.entries);
        return true;
    }

    private boolean removeFilter() {
        if (!isFiltering()) {
            return false;
        }
        this.currentEntries = this.entries;
        this.filteredEntries = null;
        return true;
    }

    private void useFilteredFromEntries(LimitSizeEntryList entries) {
        LimitSizeEntryList filteredEntries = filterEntries(entries);
        this.currentEntries = filteredEntries;
        this.filteredEntries = filteredEntries;
    }

    private LimitSizeEntryList filterEntries(LimitSizeEntryList entries) {
        LimitSizeEntryList list = new LimitSizeEntryList(entries.capacity(), entries.getTrimSize());
        Iterator i$ = entries.iterator();
        while (i$.hasNext()) {
            ConsoleEntry entry = (ConsoleEntry) i$.next();
            if (isFiltered(entry)) {
                list.addObject(entry);
            }
        }
        return list;
    }

    private boolean isFiltered(ConsoleEntry entry) {
        if ((this.logDisabledTypesMask & ConsoleLogType.getMask(entry.type)) != 0) {
            return false;
        }
        if (StringUtils.length(this.filterText) == 0 || StringUtils.containsIgnoreCase(entry.message, this.filterText)) {
            return true;
        }
        return false;
    }

    private boolean hasLogTypeFilters() {
        return this.logDisabledTypesMask != 0;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        int index = 0;
        int count = this.currentEntries.count();
        Iterator i$ = this.currentEntries.iterator();
        while (i$.hasNext()) {
            text.append(((ConsoleEntry) i$.next()).message);
            index++;
            if (index < count) {
                text.append('\n');
            }
        }
        return text.toString();
    }

    public int count() {
        return this.currentEntries.count();
    }

    public int totalCount() {
        return this.currentEntries.totalCount();
    }

    public int getLogCount() {
        return this.logCount;
    }

    public int getWarningCount() {
        return this.warningCount;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public String getFilterText() {
        return this.filterText;
    }

    public boolean isFiltering() {
        return this.filteredEntries != null;
    }

    public int overflowAmount() {
        return this.currentEntries.overflowCount();
    }

    public boolean isOverfloating() {
        return this.currentEntries.isOverfloating();
    }

    public int trimmedCount() {
        return this.currentEntries.trimmedCount();
    }

    public boolean isTrimmed() {
        return this.currentEntries.isTrimmed();
    }
}
