package com.upsight.mediation;

import org.json.JSONException;
import org.json.JSONStringer;

public class RewardedInfo {
    public final String preRollMessage;
    public final int rewardAmount;
    public final String rewardItem;
    public final int rewardItemId;
    public final String rewardMessage;

    public RewardedInfo(String preRollMessage, String rewardMessage, String rewardItem, int rewardAmount, int rewardItemId) {
        this.preRollMessage = preRollMessage;
        this.rewardMessage = rewardMessage;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
        this.rewardItemId = rewardItemId;
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
        r0 = (com.upsight.mediation.RewardedInfo) r0;
        r3 = r5.rewardAmount;
        r4 = r0.rewardAmount;
        if (r3 != r4) goto L_0x0005;
    L_0x001b:
        r3 = r5.rewardItemId;
        r4 = r0.rewardItemId;
        if (r3 != r4) goto L_0x0005;
    L_0x0021:
        r3 = r5.preRollMessage;
        if (r3 == 0) goto L_0x004e;
    L_0x0025:
        r3 = r5.preRollMessage;
        r4 = r0.preRollMessage;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x002f:
        r3 = r5.rewardMessage;
        if (r3 == 0) goto L_0x0053;
    L_0x0033:
        r3 = r5.rewardMessage;
        r4 = r0.rewardMessage;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x003d:
        r3 = r5.rewardItem;
        if (r3 == 0) goto L_0x0058;
    L_0x0041:
        r3 = r5.rewardItem;
        r4 = r0.rewardItem;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x004c;
    L_0x004b:
        r1 = r2;
    L_0x004c:
        r2 = r1;
        goto L_0x0005;
    L_0x004e:
        r3 = r0.preRollMessage;
        if (r3 == 0) goto L_0x002f;
    L_0x0052:
        goto L_0x0005;
    L_0x0053:
        r3 = r0.rewardMessage;
        if (r3 == 0) goto L_0x003d;
    L_0x0057:
        goto L_0x0005;
    L_0x0058:
        r3 = r0.rewardItem;
        if (r3 != 0) goto L_0x004b;
    L_0x005c:
        goto L_0x004c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.RewardedInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.preRollMessage != null) {
            result = this.preRollMessage.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.rewardMessage != null) {
            hashCode = this.rewardMessage.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.rewardItem != null) {
            i = this.rewardItem.hashCode();
        }
        return ((((hashCode + i) * 31) + this.rewardAmount) * 31) + this.rewardItemId;
    }

    public String toString() {
        return "RewardedInfo{preRollMessage='" + this.preRollMessage + '\'' + ", rewardMessage='" + this.rewardMessage + '\'' + ", rewardItem='" + this.rewardItem + '\'' + ", rewardAmount=" + this.rewardAmount + ", rewardItemId=" + this.rewardItemId + '}';
    }

    public String toJSONString() {
        try {
            return new JSONStringer().object().key("RewardedInfo").object().key("preRollMessage").value(this.preRollMessage).key("rewardMessage").value(this.rewardMessage).key("rewardItem").value(this.rewardItem).key("rewardAmount").value((long) this.rewardAmount).key("rewardItemId").value((long) this.rewardItemId).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"RewardedInfo\" : \"\" }";
        }
    }
}
