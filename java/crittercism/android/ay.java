package crittercism.android;

import java.lang.Thread.UncaughtExceptionHandler;

public final class ay implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler a;
    private final az b;

    public final void uncaughtException(java.lang.Thread r4, java.lang.Throwable r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0018 in list [B:22:0x003a]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r3 = this;
        r0 = r3.b;	 Catch:{ ThreadDeath -> 0x0019, Throwable -> 0x0030, all -> 0x001b }
        r0.a(r5);	 Catch:{ ThreadDeath -> 0x0019, Throwable -> 0x0030, all -> 0x001b }
        r0 = r3.a;
        if (r0 == 0) goto L_0x0018;
    L_0x0009:
        r0 = r3.a;
        r0 = r0 instanceof crittercism.android.ay;
        if (r0 != 0) goto L_0x0018;
    L_0x000f:
        r0 = r3.a;
        r1 = java.lang.Thread.currentThread();
        r0.uncaughtException(r1, r5);
    L_0x0018:
        return;
    L_0x0019:
        r0 = move-exception;
        throw r0;	 Catch:{ ThreadDeath -> 0x0019, Throwable -> 0x0030, all -> 0x001b }
    L_0x001b:
        r0 = move-exception;
        r1 = r3.a;
        if (r1 == 0) goto L_0x002f;
    L_0x0020:
        r1 = r3.a;
        r1 = r1 instanceof crittercism.android.ay;
        if (r1 != 0) goto L_0x002f;
    L_0x0026:
        r1 = r3.a;
        r2 = java.lang.Thread.currentThread();
        r1.uncaughtException(r2, r5);
    L_0x002f:
        throw r0;
    L_0x0030:
        r0 = move-exception;
        r1 = "Unable to send crash";	 Catch:{ ThreadDeath -> 0x0019, Throwable -> 0x0030, all -> 0x001b }
        crittercism.android.dx.a(r1, r0);	 Catch:{ ThreadDeath -> 0x0019, Throwable -> 0x0030, all -> 0x001b }
        r0 = r3.a;
        if (r0 == 0) goto L_0x0018;
    L_0x003a:
        r0 = r3.a;
        r0 = r0 instanceof crittercism.android.ay;
        if (r0 != 0) goto L_0x0018;
    L_0x0040:
        r0 = r3.a;
        r1 = java.lang.Thread.currentThread();
        r0.uncaughtException(r1, r5);
        goto L_0x0018;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.ay.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }

    public ay(az azVar, UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.b = azVar;
        this.a = uncaughtExceptionHandler;
    }
}
