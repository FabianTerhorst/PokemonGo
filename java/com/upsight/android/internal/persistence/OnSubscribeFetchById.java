package com.upsight.android.internal.persistence;

import android.content.Context;
import android.text.TextUtils;
import rx.Observable.OnSubscribe;

class OnSubscribeFetchById implements OnSubscribe<Storable> {
    private final Context mContext;
    private final String[] mIds;
    private final String mType;

    public void call(rx.Subscriber<? super com.upsight.android.internal.persistence.Storable> r14) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextEntry(HashMap.java:854)
	at java.util.HashMap$KeyIterator.next(HashMap.java:885)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r13 = this;
        r12 = new java.lang.StringBuffer;
        r12.<init>();
        r0 = "_id";
        r0 = r12.append(r0);
        r1 = " IN (";
        r0.append(r1);
        r7 = 0;
    L_0x0011:
        r0 = r13.mIds;
        r0 = r0.length;
        if (r7 >= r0) goto L_0x002a;
    L_0x0016:
        r0 = "?";
        r12.append(r0);
        r0 = r13.mIds;
        r0 = r0.length;
        r0 = r0 + -1;
        if (r7 >= r0) goto L_0x0027;
    L_0x0022:
        r0 = ",";
        r12.append(r0);
    L_0x0027:
        r7 = r7 + 1;
        goto L_0x0011;
    L_0x002a:
        r0 = ")";
        r12.append(r0);
        r6 = 0;
        r0 = r13.mContext;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = r0.getContentResolver();	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = r13.mContext;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = com.upsight.android.internal.persistence.Content.getContentUri(r1);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r2 = 0;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r3 = r12.toString();	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r4 = r13.mIds;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r5 = 0;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r6 != 0) goto L_0x005d;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
    L_0x004a:
        r0 = new com.upsight.android.UpsightException;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = "Unable to retrieve stored objects.";	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r2 = 0;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r2 = new java.lang.Object[r2];	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0.<init>(r1, r2);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r14.onError(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r6 == 0) goto L_0x005c;
    L_0x0059:
        r6.close();
    L_0x005c:
        return;
    L_0x005d:
        r0 = r6.getCount();	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = r13.mIds;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = r1.length;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r0 == r1) goto L_0x0079;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
    L_0x0066:
        r0 = new com.upsight.android.UpsightException;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r1 = "Unable to retrieve stored objects. Some ID(s) were not found.";	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r2 = 0;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r2 = new java.lang.Object[r2];	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0.<init>(r1, r2);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r14.onError(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r6 == 0) goto L_0x005c;
    L_0x0075:
        r6.close();
        goto L_0x005c;
    L_0x0079:
        r0 = r6.moveToNext();	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r0 == 0) goto L_0x00af;	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
    L_0x007f:
        r0 = "_id";	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = r6.getColumnIndex(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r8 = r6.getString(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = "type";	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = r6.getColumnIndex(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r10 = r6.getString(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = "data";	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = r6.getColumnIndex(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r11 = r6.getString(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r0 = com.upsight.android.internal.persistence.Storable.create(r8, r10, r11);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        r14.onNext(r0);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        goto L_0x0079;
    L_0x00a5:
        r9 = move-exception;
        r14.onError(r9);	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r6 == 0) goto L_0x005c;
    L_0x00ab:
        r6.close();
        goto L_0x005c;
    L_0x00af:
        r14.onCompleted();	 Catch:{ Throwable -> 0x00a5, all -> 0x00b8 }
        if (r6 == 0) goto L_0x005c;
    L_0x00b4:
        r6.close();
        goto L_0x005c;
    L_0x00b8:
        r0 = move-exception;
        if (r6 == 0) goto L_0x00be;
    L_0x00bb:
        r6.close();
    L_0x00be:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.internal.persistence.OnSubscribeFetchById.call(rx.Subscriber):void");
    }

    OnSubscribeFetchById(Context context, String type, String... ids) {
        if (context == null) {
            throw new IllegalArgumentException("Provided Context can not be null.");
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Provided type can not be empty or null.");
        } else if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("Object identifiers can not be null or empty.");
        } else {
            this.mContext = context;
            this.mType = type;
            this.mIds = ids;
        }
    }
}
