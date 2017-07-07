package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.FieldBase;

import java.lang.reflect.Field;

/**
 * ModelBase.
 */
public class ModelBase {
    @Override
    public String toString() {
        String res = this.getClass().getSimpleName();

        for(Field field : this.getClass().getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                try {
                    res +=  "\n";
                    if(!baseField.foreign())
                        res = res + "        " + String.format("%-20s %s" ,baseField.name().toUpperCase(), ": " + field.get(this));
                    else {
                        res = res + "        " + String.format("%-20s %s" ,baseField.name().toUpperCase(), ":\n");
                        res = res + "        {\n" + String.format("%-20s", field.get(this));
                        res = res + "\n        }";
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    @FieldBase(name = "_id")
    public String mId;

    @FieldBase(name = "_rev")
    public String mRef;

}
