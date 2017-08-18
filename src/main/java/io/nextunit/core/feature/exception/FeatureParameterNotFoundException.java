package io.nextunit.core.feature.exception;

import io.nextunit.core.feature.entity.FeatureConfiguration;
import lombok.Builder;
import lombok.Getter;

/**
 * This exception will be thrown, if the feature parameter is not found.
 */
@Getter
public class FeatureParameterNotFoundException extends RuntimeException {
    /**
     * Feature configuration
     */
    private FeatureConfiguration featureConfiguration;

    /**
     * Requested parameter key.
     */
    private String key;

    @Builder
    public FeatureParameterNotFoundException(String message,
                                             Throwable cause,
                                             FeatureConfiguration configuration,
                                             String key) {
        super(message, cause);
        this.featureConfiguration = configuration;
        this.key = key;
    }
}
