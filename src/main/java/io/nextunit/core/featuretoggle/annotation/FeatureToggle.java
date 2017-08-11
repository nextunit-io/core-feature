package io.nextunit.core.featuretoggle.annotation;

import java.lang.annotation.*;

/**
 * Use this annotation to check if a feature is enabled. If the feature is not enabled
 * the response will have a status code 404.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureToggle {
    String[] value();
}
