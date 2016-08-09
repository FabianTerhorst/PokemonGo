package com.upsight.mediation.log;

import java.util.ArrayList;

public class LogBuffer {
    public final String[] buffer;
    public final int bufferSize;
    public int end;
    public final int msgLength;

    public LogBuffer(int bufferSize, int msgLength) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        } else if (msgLength <= 0) {
            throw new IllegalArgumentException("Message length must be greater than 0");
        } else {
            this.bufferSize = bufferSize;
            this.buffer = new String[bufferSize];
            this.msgLength = msgLength;
            this.end = -1;
        }
    }

    public void append(String level, String tag, String s) {
        append("," + tag + "," + s);
    }

    public void append(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value may not be null");
        }
        int i = this.end + 1;
        this.end = i;
        if (i >= this.bufferSize) {
            this.end = 0;
        }
        if (value.length() > this.msgLength) {
            this.buffer[this.end] = value.substring(0, this.msgLength);
        } else {
            this.buffer[this.end] = value;
        }
    }

    public String[] getLog() {
        int startIndex = getStartIndex();
        ArrayList<String> logs = new ArrayList();
        for (int i = 0; i < this.bufferSize; i++) {
            String currentLog = this.buffer[(startIndex + i) % this.bufferSize];
            if (currentLog == null) {
                break;
            }
            logs.add(currentLog);
        }
        return (String[]) logs.toArray(new String[logs.size()]);
    }

    public int getStartIndex() {
        int start;
        if (this.buffer[this.buffer.length - 1] == null) {
            start = 0;
        } else {
            start = this.end + 1;
        }
        if (start >= this.bufferSize) {
            return 0;
        }
        return start;
    }
}
