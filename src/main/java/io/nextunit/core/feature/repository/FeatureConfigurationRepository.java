package io.nextunit.core.feature.repository;


import io.nextunit.core.feature.entity.FeatureConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Repository for the configuration
 */
@Transactional
public interface FeatureConfigurationRepository extends JpaRepository<FeatureConfiguration, String> {
}
