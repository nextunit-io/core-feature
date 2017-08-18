package io.nextunit.core.feature.service;

import io.nextunit.core.feature.entity.FeatureConfiguration;
import io.nextunit.core.feature.exception.FeatureNotFoundException;
import io.nextunit.core.feature.repository.FeatureConfigurationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@RunWith(PowerMockRunner.class)
public class FeatureServiceTest {
    @InjectMocks
    private FeatureService featureService;

    @Mock
    private FeatureConfigurationRepository featureConfigurationRepositoryMock;

    @Mock
    private FeatureConfiguration configurationMock;

    @Captor
    private ArgumentCaptor<FeatureConfiguration> configurationArgumentCaptor;

    private String featureName = "testFeatureName";
    private String parameterKey = "test parameter key";
    private String parameterValue = "test parameter value";

    @Test
    public void isFeatureActiveTrue() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        boolean isActive = featureService.isFeatureActive(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Assert.assertTrue(isActive);
    }

    @Test
    public void isFeatureActiveFalse() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        boolean isActive = featureService.isFeatureActive(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Assert.assertFalse(isActive);
    }

    @Test
    public void activateFeatureTrue() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureService.activateFeature(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1))
                .save(configurationArgumentCaptor.capture());

        Assert.assertEquals(featureName, configurationArgumentCaptor.getValue().getName());
    }

    @Test
    public void activateFeatureAlreadyActive() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        featureService.activateFeature(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.never())
                .save(Mockito.any(FeatureConfiguration.class));
    }

    @Test
    public void deactivateFeatureTrue() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        featureService.deactivateFeature(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1))
                .delete(featureName);
    }

    @Test
    public void deactivateFeatureAlreadyActive() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureService.deactivateFeature(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.never())
                .delete(Mockito.anyString());
    }

    @Test
    public void getFeatureConfigurations() {
        // GIVEN
        List<FeatureConfiguration> configurationList = Arrays.asList(
                configurationMock, configurationMock);
        Mockito.when(featureConfigurationRepositoryMock.findAll()).thenReturn(configurationList);

        // WHEN
        List<FeatureConfiguration> configurations = featureService.getFeatureConfigurations();

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findAll();
        Assert.assertEquals(configurationList, configurations);
    }

    @Test
    public void addSingleFeatureParametersSuccess() {
        // GIVEN
        HashMap<String, Serializable> parameters = new HashMap<>();
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);
        Mockito.when(configurationMock.getParameters()).thenReturn(null).thenReturn(parameters);

        // WHEN
        featureService.addFeatureParameters(featureName, parameterKey, parameterValue);

        // THEN
        Mockito.verify(configurationMock, Mockito.times(2)).getParameters();
        Mockito.verify(configurationMock, Mockito.times(1)).setParameters(Mockito.anyMap());
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1))
                .save(configurationMock);

        Assert.assertEquals(1, parameters.size());
        Assert.assertTrue(parameters.containsKey(parameterKey));
        Assert.assertEquals(parameterValue, parameters.get(parameterKey));
    }

    @Test(expected = FeatureNotFoundException.class)
    public void addSingleFeatureParametersConfigurationNotFound() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureService.addFeatureParameters(featureName, parameterKey, parameterValue);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
    }

    @Test
    public void addMapFeatureParametersSuccess() {
        // GIVEN
        HashMap<String, Serializable> parameters = new HashMap<>();
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);
        Mockito.when(configurationMock.getParameters()).thenReturn(null).thenReturn(parameters);

        // WHEN
        featureService.addFeatureParameters(featureName, new HashMap<String, Serializable>() {{
            put(parameterKey, parameterValue);
        }});

        // THEN
        Mockito.verify(configurationMock, Mockito.times(2)).getParameters();
        Mockito.verify(configurationMock, Mockito.times(1)).setParameters(Mockito.anyMap());
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1))
                .save(configurationMock);

        Assert.assertEquals(1, parameters.size());
        Assert.assertTrue(parameters.containsKey(parameterKey));
        Assert.assertEquals(parameterValue, parameters.get(parameterKey));
    }

    @Test(expected = FeatureNotFoundException.class)
    public void addMapFeatureParametersConfigurationNotFound() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureService.addFeatureParameters(featureName, new HashMap<String, Serializable>() {{
            put(parameterKey, parameterValue);
        }});

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
    }

    @Test
    public void getFeatureConfigurationSuccess() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(configurationMock);

        // WHEN
        FeatureConfiguration config = featureService.getFeatureConfiguration(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);

        Assert.assertEquals(config, configurationMock);
    }

    @Test(expected = FeatureNotFoundException.class)
    public void getFeatureConfigurationNotFound() {
        // GIVEN
        Mockito.when(featureConfigurationRepositoryMock.findOne(featureName))
                .thenReturn(null);

        // WHEN
        featureService.getFeatureConfiguration(featureName);

        // THEN
        Mockito.verify(featureConfigurationRepositoryMock, Mockito.times(1)).findOne(featureName);
    }
}
