package com.voxelbusters.nativeplugins.features.addressbook;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.RemoteException;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.AddressBook;
import com.voxelbusters.nativeplugins.defines.Keys.Permission;
import com.voxelbusters.nativeplugins.defines.UnityDefines;
import com.voxelbusters.nativeplugins.utilities.ApplicationUtility;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.FileUtility;
import com.voxelbusters.nativeplugins.utilities.JSONUtility;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressBookHandler {
    static final Uri CONTACT_CONTENT_URI = Contacts.CONTENT_URI;
    static final String CONTACT_IN_VISIBLE_GROUP = "in_visible_group";
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/name";
    @SuppressLint({"InlinedApi"})
    static final String DISPLAY_NAME = (VERSION.SDK_INT >= 11 ? PHONE_DISPLAY_NAME : PHONE_DISPLAY_NAME);
    static final String EMAIL_CONTACT_ID = "contact_id";
    static final Uri EMAIL_CONTENT_URI = Email.CONTENT_URI;
    static final String EMAIL_DATA = "data1";
    static final String EMAIL_TYPE = "data2";
    static final String FAMILY_NAME = "data3";
    static final String GIVEN_NAME = "data2";
    static final String HAS_PHONE_NUMBER = "has_phone_number";
    private static AddressBookHandler INSTANCE = null;
    static final String PHONE_CONTACT_ID = "contact_id";
    static final Uri PHONE_CONTENT_URI = Phone.CONTENT_URI;
    static final String PHONE_DISPLAY_NAME = "display_name";
    static final String PHONE_NUMBER = "data1";
    static final String PHONE_TYPE = "data2";
    static final String PHOTO_CONTENT_DIRECTORY = "photo";
    static final String PHOTO_URI = (VERSION.SDK_INT >= 11 ? "photo_uri" : "photo_id");
    static final String ROOT_CONTACT_ID = "_id";

    public static AddressBookHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddressBookHandler();
        }
        return INSTANCE;
    }

    private AddressBookHandler() {
    }

    public boolean isAuthorized() {
        return ApplicationUtility.hasPermission(NativePluginHelper.getCurrentContext(), Permission.READ_CONTACTS);
    }

    public void readContacts() {
        new Thread(new Runnable() {
            public void run() {
                AddressBookHandler.this.readContactsInBackground();
            }
        }).start();
    }

    public void addContact(String contactDataJsonStr) {
        final JSONObject json = JSONUtility.getJSON(contactDataJsonStr);
        new Thread(new Runnable() {
            public void run() {
                AddressBookHandler.this.addContactInternal(json);
            }
        }).start();
    }

    private void readContactsInBackground() {
        Exception e;
        ArrayList<HashMap> arrayList;
        Throwable th;
        String authStatus;
        HashMap<String, Object> packedData;
        Cursor cursor = null;
        try {
            Context context = NativePluginHelper.getCurrentContext();
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(CONTACT_CONTENT_URI, null, "in_visible_group = '1'", null, DISPLAY_NAME + " ASC");
            boolean contactsExist = cursor.moveToFirst();
            ArrayList<HashMap> detailsList = new ArrayList();
            if (contactsExist) {
                do {
                    try {
                        ContactDetails details = new ContactDetails();
                        String contactID = getCursorString(cursor, ROOT_CONTACT_ID);
                        Cursor nameCursor = contentResolver.query(Data.CONTENT_URI, null, "mimetype = ? AND contact_id = ?", new String[]{CONTENT_ITEM_TYPE, contactID}, PHONE_TYPE);
                        nameCursor.moveToFirst();
                        String displayName = getCursorString(nameCursor, DISPLAY_NAME);
                        details.setNames(displayName, getCursorString(nameCursor, FAMILY_NAME), getCursorString(nameCursor, PHONE_TYPE));
                        nameCursor.close();
                        if (Integer.parseInt(getCursorString(cursor, HAS_PHONE_NUMBER)) > 0) {
                            Cursor phonesCursor = contentResolver.query(PHONE_CONTENT_URI, null, "contact_id = " + contactID, null, null);
                            while (phonesCursor.moveToNext()) {
                                details.addPhoneNumber(getCursorString(phonesCursor, PHONE_NUMBER), getCursorInt(phonesCursor, PHONE_TYPE));
                            }
                            phonesCursor.close();
                        }
                        String pictureUriString = getCursorString(cursor, PHOTO_URI);
                        if (!StringUtility.isNullOrEmpty(pictureUriString)) {
                            String absolutePath = FileUtility.getSavedLocalFileFromUri(context, Uri.parse(pictureUriString), "contacts", contactID);
                            if (absolutePath == null) {
                                Debug.error(CommonDefines.ADDRESS_BOOK_TAG, "Unable to load profile image for below details");
                                Debug.log(CommonDefines.ADDRESS_BOOK_TAG, "Name : " + displayName);
                                Debug.log(CommonDefines.ADDRESS_BOOK_TAG, "pictureUriString : " + pictureUriString);
                            }
                            if (absolutePath != null) {
                                details.setPicturePath(absolutePath);
                            }
                        }
                        Cursor emailCursor = contentResolver.query(EMAIL_CONTENT_URI, null, "contact_id = ?", new String[]{contactID}, null);
                        while (emailCursor.moveToNext()) {
                            String emailContact = getCursorString(emailCursor, PHONE_NUMBER);
                            if (emailContact != null) {
                                details.addEmail(emailContact, getCursorInt(emailCursor, PHONE_TYPE));
                            }
                        }
                        emailCursor.close();
                        detailsList.add(details.getHash());
                    } catch (Exception e2) {
                        e = e2;
                        arrayList = detailsList;
                    } catch (Throwable th2) {
                        th = th2;
                        arrayList = detailsList;
                    }
                } while (cursor.moveToNext());
            }
            System.gc();
            authStatus = AddressBook.ACCESS_AUTHORIZED;
            if (cursor != null) {
                cursor.close();
            }
            arrayList = detailsList;
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                Debug.error(CommonDefines.ADDRESS_BOOK_TAG, e.getMessage());
                authStatus = AddressBook.ACCESS_RESTRICTED;
                arrayList = null;
                if (cursor != null) {
                    cursor.close();
                }
                packedData = new HashMap();
                packedData.put(AddressBook.AUTH_STATUS, authStatus);
                packedData.put(AddressBook.CONTACTS_LIST, arrayList);
                NativePluginHelper.sendMessage(UnityDefines.AddressBook.READ_CONTACTS_FINISED, (HashMap) packedData);
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        packedData = new HashMap();
        packedData.put(AddressBook.AUTH_STATUS, authStatus);
        packedData.put(AddressBook.CONTACTS_LIST, arrayList);
        NativePluginHelper.sendMessage(UnityDefines.AddressBook.READ_CONTACTS_FINISED, (HashMap) packedData);
    }

    private void addContactInternal(JSONObject json) {
        int index;
        String familyName = json.optString(AddressBook.FAMILY_NAME);
        String givenName = json.optString(AddressBook.GIVEN_NAME);
        String imagePath = json.optString(Keys.IMAGE_PATH, null);
        byte[] imageByteStream = null;
        if (!StringUtility.isNullOrEmpty(imagePath)) {
            ByteArrayOutputStream stream = FileUtility.getBitmapStream(imagePath);
            if (stream != null) {
                imageByteStream = stream.toByteArray();
            }
        }
        JSONArray emailIdList = json.optJSONArray(AddressBook.EMAIL_ID_LIST);
        JSONArray phoneNumberList = json.optJSONArray(AddressBook.PHONE_NUM_LIST);
        ContentResolver contentResolver = NativePluginHelper.getCurrentContext().getContentResolver();
        ArrayList<ContentProviderOperation> contactOperation = new ArrayList();
        int contactIndex = contactOperation.size();
        Debug.error("Test", "count : " + contactIndex);
        Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
        builder.withValue("account_type", null);
        builder.withValue("account_name", null);
        contactOperation.add(builder.build());
        builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
        builder.withValueBackReference("raw_contact_id", contactIndex);
        builder.withValue(FAMILY_NAME, familyName);
        builder.withValue(PHONE_TYPE, givenName);
        builder.withValue("mimetype", CONTENT_ITEM_TYPE);
        contactOperation.add(builder.build());
        if (imageByteStream != null) {
            builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", contactIndex);
            builder.withValue("data15", imageByteStream);
            builder.withValue("mimetype", "vnd.android.cursor.item/photo");
            contactOperation.add(builder.build());
        }
        if (emailIdList != null) {
            for (index = 0; index < emailIdList.length(); index++) {
                String eachEmail = null;
                try {
                    eachEmail = (String) emailIdList.get(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!StringUtility.isNullOrEmpty(eachEmail)) {
                    builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
                    builder.withValueBackReference("raw_contact_id", contactIndex);
                    builder.withValue(PHONE_NUMBER, eachEmail);
                    builder.withValue(PHONE_TYPE, Integer.valueOf(3));
                    builder.withValue("mimetype", "vnd.android.cursor.item/email_v2");
                    contactOperation.add(builder.build());
                }
            }
        }
        if (phoneNumberList != null) {
            for (index = 0; index < phoneNumberList.length(); index++) {
                String eachPhoneNumber = null;
                try {
                    eachPhoneNumber = (String) phoneNumberList.get(index);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                if (!StringUtility.isNullOrEmpty(eachPhoneNumber)) {
                    builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
                    builder.withValueBackReference("raw_contact_id", contactIndex);
                    builder.withValue(PHONE_NUMBER, eachPhoneNumber);
                    builder.withValue(PHONE_TYPE, Integer.valueOf(7));
                    builder.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
                    contactOperation.add(builder.build());
                }
            }
        }
        ContentProviderResult[] result = null;
        try {
            result = contentResolver.applyBatch("com.android.contacts", contactOperation);
        } catch (RemoteException e3) {
            e3.printStackTrace();
        } catch (OperationApplicationException e4) {
            e4.printStackTrace();
        }
        NativePluginHelper.sendMessage(UnityDefines.AddressBook.ADD_CONTACTS_FINISHED, result == null ? "false" : "true");
    }

    String getCursorString(Cursor cursor, String columnName) {
        String string = null;
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex != -1) {
                string = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    int getCursorInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
