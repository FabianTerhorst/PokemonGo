package crittercism.android;

import java.io.OutputStream;
import org.json.JSONArray;

public abstract class ci extends bp {
    public abstract JSONArray a();

    public final void a(OutputStream outputStream) {
        String jSONArray = a().toString();
        new StringBuilder("BREADCRUMB WRITING ").append(jSONArray);
        dx.b();
        outputStream.write(jSONArray.getBytes());
    }
}
