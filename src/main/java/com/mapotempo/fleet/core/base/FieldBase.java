package com.mapotempo.fleet.core.base;

import java.lang.annotation.*;

/**
 * DocumentBase field annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface FieldBase {
    String name();
    boolean foreign() default false;
    //Class if_missing();
}
