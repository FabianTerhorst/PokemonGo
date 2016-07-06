package com.upsight.android.internal.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.List;

public final class ContentProvider extends android.content.ContentProvider {
    private static final int MODEL = 2;
    private static final int MODEL_ALL = 3;
    private static final int MODEL_ITEM = 1;
    private static final String TYPE_SELECTION = "type = ?";
    private static UriMatcher sMatcher;
    private DataHelper mDataHelper;

    public boolean onCreate(Context context) {
        return onCreate(context, new SQLiteDataHelper(context));
    }

    public boolean onCreate(Context context, DataHelper dataHelper) {
        this.mDataHelper = dataHelper;
        if (sMatcher == null) {
            sMatcher = new UriMatcher(-1);
            String authority = Content.getAuthoritytUri(context).getAuthority();
            sMatcher.addURI(authority, "model/*/*", MODEL_ITEM);
            sMatcher.addURI(authority, "model/*", MODEL);
            sMatcher.addURI(authority, Models.CONTENT_DIRECTORY, MODEL_ALL);
        }
        return true;
    }

    public boolean onCreate() {
        return onCreate(getContext());
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sMatcher.match(uri)) {
            case MODEL_ITEM /*1*/:
                return this.mDataHelper.query(projection, updatedSelection(selection, uri.getLastPathSegment()), selectionArgs, sortOrder);
            case MODEL /*2*/:
                String[] arguments = new String[MODEL_ITEM];
                arguments[0] = uri.getLastPathSegment();
                return this.mDataHelper.query(projection, TYPE_SELECTION, arguments, sortOrder);
            case MODEL_ALL /*3*/:
                return this.mDataHelper.query(projection, selection, selectionArgs, sortOrder);
            default:
                throw new IllegalArgumentException("Uri not supported by query: " + uri);
        }
    }

    public String getType(Uri uri) {
        String result;
        switch (sMatcher.match(uri)) {
            case MODEL_ITEM /*1*/:
                result = Models.CONTENT_ITEM_TYPE;
                List<String> segments = uri.getPathSegments();
                if (segments.size() < MODEL_ALL) {
                    throw new IllegalArgumentException("Unexpected content uri: " + uri);
                } else if (TextUtils.isEmpty((String) segments.get(MODEL_ITEM))) {
                    return result;
                } else {
                    return result + "." + ((String) segments.get(MODEL_ITEM));
                }
            case MODEL /*2*/:
                result = Models.CONTENT_TYPE;
                String type = uri.getLastPathSegment();
                if (Models.CONTENT_DIRECTORY.equals(type)) {
                    return result;
                }
                return result + "." + type;
            default:
                return null;
        }
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (sMatcher.match(uri)) {
            case MODEL /*2*/:
                if (TextUtils.isEmpty(contentValues.getAsString(Keys.TYPE))) {
                    throw new IllegalArgumentException("ContentValues must have a model type");
                } else if (0 > this.mDataHelper.insert(contentValues)) {
                    throw new IllegalArgumentException("Failed to insert model for uri: " + uri);
                } else {
                    Uri resultUri = Uri.withAppendedPath(uri, contentValues.getAsString("_id"));
                    getContext().getContentResolver().notifyChange(resultUri, null);
                    return resultUri;
                }
            default:
                throw new IllegalArgumentException("Uri not supported by insert:" + uri);
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sMatcher.match(uri)) {
            case MODEL_ITEM /*1*/:
                int result = this.mDataHelper.delete(updatedSelection(selection, uri.getLastPathSegment()), selectionArgs);
                if (result > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return result;
            default:
                throw new IllegalArgumentException("Uri not supported by insert:" + uri);
        }
    }

    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (sMatcher.match(uri)) {
            case MODEL_ITEM /*1*/:
                int result = this.mDataHelper.update(contentValues, updatedSelection(selection, uri.getLastPathSegment()), selectionArgs);
                if (result > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return result;
            default:
                throw new IllegalArgumentException("Uri not supported by update:" + uri);
        }
    }

    private static String updatedSelection(String selection, String id) {
        String updatedSelection = "_id = '" + id + "'";
        if (selection != null) {
            return updatedSelection + " AND " + selection;
        }
        return updatedSelection;
    }
}
