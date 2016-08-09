package com.upsight.mediation.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.RewardedInfo;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.util.HashMapCaster;
import com.upsight.mediation.util.StringUtil;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public class Reward {
    private static final String TAG = "Reward";
    public final int itemId;
    public final int offerId;
    @NonNull
    public final String postRollMessage;
    @NonNull
    public final String preRollMessage;
    public final int rewardAmount;
    @NonNull
    public final String rewardItem;
    @Nullable
    public String richMediaPostrollScript;
    @Nullable
    public String richMediaPrerollScript;
    public final int zoneId;
    @NonNull
    public final String zoneName;

    public Reward(@Nullable String preRollMessage, @Nullable String postRollMessage, @Nullable String richMediaPrerollScript, @Nullable String richMediaPostrollScript, @NonNull String rewardItem, int rewardAmount, int itemId, int offerId, int zoneId, @NonNull String zoneName) {
        if (preRollMessage == null) {
            preRollMessage = BuildConfig.FLAVOR;
        }
        this.preRollMessage = preRollMessage;
        if (postRollMessage == null) {
            postRollMessage = BuildConfig.FLAVOR;
        }
        this.postRollMessage = postRollMessage;
        this.richMediaPrerollScript = richMediaPrerollScript;
        this.richMediaPostrollScript = richMediaPostrollScript;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
        this.itemId = itemId;
        this.offerId = offerId;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
    }

    public String toString() {
        return "RewardObject {zoneId:" + this.zoneId + ", offerId: " + this.offerId + ", itemId: " + this.itemId + ", rewardItem: " + this.rewardItem + ", rewardAmount: " + this.rewardAmount + ", preRollMessage: " + this.preRollMessage + ", rewardMessage: " + this.postRollMessage + "}";
    }

    @NonNull
    public RewardedInfo getInfo() {
        return new RewardedInfo(this.preRollMessage, this.postRollMessage, this.rewardItem, this.rewardAmount, this.itemId);
    }

    public boolean hasPostRollMessage() {
        return !StringUtil.isNullOrEmpty(this.postRollMessage);
    }

    public boolean hasPreRollMessage() {
        return !StringUtil.isNullOrEmpty(this.preRollMessage);
    }

    public boolean hasRichMediaPreroll() {
        return !StringUtil.isNullOrEmpty(this.richMediaPrerollScript);
    }

    public boolean hasRichMediaPostroll() {
        return !StringUtil.isNullOrEmpty(this.richMediaPostrollScript);
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
        r0 = (com.upsight.mediation.data.Reward) r0;
        r3 = r5.rewardAmount;
        r4 = r0.rewardAmount;
        if (r3 != r4) goto L_0x0005;
    L_0x001b:
        r3 = r5.itemId;
        r4 = r0.itemId;
        if (r3 != r4) goto L_0x0005;
    L_0x0021:
        r3 = r5.offerId;
        r4 = r0.offerId;
        if (r3 != r4) goto L_0x0005;
    L_0x0027:
        r3 = r5.zoneId;
        r4 = r0.zoneId;
        if (r3 != r4) goto L_0x0005;
    L_0x002d:
        r3 = r5.preRollMessage;
        if (r3 == 0) goto L_0x0068;
    L_0x0031:
        r3 = r5.preRollMessage;
        r4 = r0.preRollMessage;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x003b:
        r3 = r5.postRollMessage;
        if (r3 == 0) goto L_0x006d;
    L_0x003f:
        r3 = r5.postRollMessage;
        r4 = r0.postRollMessage;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0049:
        r3 = r5.rewardItem;
        if (r3 == 0) goto L_0x0072;
    L_0x004d:
        r3 = r5.rewardItem;
        r4 = r0.rewardItem;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0057:
        r3 = r5.zoneName;
        if (r3 == 0) goto L_0x0077;
    L_0x005b:
        r3 = r5.zoneName;
        r4 = r0.zoneName;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0066;
    L_0x0065:
        r1 = r2;
    L_0x0066:
        r2 = r1;
        goto L_0x0005;
    L_0x0068:
        r3 = r0.preRollMessage;
        if (r3 == 0) goto L_0x003b;
    L_0x006c:
        goto L_0x0005;
    L_0x006d:
        r3 = r0.postRollMessage;
        if (r3 == 0) goto L_0x0049;
    L_0x0071:
        goto L_0x0005;
    L_0x0072:
        r3 = r0.rewardItem;
        if (r3 == 0) goto L_0x0057;
    L_0x0076:
        goto L_0x0005;
    L_0x0077:
        r3 = r0.zoneName;
        if (r3 != 0) goto L_0x0065;
    L_0x007b:
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.data.Reward.equals(java.lang.Object):boolean");
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
        if (this.postRollMessage != null) {
            hashCode = this.postRollMessage.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.rewardItem != null) {
            hashCode = this.rewardItem.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (((((((((i2 + hashCode) * 31) + this.rewardAmount) * 31) + this.itemId) * 31) + this.offerId) * 31) + this.zoneId) * 31;
        if (this.zoneName != null) {
            i = this.zoneName.hashCode();
        }
        return hashCode + i;
    }

    @Nullable
    public static Reward createFromValues(HashMap<String, String> values) {
        HashMapCaster map = new HashMapCaster(values);
        String reward = map.get("reward");
        String zoneId = map.get("zone_id");
        if (reward != null && zoneId != null) {
            return new Reward(map.get("pre_roll"), map.get("post_roll"), map.get("pre_roll_script"), map.get("post_roll_script"), reward, map.getInt("amount", 0), map.getInt("item_id"), map.getInt("offer_id"), map.getInt(TriggerIfContentAvailable.ID), zoneId);
        }
        FuseLog.public_w(TAG, "Reward ignored due to missing values: " + map.toString());
        return null;
    }
}
