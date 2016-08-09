package com.upsight.android.googlepushservices;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.Style;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NoCache;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public interface UpsightPushNotificationBuilderFactory {

    public static class Default implements UpsightPushNotificationBuilderFactory {
        public static final float HTTP_REQUEST_BACKOFF_MULT = 2.0f;
        public static final int HTTP_REQUEST_MAX_RETRIES = 3;
        public static final int HTTP_REQUEST_TIMEOUT_MS = 5000;
        private RequestQueue mRequestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));

        public Default() {
            this.mRequestQueue.start();
        }

        public Builder getNotificationBuilder(UpsightContext upsight, String title, String message, String imageUrl) {
            Style style;
            Bitmap picture = null;
            if (!TextUtils.isEmpty(imageUrl)) {
                if (isImageUrlValid(imageUrl)) {
                    try {
                        picture = (Bitmap) getImageObservable(imageUrl).toBlocking().first();
                    } catch (Throwable t) {
                        upsight.getLogger().e(Upsight.LOG_TAG, "Failed to download notification picture, displaying notification without", t);
                    }
                } else {
                    upsight.getLogger().e(Upsight.LOG_TAG, "Malformed notification picture URL, displaying notification without", new Object[0]);
                }
            }
            if (picture != null) {
                style = new BigPictureStyle().bigPicture(picture).setSummaryText(message);
            } else {
                style = new BigTextStyle().bigText(message);
            }
            return new Builder(upsight.getApplicationContext()).setSmallIcon(upsight.getApplicationInfo().icon).setStyle(style).setContentTitle(title).setContentText(message);
        }

        protected boolean isImageUrlValid(String imageUrl) {
            return Patterns.WEB_URL.matcher(imageUrl).matches();
        }

        protected Observable<Bitmap> getImageObservable(final String imageUrl) {
            return Observable.create(new OnSubscribe<Bitmap>() {
                public void call(final Subscriber<? super Bitmap> subscriber) {
                    Default.this.mRequestQueue.add(new ImageRequest(imageUrl, new Listener<Bitmap>() {
                        public void onResponse(Bitmap response) {
                            subscriber.onNext(response);
                            subscriber.onCompleted();
                        }
                    }, 0, 0, ScaleType.CENTER_INSIDE, Config.ARGB_8888, new ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            subscriber.onError(error);
                        }
                    }).setRetryPolicy(new DefaultRetryPolicy(Default.HTTP_REQUEST_TIMEOUT_MS, Default.HTTP_REQUEST_MAX_RETRIES, Default.HTTP_REQUEST_BACKOFF_MULT)));
                }
            });
        }
    }

    Builder getNotificationBuilder(UpsightContext upsightContext, String str, String str2, String str3);
}
