package io.nextunit.core.featuretoggle.repository;


import io.nextunit.core.featuretoggle.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Repository for the configuration
 */
@Transactional
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}
