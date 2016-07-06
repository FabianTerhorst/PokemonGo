package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.WebView;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.message.BasicLineParser;
import org.apache.http.util.CharArrayBuffer;

public abstract class ak extends af {
    boolean d = false;
    int e;
    boolean f = false;
    private boolean g = false;
    private boolean h = false;

    protected abstract af g();

    public ak(af afVar) {
        super(afVar);
    }

    public final af b() {
        if (this.h) {
            return g();
        }
        this.b.clear();
        return this;
    }

    public final af c() {
        this.b.clear();
        return new ar(this);
    }

    public final boolean a(CharArrayBuffer charArrayBuffer) {
        int length = this.b.length();
        length = (length == 0 || (length == 1 && this.b.charAt(0) == '\r')) ? true : 0;
        if (length != 0) {
            this.h = true;
            return true;
        }
        try {
            Header parseHeader = BasicLineParser.DEFAULT.parseHeader(charArrayBuffer);
            if (!this.d && parseHeader.getName().equalsIgnoreCase("content-length")) {
                length = Integer.parseInt(parseHeader.getValue());
                if (length < 0) {
                    return false;
                }
                this.d = true;
                this.e = length;
                return true;
            } else if (parseHeader.getName().equalsIgnoreCase("transfer-encoding")) {
                this.f = parseHeader.getValue().equalsIgnoreCase("chunked");
                return true;
            } else if (this.g || !parseHeader.getName().equalsIgnoreCase(WebView.HOST)) {
                return true;
            } else {
                String value = parseHeader.getValue();
                if (value == null) {
                    return true;
                }
                this.g = true;
                this.a.a(value);
                return true;
            }
        } catch (ParseException e) {
            return false;
        } catch (NumberFormatException e2) {
            return false;
        }
    }

    protected final int d() {
        return 32;
    }

    protected final int e() {
        return 128;
    }
}
