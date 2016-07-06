package com.voxelbusters.nativeplugins.extensions;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import java.util.Arrays;

public class FileProviderExtended extends FileProvider {
    public Cursor query(Uri uri, String[] columnProjection, String selection, String[] selectionArguments, String sortingOrder) {
        Cursor source = super.query(uri, columnProjection, selection, selectionArguments, sortingOrder);
        String[] sourceColumnNames = source.getColumnNames();
        String[] finalColumnNames = null;
        for (String columnName : sourceColumnNames) {
            if ("_data".equals(columnName)) {
                finalColumnNames = sourceColumnNames;
            }
        }
        if (finalColumnNames == null) {
            finalColumnNames = (String[]) Arrays.copyOf(sourceColumnNames, sourceColumnNames.length + 1);
            finalColumnNames[sourceColumnNames.length] = "_data";
        }
        MatrixCursor cursor = new MatrixCursor(finalColumnNames, source.getCount());
        source.moveToPosition(-1);
        while (source.moveToNext()) {
            RowBuilder row = cursor.newRow();
            for (int i = 0; i < sourceColumnNames.length; i++) {
                row.add(source.getString(i));
            }
        }
        return cursor;
    }
}
