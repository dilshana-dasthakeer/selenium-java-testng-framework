package com.dilshana.qa.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/** Applies the retry analyzer to every test, so no test needs its own annotation. */
public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class<?> testClass, Constructor<?> testConstructor,
                          Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
