package io.nextunit.core.feature.exception;

import lombok.Builder;
import lombok.Getter;

/**
 * This exception is thrown, if the requested feature is not available.
 */
@Getter
public class FeatureNotFoundException extends RuntimeException {
    /**
     * Requested feature name.
     */
    private String featureName;

    @Builder
    public FeatureNotFoundException(String message, Throwable cause, String featureName) {
        super(message, cause);
    }
}
