package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upsight.android.Upsight;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public static class BaseSchema<T> {
        @JsonProperty("default")
        public T defaultValue;
        @JsonProperty("description")
        public String description;
        @JsonProperty("tag")
        public String tag;
        @JsonProperty("type")
        public String type;
    }

    public static class BooleanSchema extends BaseSchema<Boolean> {
    }

    public static class FloatSchema extends BaseSchema<Float> {
        @JsonProperty("max")
        public Float max;
        @JsonProperty("min")
        public Float min;
    }

    public static class IntSchema extends BaseSchema<Integer> {
        @JsonProperty("max")
        public Integer max;
        @JsonProperty("min")
        public Integer min;
    }

    public static class StringSchema extends BaseSchema<String> {
    }

    public static UxmSchema create(String uxmSchemaString, ObjectMapper mapper, UpsightLogger logger) throws IllegalArgumentException {
        String errMsg;
        List<BaseSchema> itemList = new ArrayList();
        Map<String, BaseSchema> itemSchemaMap = new HashMap();
        try {
            ArrayNode uxmSchemaNode = (ArrayNode) mapper.readTree(uxmSchemaString);
            Iterator i$ = uxmSchemaNode.iterator();
            while (i$.hasNext()) {
                JsonNode itemNode = (JsonNode) i$.next();
                if (!itemNode.isObject()) {
                    errMsg = "Managed variable schema must be a JSON object: " + itemNode;
                    logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                } else if (!itemNode.path(ITEM_SCHEMA_KEY_TAG).isTextual()) {
                    errMsg = "Managed variable schema must contain a tag: " + itemNode;
                    logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                } else if (!itemNode.path(ITEM_SCHEMA_KEY_TYPE).isTextual()) {
                    errMsg = "Managed variable schema must contain a type: " + itemNode;
                    logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                } else if (itemNode.has(ITEM_SCHEMA_KEY_DEFAULT)) {
                    String type = (String) sTypeSchemaMap.get(itemNode.path(ITEM_SCHEMA_KEY_TYPE).asText());
                    if (TextUtils.isEmpty(type)) {
                        errMsg = "Managed variable contains invalid types: " + itemNode;
                        logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                        throw new IllegalArgumentException(errMsg);
                    }
                    ((ObjectNode) itemNode).put(ITEM_SCHEMA_KEY_TYPE, type);
                    String tag = itemNode.path(ITEM_SCHEMA_KEY_TAG).asText();
                    Class clazz = (Class) sModelTypeSchemaMap.get(type);
                    if (clazz != null) {
                        try {
                            BaseSchema itemSchema = (BaseSchema) mapper.treeToValue(itemNode, clazz);
                            itemList.add(itemSchema);
                            itemSchemaMap.put(tag, itemSchema);
                        } catch (JsonProcessingException e) {
                            errMsg = "Managed variable contains invalid fields: " + itemNode;
                            logger.e(Upsight.LOG_TAG, e, errMsg, new Object[0]);
                            throw new IllegalArgumentException(errMsg, e);
                        }
                    }
                    errMsg = "Unknown managed variable type: " + type;
                    logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                } else {
                    errMsg = "Managed variable schema must contain a default value: " + itemNode;
                    logger.e(Upsight.LOG_TAG, errMsg, new Object[0]);
                    throw new IllegalArgumentException(errMsg);
                }
            }
            return new UxmSchema(itemList, itemSchemaMap, logger, uxmSchemaNode.toString());
        } catch (IOException e2) {
            errMsg = "Failed to parse UXM schema JSON: " + uxmSchemaString;
            logger.e(Upsight.LOG_TAG, e2, errMsg, new Object[0]);
            throw new IllegalArgumentException(errMsg, e2);
        } catch (ClassCastException e3) {
            errMsg = "UXM schema must be a JSON array: " + uxmSchemaString;
            logger.e(Upsight.LOG_TAG, e3, errMsg, new Object[0]);
            throw new IllegalArgumentException(errMsg, e3);
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
