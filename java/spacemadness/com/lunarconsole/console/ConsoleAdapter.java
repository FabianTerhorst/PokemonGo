package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import spacemadness.com.lunarconsole.R;

public class ConsoleAdapter extends BaseAdapter {
    private DataSource dataSource;

    public interface DataSource {
        ConsoleEntry getEntry(int i);

        int getEntryCount();
    }

    public static abstract class ViewHolder<T extends ConsoleEntry> {
        protected final View itemView;

        public abstract void onBindViewHolder(T t);

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        void bindViewHolder(ConsoleEntry entry) {
            onBindViewHolder(entry);
        }

        protected Context getContext() {
            return this.itemView.getContext();
        }

        protected Resources getResources() {
            return getContext().getResources();
        }

        protected String getString(int id) {
            return getResources().getString(id);
        }

        protected int getColor(int id) {
            return getResources().getColor(id);
        }
    }

    public ConsoleAdapter(DataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("Data source is null");
        }
        this.dataSource = dataSource;
    }

    public int getCount() {
        return this.dataSource.getEntryCount();
    }

    public Object getItem(int position) {
        return this.dataSource.getEntry(position);
    }

    public long getItemId(int position) {
        return (long) this.dataSource.getEntry(position).type;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lunar_layout_console_log_entry, parent, false);
            viewHolder = new spacemadness.com.lunarconsole.console.ConsoleEntry.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bindViewHolder(this.dataSource.getEntry(position));
        return convertView;
    }
}
