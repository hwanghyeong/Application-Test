package com.daou.emoticon;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Test
@Tag("fast")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FastTest {
}
