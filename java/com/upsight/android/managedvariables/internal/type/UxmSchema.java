package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.Upsight;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class UxmSchema {
    private static final String ITEM_SCHEMA_KEY_DEFAULT = "default";
    private static final String ITEM_SCHEMA_KEY_TAG = "tag";
    private static final String ITEM_SCHEMA_KEY_TYPE = "type";
    private static final Map<Class<? extends ManagedVariable>, Class<? extends BaseSchema>> sClassSchemaMap = new HashMap<Class<? extends ManagedVariable>, Class<? extends BaseSchema>>() {
        {
            put(UpsightManagedString.class, StringSchema.class);
            put(UpsightManagedBoolean.class, BooleanSchema.class);
            put(UpsightManagedInt.class, IntSchema.class);
            put(UpsightManagedFloat.class, FloatSchema.class);
        }
    };
    private static final Map<String, Class<? extends BaseSchema>> sModelTypeSchemaMap = new HashMap<String, Class<? extends BaseSchema>>() {
        {
            put("com.upsight.uxm.string", StringSchema.class);
            put("com.upsight.uxm.boolean", BooleanSchema.class);
            put("com.upsight.uxm.integer", IntSchema.class);
            put("com.upsight.uxm.float", FloatSchema.class);
        }
    };
    private static final Map<String, String> sTypeSchemaMap = new HashMap<String, String>() {
        {
            put("string", "com.upsight.uxm.string");
            put("boolean", "com.upsight.uxm.boolean");
            put("integer", "com.upsight.uxm.integer");
            put("float", "com.upsight.uxm.float");
        }
    };
    private List<BaseSchema> mItemList = new ArrayList();
    private Map<String, BaseSchema> mItemSchemaMap = new HashMap();
    private UpsightLogger mLogger;
    public final String mSchemaJsonString;

    public static abstract class BaseSchema<T> {
        private static final Set<String> BASE_KEYS = new HashSet<String>() {
            {
                add(UxmSchema.ITEM_SCHEMA_KEY_TAG);
                add(UxmSchema.ITEM_SCHEMA_KEY_TYPE);
                add("description");
                add(UxmSchema.ITEM_SCHEMA_KEY_DEFAULT);
            }
        };
        @SerializedName("default")
        @Expose
        public T defaultValue;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("tag")
        @Expose
        public String tag;
        @SerializedName("type")
        @Expose
        public String type;

        abstract Set<String> getTypeSpecificKeys();

        abstract boolean isDefaultValueValid(JsonElement jsonElement);

        private void validate(JsonElement element) throws IllegalArgumentException {
            if (element == null) {
                throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to null JSON element");
            } else if (element.isJsonObject()) {
                for (Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                    String key = (String) entry.getKey();
                    if (!BASE_KEYS.contains(key) && !getTypeSpecificKeys().contains(key)) {
                        throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to unknown key");
                    }
                }
                if (!isDefaultValueValid(element.getAsJsonObject().get(UxmSchema.ITEM_SCHEMA_KEY_DEFAULT))) {
                    throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to invalid default value");
                }
            } else {
                throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to invalid JSON element type");
            }
        }
    }

    public static class BooleanSchema extends BaseSchema<Boolean> {
        private static final Set<String> TYPE_SPECIFIC_KEYS = new HashSet();

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement element) {
            return element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean();
        }
    }

    public static class FloatSchema extends BaseSchema<Float> {
        private static final Set<String> TYPE_SPECIFIC_KEYS = new HashSet<String>() {
            {
                add("min");
                add("max");
            }
        };
        @SerializedName("max")
        @Expose
        public Float max;
        @SerializedName("min")
        @Expose
        public Float min;

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement element) {
            return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
        }
    }

    public static class IntSchema extends BaseSchema<Integer> {
        private static final Set<String> TYPE_SPECIFIC_KEYS = new HashSet<String>() {
            {
                add("min");
                add("max");
            }
        };
        @SerializedName("max")
        @Expose
        public Integer max;
        @SerializedName("min")
        @Expose
        public Integer min;

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement element) {
            return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
        }
    }

    public static class StringSchema extends BaseSchema<String> {
        private static final Set<String> TYPE_SPECIFIC_KEYS = new HashSet();

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement element) {
            return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
        }
    }

    public static UxmSchema create(String uxmSchemaString, Gson gson, JsonParser parser, UpsightLogger logger) throws IllegalArgumentException {
        List<BaseSchema> itemList = new ArrayList();
        Map<String, BaseSchema> itemSchemaMap = new HashMap();
        String errMsg;
        UpsightLogger upsightLogger;
        try {
            JsonElement uxmSchemaElement = parser.parse(uxmSchemaString);
            if (uxmSchemaElement == null || !uxmSchemaElement.isJsonArray()) {
                errMsg = "UXM schema must be a JSON array: " + uxmSchemaString;
                upsightLogger = logger;
                upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                throw new IllegalArgumentException(errMsg);
            }
            JsonArray uxmSchemaArray = uxmSchemaElement.getAsJsonArray();
            Iterator it = uxmSchemaArray.iterator();
            while (it.hasNext()) {
                JsonElement itemNode = (JsonElement) it.next();
                if (itemNode.isJsonObject()) {
                    JsonElement tagElement = itemNode.getAsJsonObject().get(ITEM_SCHEMA_KEY_TAG);
                    if (tagElement != null && tagElement.isJsonPrimitive() && tagElement.getAsJsonPrimitive().isString()) {
                        JsonElement typeElement = itemNode.getAsJsonObject().get(ITEM_SCHEMA_KEY_TYPE);
                        if (typeElement == null || !typeElement.isJsonPrimitive() || !typeElement.getAsJsonPrimitive().isString()) {
                            errMsg = "Managed variable schema must contain a type: " + itemNode;
                            upsightLogger = logger;
                            upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                            throw new IllegalArgumentException(errMsg);
                        } else if (itemNode.getAsJsonObject().has(ITEM_SCHEMA_KEY_DEFAULT)) {
                            String type = (String) sTypeSchemaMap.get(typeElement.getAsString());
                            if (TextUtils.isEmpty(type)) {
                                errMsg = "Managed variable contains invalid types: " + itemNode;
                                upsightLogger = logger;
                                upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                                throw new IllegalArgumentException(errMsg);
                            }
                            itemNode.getAsJsonObject().addProperty(ITEM_SCHEMA_KEY_TYPE, type);
                            String tag = tagElement.getAsString();
                            Class clazz = (Class) sModelTypeSchemaMap.get(type);
                            if (clazz != null) {
                                try {
                                    BaseSchema itemSchema = (BaseSchema) gson.fromJson(itemNode, clazz);
                                    itemSchema.validate(itemNode);
                                    itemList.add(itemSchema);
                                    itemSchemaMap.put(tag, itemSchema);
                                } catch (JsonSyntaxException e) {
                                    errMsg = "Managed variable contains invalid fields: " + itemNode;
                                    upsightLogger = logger;
                                    upsightLogger.e(Upsight.LOG_TAG, e, errMsg, new Object[0]);
                                    throw new IllegalArgumentException(errMsg, e);
                                }
                            }
                            errMsg = "Unknown managed variable type: " + type;
                            upsightLogger = logger;
                            upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                            throw new IllegalArgumentException(errMsg);
                        } else {
                            errMsg = "Managed variable schema must contain a default value: " + itemNode;
                            upsightLogger = logger;
                            upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                            throw new IllegalArgumentException(errMsg);
                        }
                    }
                    errMsg = "Managed variable schema must contain a tag: " + itemNode;
                    upsightLogger = logger;
                    upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                }
                errMsg = "Managed variable schema must be a JSON object: " + itemNode;
                upsightLogger = logger;
                upsightLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                throw new IllegalArgumentException(errMsg);
            }
            return new UxmSchema(itemList, itemSchemaMap, logger, uxmSchemaArray.toString());
        } catch (JsonSyntaxException e2) {
            errMsg = "Failed to parse UXM schema JSON: " + uxmSchemaString;
            upsightLogger = logger;
            upsightLogger.e(Upsight.LOG_TAG, e2, errMsg, new Object[0]);
            throw new IllegalArgumentException(errMsg, e2);
        }
    }

    UxmSchema(UpsightLogger logger) {
        this.mLogger = logger;
        this.mSchemaJsonString = null;
    }

    private UxmSchema(List<BaseSchema> itemList, Map<String, BaseSchema> itemSchemaMap, UpsightLogger logger, String schemaJsonString) {
        this.mItemList = itemList;
        this.mItemSchemaMap = itemSchemaMap;
        this.mLogger = logger;
        this.mSchemaJsonString = schemaJsonString;
    }

    public List<BaseSchema> getAllOrdered() {
        return new ArrayList(this.mItemList);
    }

    public <T extends ManagedVariable> BaseSchema get(Class<T> clazz, String tag) {
        BaseSchema itemSchema = (BaseSchema) this.mItemSchemaMap.get(tag);
        if (itemSchema == null) {
            return null;
        }
        Class<?> expectedClass = (Class) sClassSchemaMap.get(clazz);
        Class<?> tagClass = (Class) sModelTypeSchemaMap.get(itemSchema.type);
        if (expectedClass != null && tagClass != null && tagClass.equals(expectedClass)) {
            return itemSchema;
        }
        String errMsg = "The tag is not of the expected class: " + tag;
        this.mLogger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
        throw new IllegalArgumentException(errMsg);
    }
}
