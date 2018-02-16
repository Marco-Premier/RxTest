package com.prem.test.swrve.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by prem on 14/02/2018.
 * Useful annotation for easily setting up fonts
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Font {

    enum Fonts {
        ROMAN, BOOK, LIGHT
    }

    Fonts value() default Fonts.ROMAN;

}
