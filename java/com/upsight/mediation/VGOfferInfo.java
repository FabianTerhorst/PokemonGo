package com.upsight.mediation;

import android.util.Base64;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONStringer;
import spacemadness.com.lunarconsole.BuildConfig;

public class VGOfferInfo {
    public int currencyID;
    public Date endTime;
    public int itemAmount;
    public String itemName;
    public String metadata;
    public String purchaseCurrency;
    public float purchasePrice;
    public Date startTime;
    public int virtualGoodID;

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
        r0 = (com.upsight.mediation.VGOfferInfo) r0;
        r3 = r0.purchasePrice;
        r4 = r5.purchasePrice;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x0005;
    L_0x001f:
        r3 = r5.itemAmount;
        r4 = r0.itemAmount;
        if (r3 != r4) goto L_0x0005;
    L_0x0025:
        r3 = r5.virtualGoodID;
        r4 = r0.virtualGoodID;
        if (r3 != r4) goto L_0x0005;
    L_0x002b:
        r3 = r5.currencyID;
        r4 = r0.currencyID;
        if (r3 != r4) goto L_0x0005;
    L_0x0031:
        r3 = r5.purchaseCurrency;
        if (r3 == 0) goto L_0x007a;
    L_0x0035:
        r3 = r5.purchaseCurrency;
        r4 = r0.purchaseCurrency;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x003f:
        r3 = r5.itemName;
        if (r3 == 0) goto L_0x007f;
    L_0x0043:
        r3 = r5.itemName;
        r4 = r0.itemName;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x004d:
        r3 = r5.startTime;
        if (r3 == 0) goto L_0x0084;
    L_0x0051:
        r3 = r5.startTime;
        r4 = r0.startTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x005b:
        r3 = r5.endTime;
        if (r3 == 0) goto L_0x008a;
    L_0x005f:
        r3 = r5.endTime;
        r4 = r0.endTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0069:
        r3 = r5.metadata;
        if (r3 == 0) goto L_0x0090;
    L_0x006d:
        r3 = r5.metadata;
        r4 = r0.metadata;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0078;
    L_0x0077:
        r1 = r2;
    L_0x0078:
        r2 = r1;
        goto L_0x0005;
    L_0x007a:
        r3 = r0.purchaseCurrency;
        if (r3 == 0) goto L_0x003f;
    L_0x007e:
        goto L_0x0005;
    L_0x007f:
        r3 = r0.itemName;
        if (r3 == 0) goto L_0x004d;
    L_0x0083:
        goto L_0x0005;
    L_0x0084:
        r3 = r0.startTime;
        if (r3 == 0) goto L_0x005b;
    L_0x0088:
        goto L_0x0005;
    L_0x008a:
        r3 = r0.endTime;
        if (r3 == 0) goto L_0x0069;
    L_0x008e:
        goto L_0x0005;
    L_0x0090:
        r3 = r0.metadata;
        if (r3 != 0) goto L_0x0077;
    L_0x0094:
        goto L_0x0078;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.VGOfferInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int result;
        int floatToIntBits;
        int i = 0;
        if (this.purchaseCurrency != null) {
            result = this.purchaseCurrency.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.purchasePrice != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.purchasePrice);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.itemName != null) {
            floatToIntBits = this.itemName.hashCode();
        } else {
            floatToIntBits = 0;
        }
        i2 = (((((((i2 + floatToIntBits) * 31) + this.itemAmount) * 31) + this.virtualGoodID) * 31) + this.currencyID) * 31;
        if (this.startTime != null) {
            floatToIntBits = this.startTime.hashCode();
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.endTime != null) {
            floatToIntBits = this.endTime.hashCode();
        } else {
            floatToIntBits = 0;
        }
        floatToIntBits = (i2 + floatToIntBits) * 31;
        if (this.metadata != null) {
            i = this.metadata.hashCode();
        }
        return floatToIntBits + i;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("VGOfferInfo{");
        sb.append("purchaseCurrency='").append(this.purchaseCurrency).append('\'');
        sb.append(", purchasePrice=").append(this.purchasePrice);
        sb.append(", itemName='").append(this.itemName).append('\'');
        sb.append(", itemAmount=").append(this.itemAmount);
        sb.append(", virtualGoodID=").append(this.virtualGoodID);
        sb.append(", currencyID=").append(this.currencyID);
        sb.append(", startTime=").append(this.startTime);
        sb.append(", endTime=").append(this.endTime);
        sb.append(", metadata='").append(this.metadata).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String toJSONString() {
        long j = 0;
        try {
            JSONStringer key = new JSONStringer().object().key("VGOfferInfo").object().key("purchaseCurrency").value(this.purchaseCurrency).key("purchasePrice").value((double) this.purchasePrice).key("itemName").value(this.itemName).key("itemAmount").value((long) this.itemAmount).key("virtualGoodID").value((long) this.virtualGoodID).key("currencyID").value((long) this.currencyID).key("startTime").value(this.startTime == null ? 0 : this.startTime.getTime()).key("endTime");
            if (this.endTime != null) {
                j = this.endTime.getTime();
            }
            return key.value(j).key("metadata").value(this.metadata == null ? BuildConfig.FLAVOR : Base64.encodeToString(this.metadata.getBytes(), 2)).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"VGOfferInfo\" : \"\" }";
        }
    }
}
