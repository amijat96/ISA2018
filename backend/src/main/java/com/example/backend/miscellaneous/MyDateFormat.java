package com.example.backend.miscellaneous;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyDateFormat {
    String value() default "yyyy-MM-dd";
}
