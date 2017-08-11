package io.nextunit.core.featuretoggle.service;

import io.nextunit.core.featuretoggle.entity.Configuration;
import io.nextunit.core.featuretoggle.repository.ConfigurationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.modules.junit4.PowerMockRunner;

@SuppressWarnings("ConstantConditions")
@RunWith(PowerMockRunner.class)
public class FeatureToggleServiceTest {
    @InjectMocks
    private FeatureToggleService featureToggleService;

    @Mock
    private ConfigurationRepository configurationRepositoryMock;

    @Mock
    private Configuration configurationMock;

    @Captor
    private ArgumentCaptor<Configuration> configurationArgumentCaptor;

    private String featureName = "testFeatureName";

    @Test
    public void isFeatureActiveTrue() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        boolean isActive = featureToggleService.isFeatureActive(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Assert.assertTrue(isActive);
    }

    @Test
    public void isFeatureActiveFalse() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        boolean isActive = featureToggleService.isFeatureActive(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Assert.assertFalse(isActive);
    }

    @Test
    public void activateFeatureTrue() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureToggleService.activateFeature(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(configurationRepositoryMock, Mockito.times(1))
                .save(configurationArgumentCaptor.capture());

        Assert.assertEquals(featureName, configurationArgumentCaptor.getValue().getName());
    }

    @Test
    public void activateFeatureAlreadyActive() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        featureToggleService.activateFeature(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(configurationRepositoryMock, Mockito.never())
                .save(Mockito.any(Configuration.class));
    }

    @Test
    public void deactivateFeatureTrue() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        featureToggleService.deactivateFeature(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(configurationRepositoryMock, Mockito.times(1))
                .delete(featureName);
    }

    @Test
    public void deactivateFeatureAlreadyActive() {
        // GIVEN
        Mockito.when(configurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureToggleService.deactivateFeature(featureName);

        // THEN
        Mockito.verify(configurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(configurationRepositoryMock, Mockito.never())
                .delete(Mockito.anyString());
    }
}
