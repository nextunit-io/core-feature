package io.nextunit.core.feature.service;

import io.nextunit.core.feature.entity.FeatureConfiguration;
import io.nextunit.core.feature.exception.FeatureNotFoundException;
import io.nextunit.core.feature.repository.FeatureConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service provides methods to calculate the feature toggle.
 */
@Service
public class FeatureService {
    @Autowired
    private FeatureConfigurationRepository featureConfigurationRepository;

    /**
     * Returns a boolean, if the requested feature is activated.
     *
     * @param featureName Feature name, which should be checked for activation.
     *
     * @return true if the feature is activated, false if not.
     */
    public boolean isFeatureActive(String featureName) {
        FeatureConfiguration config = featureConfigurationRepository.findOne(featureName);
        return config != null;
    }

    /**
     * Activates a feature with storing the name in the feature configuration table.
     *
     * @param featureName Feature name, which should be activated.
     */
    public void activateFeature(String featureName) {
        if (!isFeatureActive(featureName)) {
            featureConfigurationRepository.save(FeatureConfiguration.builder().name(featureName).build());
        }
    }

    /**
     * This adds the parameters to the feature.
     *
     * @param featureName The name of the feature where the parameters should be added.
     * @param parameters  The configuration parameters.
     */
    public void addFeatureParameters(String featureName, Map<String, Serializable> parameters) {
        FeatureConfiguration configuration = getFeatureConfiguration(featureName);
        addFeatureParameters(configuration, parameters);
    }

    /**
     * This adds the parameters to the feature.
     *
     * @param featureName The name of the feature where the parameters should be added.
     * @param key         The key of the parameter
     * @param parameter   The parameter which should be stored for the key at the feature
     */
    public void addFeatureParameters(String featureName, String key, Serializable parameter) {
        FeatureConfiguration configuration = getFeatureConfiguration(featureName);

        HashMap<String, Serializable> map = new HashMap<>();
        map.put(key, parameter);

        addFeatureParameters(configuration, map);
    }

    /**
     * Deactivates a feature with storing the name in the feature configuration table.
     *
     * @param featureName Feature name, which should be activated.
     */
    public void deactivateFeature(String featureName) {
        if (isFeatureActive(featureName)) {
            featureConfigurationRepository.delete(featureName);
        }
    }

    /**
     * Returns all feature configurations.
     *
     * @return List of {@link FeatureConfiguration}
     */
    public List<FeatureConfiguration> getFeatureConfigurations() {
        return featureConfigurationRepository.findAll();
    }

    /**
     * Returns a single feature configuration. If the configuration is not available it will be
     * thrown a {@link FeatureNotFoundException}.
     *
     * @param featureName The requested featureName
     *
     * @return FeatureConfiguration
     */
    public FeatureConfiguration getFeatureConfiguration(String featureName) {
        FeatureConfiguration featureConfiguration = featureConfigurationRepository
                .findOne(featureName);

        if (featureConfiguration == null) {
            throw FeatureNotFoundException
                    .builder()
                    .featureName(featureName)
                    .message(String.format("Feature '%s' not found.", featureName))
                    .build();
        }

        return featureConfiguration;
    }

    /**
     * This adds the parameters to the feature.
     *
     * @param configuration The configuration where the parameters should be added.
     * @param parameters    The configuration parameters.
     */
    private void addFeatureParameters(FeatureConfiguration configuration,
                                      Map<String, Serializable> parameters) {
        if (configuration.getParameters() == null) {
            configuration.setParameters(new HashMap<>());
        }

        configuration.getParameters().putAll(parameters);

        featureConfigurationRepository.save(configuration);
    }
}
