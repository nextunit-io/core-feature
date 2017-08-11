package io.nextunit.core.featuretoggle;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ImportResource({"classpath:/configuration/feature-toggle-mvc.xml"})
@EntityScan(basePackages = "io.nextunit.core.featuretoggle.entity")
@EnableJpaRepositories
public class FeatureToggleConfiguration {
}
