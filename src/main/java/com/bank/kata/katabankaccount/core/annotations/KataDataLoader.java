package com.bank.kata.katabankaccount.core.annotations;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Transactional
@Profile("auto_data_injection")
public @interface KataDataLoader {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
