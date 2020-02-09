package com.example.backend.miscellaneous;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyDateTimeFormat {
    String value() default "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
}

