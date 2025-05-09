package io.github.julwas797.jtimebomb.annotations;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.FixedValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

class TimeBombTest {
    private static Class<?> clazz;

    @BeforeAll
    static void beforeAll() {
        var byteBuddy = new ByteBuddy();

        var annotationDescriptor = AnnotationDescription.Builder
                .ofType(TimeBomb.class)
                .define("value", "example")
                .define("message", "Hello, World!")
                .build();

        try (var unloaded = byteBuddy.subclass(Object.class)
                        .name("io.github.julwas797.jtimebomb.Test")
                        .defineMethod("test", String.class, Modifier.PUBLIC)
                        .intercept(FixedValue.value("Test!"))
                        .annotateMethod(annotationDescriptor)
                        .make()) {
            clazz = unloaded
                    .load(TimeBombTest.class.getClassLoader())
                    .getLoaded();
        }
    }

    @Test
    @DisplayName("Assure the annotations are applied correctly")
    void annotationTest() {
        Assertions.assertTrue(() -> Arrays.stream(clazz.getMethods()).anyMatch(
                method -> method.isAnnotationPresent(TimeBomb.class)));
    }
}
