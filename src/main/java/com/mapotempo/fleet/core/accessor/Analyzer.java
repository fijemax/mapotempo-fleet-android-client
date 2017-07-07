package com.mapotempo.fleet.core.accessor;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Analyzer.
 */
class Analyzer<T> {

    private Class<T> mClazz;

    private DocumentBase mDocumentAnnotation;

    private Field mIdField;

    /**
     * Analyzer.
     * @param clazz the class type to analyze (should be conform).
     * @throws CoreException Mapotempo exception.
     */
    Analyzer(@NotNull Class<T> clazz) throws CoreException {
        mClazz = clazz;
        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);
        analyzeType();
    }

    /**
     * analyzeType.
     * Analyse du type template :
     *  - Vérification de l'annotation DocumentBase
     *  - Vérification des champs doublons
     *  - Vérification de l'existance de la clef primaire.
     *  - To Completed.
     */
    private void analyzeType() throws CoreException {
        // Definition de l'annotationd de l'annotation
        if (mDocumentAnnotation == null) {
            throw new CoreException("In Class : " + mClazz.getTypeName() + ", annotation DocumentBase is not defined.");
        }

        boolean primary = false;

        // Champ en doublon
        Map<String, String> mapData = new HashMap<>();
        for (Field field : mClazz.getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                if(baseField.name().equals("_id" ) && field.getType().equals(String.class)) {
                    mIdField = field;
                    primary = true;
                }

                if(mapData.get(baseField.name()) != null) {
                    throw new CoreException("In Class : " + mClazz.getTypeName() + ", FieldBase : " + baseField.name() + " already defined.");
                } else {
                    mapData.put(baseField.name(), baseField.name());
                }
            }
        }

        // Definition de la clef primaire.
        if(!primary)
            throw new CoreException("In Class : " + mClazz.getTypeName() + ", no primary key '_id' found.");
    }

    public Map getData(T data) throws CoreException
    {
        Map<String, Object> mapData = new HashMap<>();
        for(Field field : mClazz.getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                if(baseField.foreign()) {
                    try {
                        Object base = field.get(data);
                        if(base != null) {
                            // Creation d'une instance d'analyze pour verifier que la foreign key est bien un type conforme.
                            Analyzer analyzer = new Analyzer(base.getClass());
                            mapData.put(baseField.name(), mIdField.get(base));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        mapData.put(baseField.name(), field.get(data));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mapData.put(mDocumentAnnotation.type_field(), mDocumentAnnotation.type());
        return mapData;
    }
}
