package com.nianticproject.holoholo.sfida.service;

public class SfidaButtonDetector {
    private static final int NOTIFY_BIT_SIZE = 10;
    private static final int SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE = 2;
    private static final String TAG = SfidaButtonDetector.class.getSimpleName();
    int count = 0;
    private OnClickListener onClickListener;
    private ButtonStatus preButtonStatus = new ButtonStatus();

    private static class ButtonStatus {
        int clickedCount;
        int pressedCount;
        int releasedCount;
        boolean[] value = new boolean[SfidaButtonDetector.NOTIFY_BIT_SIZE];
    }

    public interface OnClickListener {
        void onClick();

        void onPress();

        void onRelease();
    }

    public void setOnclickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void release() {
        setOnclickListener(null);
    }

    public void setButtonStatus(byte[] buttonValue, boolean isOnBackground) {
        if (buttonValue != null && buttonValue.length == SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE) {
            byte count;
            int i;
            boolean z;
            boolean[] result = new boolean[NOTIFY_BIT_SIZE];
            Integer value = Integer.valueOf(buttonValue[0]);
            for (count = (byte) 0; count < (byte) 2; count = (byte) (count + 1)) {
                i = 1 - count;
                if ((value.intValue() & (1 << count)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                result[i] = z;
            }
            value = Integer.valueOf(buttonValue[1]);
            for (count = (byte) 0; count < (byte) 8; count = (byte) (count + 1)) {
                i = 9 - count;
                if ((value.intValue() & (1 << count)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                result[i] = z;
            }
            this.preButtonStatus = getButtonStatus(result, isOnBackground);
        }
    }

    private com.nianticproject.holoholo.sfida.service.SfidaButtonDetector.ButtonStatus getButtonStatus(boolean[] r7, boolean r8) {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:617)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r6 = this;
        r0 = new com.nianticproject.holoholo.sfida.service.SfidaButtonDetector$ButtonStatus;
        r0.<init>();
        r3 = r6.preButtonStatus;
        r3 = r3.value;
        r4 = 9;
        r1 = r3[r4];
        r3 = TAG;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "isPreValuePressed start with : ";
        r4 = r4.append(r5);
        r4 = r4.append(r1);
        r4 = r4.toString();
        android.util.Log.d(r3, r4);
        r4 = r7.length;
        r3 = 0;
    L_0x0027:
        if (r3 >= r4) goto L_0x0063;
    L_0x0029:
        r2 = r7[r3];
        if (r2 == 0) goto L_0x0042;
    L_0x002d:
        if (r1 != 0) goto L_0x003e;
    L_0x002f:
        r5 = r0.pressedCount;
        r5 = r5 + 1;
        r0.pressedCount = r5;
        r5 = r6.onClickListener;
        if (r5 == 0) goto L_0x003e;
    L_0x0039:
        r5 = r6.onClickListener;
        r5.onPress();
    L_0x003e:
        r1 = r2;
        r3 = r3 + 1;
        goto L_0x0027;
    L_0x0042:
        if (r1 == 0) goto L_0x003e;
    L_0x0044:
        r5 = r0.clickedCount;
        r5 = r5 + 1;
        r0.clickedCount = r5;
        r5 = r0.releasedCount;
        r5 = r5 + 1;
        r0.releasedCount = r5;
        r5 = r6.onClickListener;
        if (r5 == 0) goto L_0x0059;
    L_0x0054:
        r5 = r6.onClickListener;
        r5.onClick();
    L_0x0059:
        r5 = r6.onClickListener;
        if (r5 == 0) goto L_0x003e;
    L_0x005d:
        r5 = r6.onClickListener;
        r5.onRelease();
        goto L_0x003e;
    L_0x0063:
        r0.value = r7;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nianticproject.holoholo.sfida.service.SfidaButtonDetector.getButtonStatus(boolean[], boolean):com.nianticproject.holoholo.sfida.service.SfidaButtonDetector$ButtonStatus");
    }

    private boolean[] createTestCase() {
        boolean[] result = new boolean[NOTIFY_BIT_SIZE];
        if (this.count == 0) {
            result[0] = false;
            result[1] = false;
            result[SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE] = false;
            result[3] = false;
            result[4] = false;
            result[5] = false;
            result[6] = false;
            result[7] = false;
            result[8] = false;
            result[9] = true;
        } else if (this.count == 1) {
            result[0] = false;
            result[1] = true;
            result[SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE] = false;
            result[3] = true;
            result[4] = false;
            result[5] = true;
            result[6] = false;
            result[7] = true;
            result[8] = false;
            result[9] = true;
        }
        this.count++;
        return result;
    }
}
