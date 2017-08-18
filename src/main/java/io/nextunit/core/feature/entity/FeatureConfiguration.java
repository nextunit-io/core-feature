package io.nextunit.core.feature.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Configuration for feature toggle. If a feature is activated, the name with some data is
 * stored in the database. So we can toggle a feature at runtime of the application.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

@Entity
@Table(name = "feature_configuration",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class FeatureConfiguration {
    @Id
    @Column
    private String name;

    @Lob
    @Column
    @Setter
    private Map<String, Serializable> parameters;
}
