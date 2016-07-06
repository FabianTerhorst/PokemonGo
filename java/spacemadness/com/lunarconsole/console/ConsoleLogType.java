package spacemadness.com.lunarconsole.console;

public final class ConsoleLogType {
    public static final byte ASSERT = (byte) 1;
    public static final byte COUNT = (byte) 5;
    public static final byte ERROR = (byte) 0;
    public static final byte EXCEPTION = (byte) 4;
    public static final byte LOG = (byte) 3;
    public static final byte WARNING = (byte) 2;

    public static boolean isErrorType(int type) {
        return type == 4 || type == 0 || type == 1;
    }

    public static boolean isValidType(int type) {
        return type >= 0 && type < 5;
    }

    public static int getMask(int type) {
        return 1 << type;
    }

    private ConsoleLogType() {
    }
}
