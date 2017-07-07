package com.mapotempo.fleet.core.base;

import java.lang.annotation.*;

/**
 * Created by maxime on 05/07/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DocumentBase {
    String type();
    String type_field() default "type";
}
