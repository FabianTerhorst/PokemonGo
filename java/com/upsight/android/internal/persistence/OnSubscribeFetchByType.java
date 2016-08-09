package com.upsight.android.internal.persistence;

import android.content.Context;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import rx.Observable.OnSubscribe;

class OnSubscribeFetchByType implements OnSubscribe<Storable> {
    private final WeakReference<Context> reference;
    private final String type;

    public void call(rx.Subscriber<? super com.upsight.android.internal.persistence.Storable> r13) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r12 = this;
        r0 = r12.reference;
        r6 = r0.get();
        r6 = (android.content.Context) r6;
        if (r6 != 0) goto L_0x0015;
    L_0x000a:
        r0 = new java.lang.IllegalStateException;
        r2 = "Context has been reclaimed by Android.";
        r0.<init>(r2);
        r13.onError(r0);
    L_0x0014:
        return;
    L_0x0015:
        r7 = 0;
        r0 = r12.type;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r1 = com.upsight.android.internal.persistence.Content.getContentTypeUri(r6, r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = r6.getContentResolver();	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r2 = 0;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r3 = 0;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r4 = 0;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r5 = 0;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r7 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        if (r7 != 0) goto L_0x003d;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
    L_0x002a:
        r0 = new com.upsight.android.UpsightException;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r2 = "Unable to retrieve stored objects.";	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r3 = 0;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r3 = new java.lang.Object[r3];	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0.<init>(r2, r3);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r13.onError(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        if (r7 == 0) goto L_0x0014;
    L_0x0039:
        r7.close();
        goto L_0x0014;
    L_0x003d:
        r0 = r7.moveToNext();	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        if (r0 == 0) goto L_0x0073;	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
    L_0x0043:
        r0 = "_id";	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = r7.getColumnIndex(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r8 = r7.getString(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = "type";	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = r7.getColumnIndex(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r10 = r7.getString(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = "data";	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = r7.getColumnIndex(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r11 = r7.getString(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r0 = com.upsight.android.internal.persistence.Storable.create(r8, r10, r11);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        r13.onNext(r0);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        goto L_0x003d;
    L_0x0069:
        r9 = move-exception;
        r13.onError(r9);	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        if (r7 == 0) goto L_0x0014;
    L_0x006f:
        r7.close();
        goto L_0x0014;
    L_0x0073:
        r13.onCompleted();	 Catch:{ Throwable -> 0x0069, all -> 0x007c }
        if (r7 == 0) goto L_0x0014;
    L_0x0078:
        r7.close();
        goto L_0x0014;
    L_0x007c:
        r0 = move-exception;
        if (r7 == 0) goto L_0x0082;
    L_0x007f:
        r7.close();
    L_0x0082:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.internal.persistence.OnSubscribeFetchByType.call(rx.Subscriber):void");
    }

    OnSubscribeFetchByType(Context context, String type) {
        if (context == null) {
            throw new IllegalArgumentException("Provided Context can not be null.");
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Provided type can not be empty or null.");
        } else {
            this.reference = new WeakReference(context);
            this.type = type;
        }
    }
}
