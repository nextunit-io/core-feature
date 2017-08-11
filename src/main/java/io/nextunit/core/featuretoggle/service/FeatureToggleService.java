package io.nextunit.core.featuretoggle.service;

import io.nextunit.core.featuretoggle.entity.Configuration;
import io.nextunit.core.featuretoggle.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service provides methods to calculate the feature toggle.
 */
@Service
public class FeatureToggleService {
    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * Returns a boolean, if the requested feature is activated.
     *
     * @param featureName Feature name, which should be checked for activation.
     *
     * @return true if the feature is activated, false if not.
     */
    public boolean isFeatureActive(String featureName) {
        Configuration config = configurationRepository.findOne(featureName);
        return config != null;
    }

    /**
     * Activates a feature with storing the name in the feature configuration table.
     * @param featureName Feature name, which should be activated.
     */
    public void activateFeature(String featureName) {
        if (!isFeatureActive(featureName)) {
            configurationRepository.save(Configuration.builder().name(featureName).build());
        }
    }

    /**
     * Deactivates a feature with storing the name in the feature configuration table.
     * @param featureName Feature name, which should be activated.
     */
    public void deactivateFeature(String featureName) {
        if (isFeatureActive(featureName)) {
            configurationRepository.delete(featureName);
        }
    }
}
