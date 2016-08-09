package com.upsight.mediation.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.IAPOfferInfo;
import com.upsight.mediation.VGOfferInfo;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.upsight.mediation.util.HashMapCaster;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.Date;
import java.util.HashMap;
import spacemadness.com.lunarconsole.R;

public class Offer {
    public static final int IAP_OFFER = 51;
    public static final int VIRTUAL_GOODS_OFFER = 52;
    public final int adZoneID;
    public boolean consumed = false;
    @Nullable
    public final Integer contentId;
    public final Date endTime;
    public final int id;
    public final String itemId;
    public final String itemName;
    public final int itemQuantity;
    public final String metadata;
    public final String offerHtml;
    public final String purchaseCurrency;
    public final float purchasePrice;
    public final Date startTime;
    public final int t;
    public final OfferType type;
    public final int vg_currencyID;
    public final int vg_virtualGoodID;
    public final String zoneName;

    public @interface OfferCategory {
    }

    public enum OfferType {
        Discount(1),
        Standard(2),
        Bonus(3);
        
        public int value;

        private OfferType(int value) {
            this.value = value;
        }

        public static OfferType getType(int value) {
            switch (value) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    return Discount;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    return Standard;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    return Bonus;
                default:
                    return null;
            }
        }
    }

    public Offer(int id, String itemId, @Nullable Integer contentId, int vg_virtualGoodID, int vg_currencyID, int adZoneID, String zoneName, int t, String itemName, int itemQuantity, float purchasePrice, String purchaseCurrency, OfferType type, String offerHtml, Date startTime, Date endTime, String metadata) {
        this.id = id;
        this.itemId = itemId;
        this.contentId = contentId;
        this.vg_virtualGoodID = vg_virtualGoodID;
        this.vg_currencyID = vg_currencyID;
        this.adZoneID = adZoneID;
        this.zoneName = zoneName;
        this.t = t;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.purchasePrice = purchasePrice;
        this.purchaseCurrency = purchaseCurrency;
        this.type = type;
        this.offerHtml = offerHtml;
        this.startTime = startTime;
        this.endTime = endTime;
        this.metadata = metadata;
    }

    public Object getInfo() {
        if (this.t == IAP_OFFER) {
            IAPOfferInfo info = new IAPOfferInfo();
            info.itemAmount = this.itemQuantity;
            info.productPrice = this.purchasePrice;
            info.itemName = this.itemName;
            info.productID = this.itemId;
            info.startTime = this.startTime;
            info.endTime = this.endTime;
            info.metadata = this.metadata;
            return info;
        }
        info = new VGOfferInfo();
        info.purchaseCurrency = this.purchaseCurrency;
        info.purchasePrice = this.purchasePrice;
        info.itemName = this.itemName;
        info.itemAmount = this.itemQuantity;
        info.virtualGoodID = this.vg_virtualGoodID;
        info.currencyID = this.vg_currencyID;
        info.startTime = this.startTime;
        info.endTime = this.endTime;
        info.metadata = this.metadata;
        return info;
    }

    public void consume() {
        this.consumed = true;
    }

    public boolean isExpired(@NonNull Date currentTime) {
        return this.endTime != null && this.endTime.before(currentTime);
    }

    public static Offer createFromValues(HashMap<String, String> values) {
        HashMapCaster map = new HashMapCaster(values);
        Integer contentId = Integer.valueOf(map.getInt("content_id"));
        if (contentId.intValue() == -1) {
            contentId = null;
        }
        return new Offer(map.getInt("offer_id"), map.get("bundle_id"), contentId, map.getInt("virtualgoodID"), map.getInt("currencyID"), map.getInt(TriggerIfContentAvailable.ID), map.get("zone_id"), map.getInt("t"), map.get("item"), map.getInt("itemQty", 0), map.getFloat("price", 0.0f), map.get("currency"), OfferType.getType(map.getInt(Keys.TYPE)), map.get("script"), map.getDate("start_date"), map.getDate("end_date"), map.get("metadata"));
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
        r0 = (com.upsight.mediation.data.Offer) r0;
        r3 = r5.id;
        r4 = r0.id;
        if (r3 != r4) goto L_0x0005;
    L_0x001b:
        r3 = r5.contentId;
        if (r3 == 0) goto L_0x00d1;
    L_0x001f:
        r3 = r5.contentId;
        r4 = r0.contentId;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0029:
        r3 = r5.vg_virtualGoodID;
        r4 = r0.vg_virtualGoodID;
        if (r3 != r4) goto L_0x0005;
    L_0x002f:
        r3 = r5.vg_currencyID;
        r4 = r0.vg_currencyID;
        if (r3 != r4) goto L_0x0005;
    L_0x0035:
        r3 = r5.adZoneID;
        r4 = r0.adZoneID;
        if (r3 != r4) goto L_0x0005;
    L_0x003b:
        r3 = r5.t;
        r4 = r0.t;
        if (r3 != r4) goto L_0x0005;
    L_0x0041:
        r3 = r5.itemQuantity;
        r4 = r0.itemQuantity;
        if (r3 != r4) goto L_0x0005;
    L_0x0047:
        r3 = r0.purchasePrice;
        r4 = r5.purchasePrice;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x0005;
    L_0x0051:
        r3 = r5.consumed;
        r4 = r0.consumed;
        if (r3 != r4) goto L_0x0005;
    L_0x0057:
        r3 = r5.itemId;
        if (r3 == 0) goto L_0x00d7;
    L_0x005b:
        r3 = r5.itemId;
        r4 = r0.itemId;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0065:
        r3 = r5.zoneName;
        if (r3 == 0) goto L_0x00dd;
    L_0x0069:
        r3 = r5.zoneName;
        r4 = r0.zoneName;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0073:
        r3 = r5.itemName;
        if (r3 == 0) goto L_0x00e3;
    L_0x0077:
        r3 = r5.itemName;
        r4 = r0.itemName;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0081:
        r3 = r5.purchaseCurrency;
        if (r3 == 0) goto L_0x00e9;
    L_0x0085:
        r3 = r5.purchaseCurrency;
        r4 = r0.purchaseCurrency;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x008f:
        r3 = r5.type;
        r4 = r0.type;
        if (r3 != r4) goto L_0x0005;
    L_0x0095:
        r3 = r5.offerHtml;
        if (r3 == 0) goto L_0x00ef;
    L_0x0099:
        r3 = r5.offerHtml;
        r4 = r0.offerHtml;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x00a3:
        r3 = r5.startTime;
        if (r3 == 0) goto L_0x00f5;
    L_0x00a7:
        r3 = r5.startTime;
        r4 = r0.startTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x00b1:
        r3 = r5.endTime;
        if (r3 == 0) goto L_0x00fb;
    L_0x00b5:
        r3 = r5.endTime;
        r4 = r0.endTime;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x00bf:
        r3 = r5.metadata;
        if (r3 == 0) goto L_0x0101;
    L_0x00c3:
        r3 = r5.metadata;
        r4 = r0.metadata;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x00ce;
    L_0x00cd:
        r1 = r2;
    L_0x00ce:
        r2 = r1;
        goto L_0x0005;
    L_0x00d1:
        r3 = r0.contentId;
        if (r3 == 0) goto L_0x0029;
    L_0x00d5:
        goto L_0x0005;
    L_0x00d7:
        r3 = r0.itemId;
        if (r3 == 0) goto L_0x0065;
    L_0x00db:
        goto L_0x0005;
    L_0x00dd:
        r3 = r0.zoneName;
        if (r3 == 0) goto L_0x0073;
    L_0x00e1:
        goto L_0x0005;
    L_0x00e3:
        r3 = r0.itemName;
        if (r3 == 0) goto L_0x0081;
    L_0x00e7:
        goto L_0x0005;
    L_0x00e9:
        r3 = r0.purchaseCurrency;
        if (r3 == 0) goto L_0x008f;
    L_0x00ed:
        goto L_0x0005;
    L_0x00ef:
        r3 = r0.offerHtml;
        if (r3 == 0) goto L_0x00a3;
    L_0x00f3:
        goto L_0x0005;
    L_0x00f5:
        r3 = r0.startTime;
        if (r3 == 0) goto L_0x00b1;
    L_0x00f9:
        goto L_0x0005;
    L_0x00fb:
        r3 = r0.endTime;
        if (r3 == 0) goto L_0x00bf;
    L_0x00ff:
        goto L_0x0005;
    L_0x0101:
        r3 = r0.metadata;
        if (r3 != 0) goto L_0x00cd;
    L_0x0105:
        goto L_0x00ce;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.data.Offer.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = ((this.id * 31) + (this.itemId != null ? this.itemId.hashCode() : 0)) * 31;
        if (this.contentId != null) {
            hashCode = this.contentId.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (((((((hashCode2 + hashCode) * 31) + this.vg_virtualGoodID) * 31) + this.vg_currencyID) * 31) + this.adZoneID) * 31;
        if (this.zoneName != null) {
            hashCode = this.zoneName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (((hashCode2 + hashCode) * 31) + this.t) * 31;
        if (this.itemName != null) {
            hashCode = this.itemName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (((hashCode2 + hashCode) * 31) + this.itemQuantity) * 31;
        if (this.purchasePrice != 0.0f) {
            hashCode = Float.floatToIntBits(this.purchasePrice);
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.purchaseCurrency != null) {
            hashCode = this.purchaseCurrency.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.type != null) {
            hashCode = this.type.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.offerHtml != null) {
            hashCode = this.offerHtml.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.startTime != null) {
            hashCode = this.startTime.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.endTime != null) {
            hashCode = this.endTime.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 31;
        if (this.metadata != null) {
            hashCode = this.metadata.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode2 + hashCode) * 31;
        if (this.consumed) {
            i = 1;
        }
        return hashCode + i;
    }

    public String toString() {
        return "Offer{id=" + this.id + ", itemId='" + this.itemId + '\'' + ", contentId=" + this.contentId + ", vg_virtualGoodID=" + this.vg_virtualGoodID + ", vg_currencyID=" + this.vg_currencyID + ", adZoneID=" + this.adZoneID + ", zoneName='" + this.zoneName + '\'' + ", t=" + this.t + ", itemName='" + this.itemName + '\'' + ", itemQuantity=" + this.itemQuantity + ", purchasePrice=" + this.purchasePrice + ", purchaseCurrency='" + this.purchaseCurrency + '\'' + ", type=" + this.type + ", offerHtml='" + this.offerHtml + '\'' + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", metadata='" + this.metadata + '\'' + ", consumed=" + this.consumed + '}';
    }
}
