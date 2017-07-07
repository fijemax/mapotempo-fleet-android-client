package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Factory.
 */
class Factory<T> {

    private DatabaseHandler mDatabaseHandler;

    private Class<T> mClazz;

    private DocumentBase mDocumentAnnotation;

    private Constructor<T> mConstructor;

    /**
     * Factory.
     * @param clazz The class
     * @param databaseHandler The databaseHandler for subobject
     * @throws CoreException
     */
    Factory(Class<T> clazz, DatabaseHandler databaseHandler) throws CoreException {

        mDatabaseHandler = databaseHandler;

        mClazz = clazz;

        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);

        if(mDocumentAnnotation == null)
            throw new CoreException("e");
        try {
            mConstructor = mClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            System.err.println("In Class : " + mClazz.getTypeName() + ", no default constructor define.");
            throw new CoreException(e);
        }
    }

    public T getInstance(Document document) throws CoreException {
        try {
            T instance = mConstructor.newInstance();
            mClazz.getAnnotation(DocumentBase.class);
            for(Field field : instance.getClass().getFields()) {
                FieldBase baseField = field.getAnnotation(FieldBase.class);
                if (baseField != null) {
                    Object value = document.getProperty(baseField.name());
                    if (value != null) {
                        // Foreigner
                        if(baseField.foreign() == true) {
                            Class clazz = field.getType();
                            Access access = new Access(clazz, mDatabaseHandler);
                            System.out.println(value.toString());
                            Object model = access.get(value.toString());
                            field.set(instance, model);
                        }
                        // Classic
                        else {
                            // Generate SubModelBase Field
                            if (value instanceof Map) {
                                if (field.getType().getSuperclass() == SubModelBase.class) {
                                    field.set(instance, field.getType().getConstructor(Map.class, DatabaseHandler.class).newInstance(value, mDatabaseHandler));
                                }
                            }
                            // When a document property was update the true type was return by couchbase-lite.
                            else if (field.getType().equals(value.getClass())) {
                                field.set(instance, value);
                            }
                            // Else it's a primitive or string field
                            else {
                                field.set(instance, toObject(field.getType(), value.toString()));
                            }
                        }
                    }
                }
            }
            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * toObject.
     * @param clazz the target type
     * @param value the value to convert
     * @return type convert if possible
     */
    private static Object toObject( Class clazz, String value ) {
        if( Boolean.class == clazz || boolean.class == clazz) return Boolean.parseBoolean( value );
        if( Byte.class == clazz || byte.class == clazz) return Byte.parseByte( value );
        if( Short.class == clazz || short.class == clazz) return Short.parseShort( value );
        if( Integer.class == clazz || int.class == clazz) return Integer.parseInt( value );
        if( Long.class == clazz || long.class == clazz) return Long.parseLong( value );
        if( Float.class == clazz || float.class == clazz) return Float.parseFloat( value );
        if( Double.class == clazz || double.class == clazz) return Double.parseDouble( value );
        return value;
    }
}
