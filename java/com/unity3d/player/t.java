package com.unity3d.player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;

final class t {
    public static t a;
    private final ViewGroup b;
    private Set c = new HashSet();
    private View d;
    private View e;

    t(ViewGroup viewGroup) {
        this.b = viewGroup;
        a = this;
    }

    private void e(View view) {
        this.b.addView(view, this.b.getChildCount());
    }

    private void f(View view) {
        this.b.removeView(view);
        this.b.requestLayout();
    }

    public final Context a() {
        return this.b.getContext();
    }

    public final void a(View view) {
        this.c.add(view);
        if (this.d != null) {
            e(view);
        }
    }

    public final void b(View view) {
        this.c.remove(view);
        if (this.d != null) {
            f(view);
        }
    }

    public final void c(View view) {
        if (this.d != view) {
            this.d = view;
            this.b.addView(view);
            for (View e : this.c) {
                e(e);
            }
            if (this.e != null) {
                this.e.setVisibility(4);
            }
        }
    }

    public final void d(View view) {
        if (this.d == view) {
            for (View f : this.c) {
                f(f);
            }
            this.b.removeView(view);
            this.d = null;
            if (this.e != null) {
                this.e.setVisibility(0);
            }
        }
    }
}
