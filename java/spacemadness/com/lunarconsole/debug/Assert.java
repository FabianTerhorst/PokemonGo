package spacemadness.com.lunarconsole.debug;

import java.util.Collection;
import java.util.List;
import spacemadness.com.lunarconsole.utils.ObjectUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class Assert {
    private static boolean IsEnabled = true;

    public static void IsTrue(boolean condition) {
        if (IsEnabled && !condition) {
            AssertHelper("Assertion failed: 'true' expected", new Object[0]);
        }
    }

    public static void IsTrue(boolean condition, String message) {
        if (IsEnabled && !condition) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsTrue(boolean condition, String format, Object... args) {
        if (IsEnabled && !condition) {
            AssertHelper(format, args);
        }
    }

    public static void IsTrue(boolean condition, String format, Object arg0) {
        if (IsEnabled && !condition) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsTrue(boolean condition, String format, Object arg0, Object arg1) {
        if (IsEnabled && !condition) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsTrue(boolean condition, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && !condition) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsFalse(boolean condition) {
        if (IsEnabled && condition) {
            AssertHelper("Assertion failed: 'false' expected", new Object[0]);
        }
    }

    public static void IsFalse(boolean condition, String message) {
        if (IsEnabled && condition) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsFalse(boolean condition, String format, Object... args) {
        if (IsEnabled && condition) {
            AssertHelper(format, args);
        }
    }

    public static void IsFalse(boolean condition, String format, Object arg0) {
        if (IsEnabled && condition) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsFalse(boolean condition, String format, Object arg0, Object arg1) {
        if (IsEnabled && condition) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsFalse(boolean condition, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && condition) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsNull(Object obj) {
        if (IsEnabled && obj != null) {
            AssertHelper("Assertion failed: expected 'null' but was '%s'", obj);
        }
    }

    public static void IsNull(Object obj, String message) {
        if (IsEnabled && obj != null) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsNull(Object obj, String format, Object... args) {
        if (IsEnabled && obj != null) {
            AssertHelper(format, args);
        }
    }

    public static void IsNull(Object obj, String format, Object arg0) {
        if (IsEnabled && obj != null) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsNull(Object obj, String format, Object arg0, Object arg1) {
        if (IsEnabled && obj != null) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsNull(Object obj, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && obj != null) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsNotNull(Object obj) {
        if (IsEnabled && obj == null) {
            AssertHelper("Assertion failed: Object is 'null'", new Object[0]);
        }
    }

    public static void IsNotNull(Object obj, String message) {
        if (IsEnabled && obj == null) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsNotNull(Object obj, String format, Object... args) {
        if (IsEnabled && obj == null) {
            AssertHelper(format, args);
        }
    }

    public static void IsNotNullElement(List<?> list) {
        if (IsEnabled) {
            int index = 0;
            for (Object t : list) {
                IsNotNull(t, "Element at %s is null", toString(Integer.valueOf(index)));
                index++;
            }
        }
    }

    public static void IsNotNull(Object obj, String format, Object arg0) {
        if (IsEnabled && obj == null) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsNotNull(Object obj, String format, Object arg0, Object arg1) {
        if (IsEnabled && obj == null) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsNotNull(Object obj, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && obj == null) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(boolean expected, boolean actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", Boolean.toString(expected), Boolean.toString(actual));
        }
    }

    public static void AreEqual(boolean expected, boolean actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(boolean expected, boolean actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(byte expected, byte actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Byte.valueOf(expected)), toString(Byte.valueOf(actual)));
        }
    }

    public static void AreEqual(byte expected, byte actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(byte expected, byte actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(byte expected, byte actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(byte expected, byte actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(byte expected, byte actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(short expected, short actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Short.valueOf(expected)), toString(Short.valueOf(actual)));
        }
    }

    public static void AreEqual(short expected, short actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(short expected, short actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(short expected, short actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(short expected, short actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(short expected, short actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(char expected, char actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Character.valueOf(expected)), toString(Character.valueOf(actual)));
        }
    }

    public static void AreEqual(char expected, char actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(char expected, char actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(char expected, char actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(char expected, char actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(char expected, char actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(int expected, int actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Integer.valueOf(expected)), toString(Integer.valueOf(actual)));
        }
    }

    public static void AreEqual(int expected, int actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(int expected, int actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(int expected, int actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(int expected, int actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(int expected, int actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(long expected, long actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Long.valueOf(expected)), toString(Long.valueOf(actual)));
        }
    }

    public static void AreEqual(long expected, long actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(long expected, long actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(long expected, long actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(long expected, long actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(long expected, long actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(float expected, float actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Float.valueOf(expected)), toString(Float.valueOf(actual)));
        }
    }

    public static void AreEqual(float expected, float actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(float expected, float actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(float expected, float actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(float expected, float actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(float expected, float actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(double expected, double actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(Double.valueOf(expected)), toString(Double.valueOf(actual)));
        }
    }

    public static void AreEqual(double expected, double actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(double expected, double actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(double expected, double actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(double expected, double actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(double expected, double actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreEqual(Object expected, Object actual) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper("Assertion failed: expected '%s' but was '%s'", toString(expected), toString(actual));
        }
    }

    public static void AreEqual(Object expected, Object actual, String message) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreEqual(Object expected, Object actual, String format, Object... args) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper(format, args);
        }
    }

    public static void AreEqual(Object expected, Object actual, String format, Object arg0) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreEqual(Object expected, Object actual, String format, Object arg0, Object arg1) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreEqual(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2) {
        if (!IsEnabled) {
            return;
        }
        if (expected != null && actual != null && expected.equals(actual)) {
            return;
        }
        if (expected != null || actual != null) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Boolean.valueOf(expected)));
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(byte expected, byte actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Byte.valueOf(expected)));
        }
    }

    public static void AreNotEqual(byte expected, byte actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(byte expected, byte actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(short expected, short actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Short.valueOf(expected)));
        }
    }

    public static void AreNotEqual(short expected, short actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(short expected, short actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(short expected, short actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(short expected, short actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(short expected, short actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(char expected, char actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Character.valueOf(expected)));
        }
    }

    public static void AreNotEqual(char expected, char actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(char expected, char actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(char expected, char actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(char expected, char actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(char expected, char actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(int expected, int actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Integer.valueOf(expected)));
        }
    }

    public static void AreNotEqual(int expected, int actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(int expected, int actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(int expected, int actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(int expected, int actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(int expected, int actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(long expected, long actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Long.valueOf(expected)));
        }
    }

    public static void AreNotEqual(long expected, long actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(long expected, long actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(long expected, long actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(long expected, long actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(long expected, long actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(float expected, float actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Float.valueOf(expected)));
        }
    }

    public static void AreNotEqual(float expected, float actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(float expected, float actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(float expected, float actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(float expected, float actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(float expected, float actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(double expected, double actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: values are equal '%s'", toString(Double.valueOf(expected)));
        }
    }

    public static void AreNotEqual(double expected, double actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(double expected, double actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(double expected, double actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(double expected, double actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(double expected, double actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotEqual(Object expected, Object actual) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper("Assertion failed: Objects are equal '%s'", toString(expected));
        }
    }

    public static void AreNotEqual(Object expected, Object actual, String message) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotEqual(Object expected, Object actual, String format, Object... args) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0, Object arg1) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2) {
        if (!IsEnabled) {
            return;
        }
        if ((expected != null && actual != null && expected.equals(actual)) || (expected == null && actual == null)) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(byte a, byte b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Byte.valueOf(a), Byte.valueOf(b));
        }
    }

    public static void Greater(byte a, byte b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(byte a, byte b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(byte a, byte b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(byte a, byte b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(byte a, byte b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(short a, short b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Short.valueOf(a), Short.valueOf(b));
        }
    }

    public static void Greater(short a, short b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(short a, short b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(short a, short b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(short a, short b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(short a, short b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(char a, char b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Character.valueOf(a), Character.valueOf(b));
        }
    }

    public static void Greater(char a, char b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(char a, char b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(char a, char b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(char a, char b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(char a, char b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(int a, int b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Integer.valueOf(a), Integer.valueOf(b));
        }
    }

    public static void Greater(int a, int b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(int a, int b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(int a, int b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(int a, int b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(int a, int b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(long a, long b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Long.valueOf(a), Long.valueOf(b));
        }
    }

    public static void Greater(long a, long b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(long a, long b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(long a, long b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(long a, long b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(long a, long b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(float a, float b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Float.valueOf(a), Float.valueOf(b));
        }
    }

    public static void Greater(float a, float b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(float a, float b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(float a, float b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(float a, float b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(float a, float b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Greater(double a, double b) {
        if (IsEnabled && a <= b) {
            AssertHelper("Assertion failed: '%s' > '%s'", Double.valueOf(a), Double.valueOf(b));
        }
    }

    public static void Greater(double a, double b, String message) {
        if (IsEnabled && a <= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Greater(double a, double b, String format, Object... args) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, args);
        }
    }

    public static void Greater(double a, double b, String format, Object arg0) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Greater(double a, double b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Greater(double a, double b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a <= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(byte a, byte b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Byte.valueOf(a), Byte.valueOf(b));
        }
    }

    public static void GreaterOrEqual(byte a, byte b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(byte a, byte b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(byte a, byte b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(byte a, byte b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(byte a, byte b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(short a, short b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Short.valueOf(a), Short.valueOf(b));
        }
    }

    public static void GreaterOrEqual(short a, short b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(short a, short b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(short a, short b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(short a, short b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(short a, short b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(char a, char b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Character.valueOf(a), Character.valueOf(b));
        }
    }

    public static void GreaterOrEqual(char a, char b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(char a, char b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(char a, char b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(char a, char b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(char a, char b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(int a, int b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Integer.valueOf(a), Integer.valueOf(b));
        }
    }

    public static void GreaterOrEqual(int a, int b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(int a, int b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(int a, int b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(int a, int b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(int a, int b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(long a, long b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Long.valueOf(a), Long.valueOf(b));
        }
    }

    public static void GreaterOrEqual(long a, long b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(long a, long b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(long a, long b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(long a, long b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(long a, long b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(float a, float b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Float.valueOf(a), Float.valueOf(b));
        }
    }

    public static void GreaterOrEqual(float a, float b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(float a, float b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(float a, float b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(float a, float b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(float a, float b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void GreaterOrEqual(double a, double b) {
        if (IsEnabled && a < b) {
            AssertHelper("Assertion failed: '%s' >= '%s'", Double.valueOf(a), Double.valueOf(b));
        }
    }

    public static void GreaterOrEqual(double a, double b, String message) {
        if (IsEnabled && a < b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void GreaterOrEqual(double a, double b, String format, Object... args) {
        if (IsEnabled && a < b) {
            AssertHelper(format, args);
        }
    }

    public static void GreaterOrEqual(double a, double b, String format, Object arg0) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0);
        }
    }

    public static void GreaterOrEqual(double a, double b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void GreaterOrEqual(double a, double b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a < b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(byte a, byte b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Byte.valueOf(a), Byte.valueOf(b));
        }
    }

    public static void Less(byte a, byte b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(byte a, byte b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(byte a, byte b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(byte a, byte b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(byte a, byte b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(short a, short b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Short.valueOf(a), Short.valueOf(b));
        }
    }

    public static void Less(short a, short b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(short a, short b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(short a, short b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(short a, short b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(short a, short b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(char a, char b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Character.valueOf(a), Character.valueOf(b));
        }
    }

    public static void Less(char a, char b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(char a, char b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(char a, char b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(char a, char b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(char a, char b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(int a, int b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Integer.valueOf(a), Integer.valueOf(b));
        }
    }

    public static void Less(int a, int b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(int a, int b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(int a, int b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(int a, int b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(int a, int b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(long a, long b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Long.valueOf(a), Long.valueOf(b));
        }
    }

    public static void Less(long a, long b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(long a, long b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(long a, long b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(long a, long b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(long a, long b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(float a, float b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Float.valueOf(a), Float.valueOf(b));
        }
    }

    public static void Less(float a, float b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(float a, float b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(float a, float b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(float a, float b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(float a, float b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void Less(double a, double b) {
        if (IsEnabled && a >= b) {
            AssertHelper("Assertion failed: '%s' < '%s'", Double.valueOf(a), Double.valueOf(b));
        }
    }

    public static void Less(double a, double b, String message) {
        if (IsEnabled && a >= b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Less(double a, double b, String format, Object... args) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, args);
        }
    }

    public static void Less(double a, double b, String format, Object arg0) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0);
        }
    }

    public static void Less(double a, double b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Less(double a, double b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a >= b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(byte a, byte b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Byte.valueOf(a), Byte.valueOf(b));
        }
    }

    public static void LessOrEqual(byte a, byte b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(byte a, byte b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(byte a, byte b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(byte a, byte b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(byte a, byte b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(short a, short b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Short.valueOf(a), Short.valueOf(b));
        }
    }

    public static void LessOrEqual(short a, short b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(short a, short b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(short a, short b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(short a, short b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(short a, short b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(char a, char b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Character.valueOf(a), Character.valueOf(b));
        }
    }

    public static void LessOrEqual(char a, char b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(char a, char b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(char a, char b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(char a, char b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(char a, char b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(int a, int b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Integer.valueOf(a), Integer.valueOf(b));
        }
    }

    public static void LessOrEqual(int a, int b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(int a, int b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(int a, int b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(int a, int b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(int a, int b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(long a, long b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Long.valueOf(a), Long.valueOf(b));
        }
    }

    public static void LessOrEqual(long a, long b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(long a, long b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(long a, long b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(long a, long b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(long a, long b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(float a, float b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Float.valueOf(a), Float.valueOf(b));
        }
    }

    public static void LessOrEqual(float a, float b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(float a, float b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(float a, float b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(float a, float b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(float a, float b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void LessOrEqual(double a, double b) {
        if (IsEnabled && a > b) {
            AssertHelper("Assertion failed: '%s' <= '%s'", Double.valueOf(a), Double.valueOf(b));
        }
    }

    public static void LessOrEqual(double a, double b, String message) {
        if (IsEnabled && a > b) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void LessOrEqual(double a, double b, String format, Object... args) {
        if (IsEnabled && a > b) {
            AssertHelper(format, args);
        }
    }

    public static void LessOrEqual(double a, double b, String format, Object arg0) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0);
        }
    }

    public static void LessOrEqual(double a, double b, String format, Object arg0, Object arg1) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void LessOrEqual(double a, double b, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && a > b) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreSame(Object expected, Object actual) {
        if (IsEnabled && expected != actual) {
            AssertHelper("Assertion failed: Object references are not the same '%s' but was '%s'", toString(expected), toString(actual));
        }
    }

    public static void AreSame(Object expected, Object actual, String message) {
        if (IsEnabled && expected != actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreSame(Object expected, Object actual, String format, Object... args) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreSame(Object expected, Object actual, String format, Object arg0) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreSame(Object expected, Object actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreSame(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected != actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void AreNotSame(Object expected, Object actual) {
        if (IsEnabled && expected == actual) {
            AssertHelper("Assertion failed: Object references are the same '%s'", toString(expected));
        }
    }

    public static void AreNotSame(Object expected, Object actual, String message) {
        if (IsEnabled && expected == actual) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void AreNotSame(Object expected, Object actual, String format, Object... args) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, args);
        }
    }

    public static void AreNotSame(Object expected, Object actual, String format, Object arg0) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0);
        }
    }

    public static void AreNotSame(Object expected, Object actual, String format, Object arg0, Object arg1) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void AreNotSame(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && expected == actual) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static <T> void Contains(T expected, Collection<T> collection) {
        if (!IsEnabled) {
            return;
        }
        if (collection != null && collection.contains(expected)) {
            return;
        }
        if (collection == null) {
            AssertHelper("Assertion failed: collection is null", new Object[0]);
            return;
        }
        AssertHelper("Assertion failed: collection doesn't contain the item %s", expected);
    }

    public static <T> void NotContains(T expected, Collection<T> collection) {
        if (!IsEnabled || collection == null || !collection.contains(expected)) {
            return;
        }
        if (collection == null) {
            AssertHelper("Assertion failed: collection is null", new Object[0]);
            return;
        }
        AssertHelper("Assertion failed: collection contains the item %s", expected);
    }

    public static void Fail() {
        if (IsEnabled) {
            AssertHelper("Assertion failed", new Object[0]);
        }
    }

    public static void Fail(String message) {
        if (IsEnabled) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void Fail(String format, Object... args) {
        if (IsEnabled) {
            AssertHelper(format, args);
        }
    }

    public static void Fail(String format, Object arg0) {
        if (IsEnabled) {
            AssertHelper(format, arg0);
        }
    }

    public static void Fail(String format, Object arg0, Object arg1) {
        if (IsEnabled) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void Fail(String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            String str = "Assertion failed: expected type of '%s' but was '%s'";
            Object[] objArr = new Object[2];
            objArr[0] = type;
            objArr[1] = o != null ? o.getClass() : null;
            AssertHelper(str, objArr);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o, String message) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o, String format, Object... args) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            AssertHelper(format, args);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o, String format, Object arg0) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o, String format, Object arg0, Object arg1) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsInstanceOfType(Class<?> type, Object o, String format, Object arg0, Object arg1, Object arg2) {
        if (!IsEnabled) {
            return;
        }
        if (type == null || !type.isInstance(o)) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            String str = "Assertion failed: Object '%s' is subtype of '%s'";
            Object[] objArr = new Object[2];
            objArr[0] = type;
            objArr[1] = o != null ? o.getClass() : null;
            AssertHelper(str, objArr);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o, String message) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o, String format, Object... args) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            AssertHelper(format, args);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o, String format, Object arg0) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o, String format, Object arg0, Object arg1) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsNotInstanceOfType(Class<?> type, Object o, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && type != null && type.isInstance(o)) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsEmpty(String str) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper("Assertion failed: String is not empty '%s'", str);
        }
    }

    public static void IsEmpty(String str, String message) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsEmpty(String str, String format, Object... args) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, args);
        }
    }

    public static void IsEmpty(String str, String format, Object arg0) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsEmpty(String str, String format, Object arg0, Object arg1) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsEmpty(String str, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && !StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsNotEmpty(String str) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper("Assertion failed: String is null or empty '%s'", str);
        }
    }

    public static void IsNotEmpty(String str, String message) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsNotEmpty(String str, String format, Object... args) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, args);
        }
    }

    public static void IsNotEmpty(String str, String format, Object arg0) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsNotEmpty(String str, String format, Object arg0, Object arg1) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsNotEmpty(String str, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && StringUtils.IsNullOrEmpty(str)) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsEmpty(Collection<?> collection) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper("Assertion failed: collection is null or not empty '%s'", collection);
        }
    }

    public static void IsEmpty(Collection<?> collection, String message) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsEmpty(Collection<?> collection, String format, Object... args) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper(format, args);
        }
    }

    public static void IsEmpty(Collection<?> collection, String format, Object arg0) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsEmpty(Collection<?> collection, String format, Object arg0, Object arg1) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsEmpty(Collection<?> collection, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && collection != null && collection.size() == 0) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    public static void IsNotEmpty(Collection<?> collection) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper("Assertion failed: collection is null or empty '%s'", collection);
        }
    }

    public static void IsNotEmpty(Collection<?> collection, String message) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper(message, new Object[0]);
        }
    }

    public static void IsNotEmpty(Collection<?> collection, String format, Object... args) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper(format, args);
        }
    }

    public static void IsNotEmpty(Collection<?> collection, String format, Object arg0) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper(format, arg0);
        }
    }

    public static void IsNotEmpty(Collection<?> collection, String format, Object arg0, Object arg1) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper(format, arg0, arg1);
        }
    }

    public static void IsNotEmpty(Collection<?> collection, String format, Object arg0, Object arg1, Object arg2) {
        if (IsEnabled && collection != null && collection.size() != 0) {
            AssertHelper(format, arg0, arg1, arg2);
        }
    }

    private static void AssertHelper(String format, Object... args) {
        throw new AssertionError(StringUtils.TryFormat(format, args));
    }

    private static String toString(Object value) {
        return ObjectUtils.toString(value);
    }
}
