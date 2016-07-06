package spacemadness.com.lunarconsole.debug;

public class Tag {
    public boolean enabled;
    public final String name;

    public Tag(String name) {
        this(name, false);
    }

    public Tag(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
