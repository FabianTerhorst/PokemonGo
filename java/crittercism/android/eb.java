package crittercism.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class eb {
    public static void a(File file) {
        if (file.getAbsolutePath().contains("com.crittercism")) {
            if (file.isDirectory()) {
                for (File a : file.listFiles()) {
                    a(a);
                }
            }
            file.delete();
        }
    }

    public static String b(File file) {
        InputStreamReader inputStreamReader;
        Throwable th;
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream fileInputStream = new FileInputStream(file);
            try {
                inputStreamReader = new InputStreamReader(fileInputStream);
                while (true) {
                    try {
                        int read = inputStreamReader.read();
                        if (read != -1) {
                            stringBuilder.append((char) read);
                        } else {
                            fileInputStream.close();
                            inputStreamReader.close();
                            return stringBuilder.toString();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        inputStream = fileInputStream;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                inputStreamReader = null;
                inputStream = fileInputStream;
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            inputStreamReader = null;
            if (inputStream != null) {
                inputStream.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            throw th;
        }
    }
}
