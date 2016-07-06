package com.crittercism.integrations;

import crittercism.android.dx;
import spacemadness.com.lunarconsole.utils.StackTrace;

public class PluginException extends Throwable {
    private static final String EMPTY_STRING = "";
    private static final long serialVersionUID = -1947260712494608235L;
    private String exceptionName;

    public PluginException(String msg, String stack) {
        this(EMPTY_STRING, msg, stack);
    }

    public PluginException(String name, String msg, String stack) {
        super(msg);
        this.exceptionName = null;
        setExceptionName(checkString(name), checkString(msg));
        setStackTrace(createStackTraceArrayFromStack(checkStringStack(stack)));
    }

    public static PluginException createUnityException(String message, String stack) {
        PluginException pluginException = null;
        try {
            pluginException = unsafeCreateUnityException(message, stack);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a("Unable to log unity exception <" + message + "> " + stack, th);
        }
        return pluginException;
    }

    private static PluginException unsafeCreateUnityException(String message, String stack) {
        String trim;
        if (message == null) {
            message = EMPTY_STRING;
        }
        if (stack == null) {
            stack = EMPTY_STRING;
        }
        int indexOf = message.indexOf(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
        if (indexOf != -1) {
            trim = message.substring(0, indexOf).trim();
            if (indexOf + 1 < message.length()) {
                message = message.substring(indexOf + 1).trim();
            }
        } else {
            trim = message;
        }
        PluginException pluginException = new PluginException(message);
        pluginException.exceptionName = trim;
        stack = stack.trim();
        if (stack.length() == 0) {
            pluginException.setStackTrace(new StackTraceElement[0]);
            return pluginException;
        }
        String[] split = stack.split("\\r?\\n");
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[split.length];
        for (int i = 0; i < split.length; i++) {
            String trim2 = split[i].trim();
            if (trim2.length() != 0) {
                String str = trim2.split(" ")[0];
                int lastIndexOf = str.lastIndexOf(".");
                if (lastIndexOf == -1) {
                    dx.b("Unable to parse unity exception.  No class and method found for frame frame <" + trim2 + ">" + stack);
                    return null;
                } else if (lastIndexOf == str.length() - 1) {
                    dx.b("Unable to parse unity exception.  Method is zero length for frame <" + trim2 + ">" + stack);
                    return null;
                } else {
                    String substring = str.substring(0, lastIndexOf);
                    String substring2 = str.substring(lastIndexOf + 1);
                    String str2 = null;
                    indexOf = -1;
                    int indexOf2 = trim2.indexOf(StackTrace.MARKER_AT);
                    if (indexOf2 != -1) {
                        trim2 = trim2.substring(indexOf2 + 5, split[i].length() - 1);
                        Object split2 = trim2.split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
                        if (split2.length >= 2) {
                            str2 = split2[0];
                            try {
                                String str3 = split2[split2.length - 1];
                                indexOf = Integer.parseInt(str3);
                                str2 = trim2.substring(0, (trim2.length() - str3.length()) - 1);
                            } catch (NumberFormatException e) {
                                new StringBuilder("Couldn't parse integer: ").append(split2[1]);
                                dx.a();
                            }
                        } else {
                            new StringBuilder("Unable to parse file & line out of Unity stack trace frame: ").append(split2).append(" ::: ").append(split[i]);
                            dx.b();
                        }
                    }
                    stackTraceElementArr[i] = new StackTraceElement(substring, substring2, str2, indexOf);
                }
            }
        }
        pluginException.setStackTrace(stackTraceElementArr);
        return pluginException;
    }

    private PluginException(String reason) {
        super(reason);
        this.exceptionName = null;
    }

    public PluginException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        super(msg);
        this.exceptionName = null;
        setExceptionName(checkString(name), checkString(msg));
        setStackTrace(createStackTraceArrayFromStack(classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public void setExceptionName(String name, String str) {
        if (name.length() > 0) {
            this.exceptionName = name;
        } else {
            this.exceptionName = "JavaScript Exception";
        }
    }

    private StackTraceElement[] createStackTraceArrayFromStack(String[] stack) {
        int i;
        StackTraceElement[] stackTraceElementArr;
        int i2 = 0;
        if (stack.length < 2 || stack[0] == null || stack[1] == null || !stack[0].equals(stack[1])) {
            i = 0;
            stackTraceElementArr = null;
        } else {
            i = 1;
            stackTraceElementArr = new StackTraceElement[(stack.length - 1)];
        }
        if (i == 0) {
            stackTraceElementArr = new StackTraceElement[stack.length];
        }
        while (i2 < stack.length) {
            if (i2 != 0 || i == 0) {
                int i3;
                if (i != 0) {
                    i3 = i2 - 1;
                } else {
                    i3 = i2;
                }
                stackTraceElementArr[i3] = new StackTraceElement(EMPTY_STRING, stack[i2], EMPTY_STRING, -1);
            }
            i2++;
        }
        return stackTraceElementArr;
    }

    private StackTraceElement[] createStackTraceArrayFromStack(String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        int length = classStackElems.length;
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[length];
        for (int i = 0; i < length; i++) {
            int i2 = lineNumberStackElems[i];
            if (i2 <= 0) {
                i2 = -1;
            }
            stackTraceElementArr[i] = new StackTraceElement(checkString(classStackElems[i]), checkString(methodStackElems[i]), checkString(fileStackElems[i]), i2);
        }
        return stackTraceElementArr;
    }

    private static String checkString(String s) {
        if (s == null) {
            return EMPTY_STRING;
        }
        return s;
    }

    private static String[] checkStringStack(String stack) {
        if (stack == null) {
            return new String[0];
        }
        return stack.split("\\r?\\n");
    }

    public final String toString() {
        String localizedMessage = getLocalizedMessage();
        String exceptionName = getExceptionName();
        return localizedMessage == null ? exceptionName : exceptionName + ": " + localizedMessage;
    }
}
