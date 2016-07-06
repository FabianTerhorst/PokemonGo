package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import spacemadness.com.lunarconsole.R;

public class ConsoleEntry {
    private static final int[] LOG_ENTRY_ICON_RES_LOOKUP = new int[5];
    public int index;
    public final String message;
    public final String stackTrace;
    public final byte type;

    public static class ViewHolder extends spacemadness.com.lunarconsole.console.ConsoleAdapter.ViewHolder<ConsoleEntry> {
        private final ImageView iconView;
        private final View layout;
        private final TextView messageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.lunar_console_log_entry_layout);
            this.iconView = (ImageView) itemView.findViewById(R.id.lunar_console_log_entry_icon);
            this.messageView = (TextView) itemView.findViewById(R.id.lunar_console_log_entry_message);
        }

        public void onBindViewHolder(ConsoleEntry entry) {
            Context context = getContext();
            this.layout.setBackgroundColor(entry.getBackgroundColor(context));
            this.iconView.setImageDrawable(entry.getIconDrawable(context));
            this.messageView.setText(entry.message);
        }
    }

    static {
        LOG_ENTRY_ICON_RES_LOOKUP[0] = R.drawable.lunar_console_icon_log_error;
        LOG_ENTRY_ICON_RES_LOOKUP[1] = R.drawable.lunar_console_icon_log_error;
        LOG_ENTRY_ICON_RES_LOOKUP[2] = R.drawable.lunar_console_icon_log_warning;
        LOG_ENTRY_ICON_RES_LOOKUP[3] = R.drawable.lunar_console_icon_log;
        LOG_ENTRY_ICON_RES_LOOKUP[4] = R.drawable.lunar_console_icon_log_error;
    }

    public ConsoleEntry(byte type, String message) {
        this(type, message, null);
    }

    public ConsoleEntry(byte type, String message, String stackTrace) {
        this.type = type;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public Drawable getIconDrawable(Context context) {
        return context.getResources().getDrawable(getIconResId(this.type));
    }

    public int getBackgroundColor(Context context) {
        return context.getResources().getColor(this.index % 2 == 0 ? R.color.lunar_console_color_cell_background_dark : R.color.lunar_console_color_cell_background_light);
    }

    public boolean hasStackTrace() {
        return this.stackTrace != null && this.stackTrace.length() > 0;
    }

    private int getIconResId(int type) {
        return (type < 0 || type >= LOG_ENTRY_ICON_RES_LOOKUP.length) ? R.drawable.lunar_console_icon_log : LOG_ENTRY_ICON_RES_LOOKUP[type];
    }
}
