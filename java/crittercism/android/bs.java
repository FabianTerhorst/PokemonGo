package crittercism.android;

import android.content.Context;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class bs {
    public final File a;
    public String b;
    public List c;
    private cj d;
    private int e;
    private int f;
    private int g;
    private a h;
    private boolean i;

    public static class a {
        int a;

        public a(int i) {
            this.a = i;
        }
    }

    public bs(Context context, br brVar) {
        this(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism//" + brVar.a()), brVar.c(), brVar.d(), brVar.e(), brVar.b(), brVar.f());
    }

    private bs(File file, a aVar, cj cjVar, int i, int i2, String str) {
        this.i = false;
        this.h = aVar;
        this.d = cjVar;
        this.g = i;
        this.f = i2;
        this.b = str;
        this.a = file;
        file.mkdirs();
        d();
        this.e = h().length;
        this.c = new LinkedList();
    }

    public final bs a(Context context) {
        return new bs(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism/pending/" + (this.a.getName() + "_" + UUID.randomUUID().toString())), this.h, this.d, this.g, this.f, this.b);
    }

    private boolean d() {
        if (!this.a.isDirectory()) {
            this.i = true;
            String absolutePath = this.a.getAbsolutePath();
            if (this.a.exists()) {
                IOException iOException = new IOException(absolutePath + " is not a directory");
            } else {
                FileNotFoundException fileNotFoundException = new FileNotFoundException(absolutePath + " does not exist");
            }
        }
        if (this.i) {
            return false;
        }
        return true;
    }

    public final synchronized boolean a(ch chVar) {
        boolean z = false;
        synchronized (this) {
            if (d()) {
                if (this.e >= this.g) {
                    dx.b();
                } else {
                    int b = b();
                    if (b != i() || f()) {
                        if (b > i()) {
                            this.i = true;
                        } else {
                            boolean c = c(chVar);
                            if (c) {
                                this.e++;
                            }
                            synchronized (this.c) {
                                for (bt c2 : this.c) {
                                    c2.c();
                                }
                            }
                            z = c;
                        }
                    }
                }
            }
        }
        return z;
    }

    public final synchronized boolean b(ch chVar) {
        boolean c;
        if (d()) {
            new File(this.a, chVar.e()).delete();
            c = c(chVar);
        } else {
            c = false;
        }
        return c;
    }

    private boolean c(ch chVar) {
        File file = new File(this.a, chVar.e());
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            new StringBuilder("Could not open output stream to : ").append(file);
            dx.a();
        }
        try {
            chVar.a(outputStream);
            return true;
        } catch (Throwable e2) {
            file.delete();
            dx.a("Unable to write to " + file.getAbsolutePath(), e2);
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (Throwable e22) {
                file.delete();
                dx.a("Unable to close " + file.getAbsolutePath(), e22);
                return false;
            }
        }
    }

    private void e() {
        while (b() > i()) {
            if (!f()) {
                return;
            }
        }
    }

    private boolean f() {
        a aVar = this.h;
        if (this.h == null) {
            return false;
        }
        a aVar2 = this.h;
        File[] g = g();
        File file = null;
        if (g.length > aVar2.a) {
            file = g[aVar2.a];
        }
        if (file == null || !file.delete()) {
            return false;
        }
        return true;
    }

    public final synchronized void a() {
        if (d()) {
            File[] h = h();
            for (File delete : h) {
                delete.delete();
            }
        }
    }

    private File[] g() {
        File[] h = h();
        Arrays.sort(h);
        return h;
    }

    private File[] h() {
        File[] listFiles = this.a.listFiles();
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }

    public final synchronized int b() {
        return h().length;
    }

    private synchronized int i() {
        return this.f;
    }

    public final synchronized void a(String str) {
        if (d() && str != null) {
            File file = new File(this.a.getAbsolutePath(), str);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(crittercism.android.bs r8) {
        /*
        r7 = this;
        if (r8 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = r7.a;
        r0 = r0.getName();
        r1 = r8.a;
        r1 = r1.getName();
        r0 = r0.compareTo(r1);
        if (r0 == 0) goto L_0x0002;
    L_0x0015:
        if (r0 >= 0) goto L_0x002d;
    L_0x0017:
        r1 = r8;
        r2 = r7;
    L_0x0019:
        monitor-enter(r2);
        monitor-enter(r1);	 Catch:{ all -> 0x002a }
        r0 = r7.d();	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0027;
    L_0x0021:
        r0 = r8.d();	 Catch:{ all -> 0x0066 }
        if (r0 != 0) goto L_0x0030;
    L_0x0027:
        monitor-exit(r1);	 Catch:{ all -> 0x0066 }
        monitor-exit(r2);	 Catch:{ all -> 0x002a }
        goto L_0x0002;
    L_0x002a:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x002d:
        r1 = r7;
        r2 = r8;
        goto L_0x0019;
    L_0x0030:
        r3 = r7.g();	 Catch:{ all -> 0x0066 }
        r0 = 0;
    L_0x0035:
        r4 = r3.length;	 Catch:{ all -> 0x0066 }
        if (r0 >= r4) goto L_0x004d;
    L_0x0038:
        r4 = new java.io.File;	 Catch:{ all -> 0x0066 }
        r5 = r8.a;	 Catch:{ all -> 0x0066 }
        r6 = r3[r0];	 Catch:{ all -> 0x0066 }
        r6 = r6.getName();	 Catch:{ all -> 0x0066 }
        r4.<init>(r5, r6);	 Catch:{ all -> 0x0066 }
        r5 = r3[r0];	 Catch:{ all -> 0x0066 }
        r5.renameTo(r4);	 Catch:{ all -> 0x0066 }
        r0 = r0 + 1;
        goto L_0x0035;
    L_0x004d:
        r8.e();	 Catch:{ all -> 0x0066 }
        r0 = r7.c;	 Catch:{ all -> 0x0066 }
        r3 = r0.iterator();	 Catch:{ all -> 0x0066 }
    L_0x0056:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0069;
    L_0x005c:
        r0 = r3.next();	 Catch:{ all -> 0x0066 }
        r0 = (crittercism.android.bt) r0;	 Catch:{ all -> 0x0066 }
        r0.d();	 Catch:{ all -> 0x0066 }
        goto L_0x0056;
    L_0x0066:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002a }
        throw r0;	 Catch:{ all -> 0x002a }
    L_0x0069:
        monitor-exit(r1);	 Catch:{ all -> 0x0066 }
        monitor-exit(r2);	 Catch:{ all -> 0x002a }
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.bs.a(crittercism.android.bs):void");
    }

    public final synchronized List c() {
        List arrayList;
        arrayList = new ArrayList();
        if (d()) {
            cj cjVar = this.d;
            File[] g = g();
            for (File a : g) {
                arrayList.add(this.d.a(a));
            }
        }
        return arrayList;
    }
}
