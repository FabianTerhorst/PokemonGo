package com.upsight.mediation;

import android.util.Base64;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONStringer;
import spacemadness.com.lunarconsole.BuildConfig;

public class IAPOfferInfo {
    public Date endTime;
    public int itemAmount;
    public String itemName;
    public String metadata;
    public String productID;
    public float productPrice;
    public Date startTime;

    public String toString() {
        StringBuffer sb = new StringBuffer("IAPOfferInfo{");
        sb.append("productPrice=").append(this.productPrice);
        sb.append(", itemAmount=").append(this.itemAmount);
        sb.append(", itemName='").append(this.itemName).append('\'');
        sb.append(", productID='").append(this.productID).append('\'');
        sb.append(", startTime=").append(this.startTime);
        sb.append(", endTime=").append(this.endTime);
        sb.append(", metadata='").append(this.metadata).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r1 = 1;
        r2 = 0;
        if (r5 != r6) goto L_0x0006;
    L_0x0004:
        r2 = r1;
    L_0x0005:
        return r2;
    L_0x0006:
        if (r6 == 0) goto L_0x0005;
    L_0x0008:
        r3 = r5.getClass();
        r4 = r6.getClass();
        if (r3 != r4) goto L_0x0005;
    L_0x0012:
        r0 = r6;
        r0 = (com.upsight.mediation.IAPOfferInfo) r0;
        r3 = r0.productPrice;
        r4 = r5.productPrice;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x0005;
    L_0x001f:
        r3 = r5.itemAmount;
        r4 = r0.itemAmount;
        if (r3 != r4) goto L_0x0005;
    L_0x0025:
        r3 = r5.itemName;
        if (r3 == 0) goto L_0x006e;
    L_0x0029:
        r3 = r5.itemName;
        r4 = r0.itemName;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0033:
        r3 = r5.productID;
        if (r3 == 0) goto L_0x0073;
    L_0x0037:
        r3 = r5.productID;
        r4 = r0.productID;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0041:
        r3 = r5.startTime;
        if (r3 == 0) goto L_0x0078;
    L_0x0045:
        r3 = r5.startTime;
        r4 = r0.startTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x004f:
        r3 = r5.endTime;
        if (r3 == 0) goto L_0x007d;
    L_0x0053:
        r3 = r5.endTime;
        r4 = r0.endTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x005d:
        r3 = r5.metadata;
        if (r3 == 0) goto L_0x0082;
    L_0x0061:
        r3 = r5.metadata;
        r4 = r0.metadata;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x006c;
    L_0x006b:
        r1 = r2;
    L_0x006c:
        r2 = r1;
        goto L_0x0005;
    L_0x006e:
        r3 = r0.itemName;
        if (r3 == 0) goto L_0x0033;
    L_0x0072:
        goto L_0x0005;
    L_0x0073:
        r3 = r0.productID;
        if (r3 == 0) goto L_0x0041;
    L_0x0077:
        goto L_0x0005;
    L_0x0078:
        r3 = r0.startTime;
        if (r3 == 0) goto L_0x004f;
    L_0x007c:
        goto L_0x0005;
    L_0x007d:
        r3 = r0.endTime;
        if (r3 == 0) goto L_0x005d;
    L_0x0081:
        goto L_0x0005;
    L_0x0082:
        r3 = r0.metadata;
        if (r3 != 0) goto L_0x006b;
    L_0x0086:
        goto L_0x006c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.IAPOfferInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.productPrice != 0.0f) {
            result = Float.floatToIntBits(this.productPrice);
        } else {
            result = 0;
        }
        int i2 = ((result * 31) + this.itemAmount) * 31;
        if (this.itemName != null) {
            hashCode = this.itemName.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.productID != null) {
            hashCode = this.productID.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.startTime != null) {
            hashCode = this.startTime.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.endTime != null) {
            hashCode = this.endTime.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.metadata != null) {
            i = this.metadata.hashCode();
        }
        return hashCode + i;
    }

    public String toJSONString() {
        try {
            return new JSONStringer().object().key("IAPOfferInfo").object().key("productPrice").value((double) this.productPrice).key("itemAmount").value((long) this.itemAmount).key("itemName").value(this.itemName).key("productID").value(this.productID).key("startTime").value(this.startTime == null ? BuildConfig.FLAVOR : Long.valueOf(this.startTime.getTime())).key("endTime").value(this.endTime == null ? BuildConfig.FLAVOR : Long.valueOf(this.endTime.getTime())).key("metadata").value(this.metadata == null ? BuildConfig.FLAVOR : Base64.encodeToString(this.metadata.getBytes(), 2)).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"IAPOfferInfo\" : \"\" }";
        }
    }
}
