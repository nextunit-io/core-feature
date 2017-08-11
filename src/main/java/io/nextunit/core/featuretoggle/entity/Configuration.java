package io.nextunit.core.featuretoggle.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
public class Configuration {
    @Id
    @Column
    private String name;

    @Lob
    @Column
    @Setter
    private Serializable value;
}
