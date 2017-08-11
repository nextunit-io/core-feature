package io.nextunit.core.featuretoggle.interceptor;

import io.nextunit.core.featuretoggle.annotation.FeatureToggle;
import io.nextunit.core.featuretoggle.service.FeatureToggleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("ConstantConditions")
@RunWith(PowerMockRunner.class)
public class FeatureToggleInterceptorTest {
    @InjectMocks
    private FeatureToggleInterceptor featureToggleInterceptor;

    @Mock
    private FeatureToggleService featureToggleServiceMock;

    @Mock
    private FeatureToggle featureToggleMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HandlerMethod handlerMethodMock;

    @Test
    public void preHandleNotInvokeMethod() throws Exception {
        // GIVEN
        Mockito.when(handlerMethodMock.toString()).thenReturn("test-method-name");
        Mockito.when(handlerMethodMock.getMethodAnnotation(FeatureToggle.class))
                .thenReturn(featureToggleMock);
        Mockito.when(featureToggleMock.value()).thenReturn(new String[] { "testFeatureToggleName",
                        "testAnotherFeatureToggleName" });
        Mockito.when(featureToggleServiceMock.isFeatureActive("testFeatureToggleName"))
                .thenReturn(true);
        Mockito.when(featureToggleServiceMock.isFeatureActive("testAnotherFeatureToggleName"))
                .thenReturn(false);

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, handlerMethodMock);

        // THEN
        Assert.assertEquals(false, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.times(1))
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureToggleServiceMock, Mockito.times(1))
                .isFeatureActive("testFeatureToggleName");
        Mockito.verify(featureToggleServiceMock, Mockito.times(1))
                .isFeatureActive("testAnotherFeatureToggleName");
    }

    @Test
    public void preHandleInvokeMethodFeatureIsActive() throws Exception {
        // GIVEN
        Mockito.when(handlerMethodMock.toString()).thenReturn("test-method-name");
        Mockito.when(handlerMethodMock.getMethodAnnotation(FeatureToggle.class))
                .thenReturn(featureToggleMock);
        Mockito.when(featureToggleMock.value()).thenReturn(new String[] { "testFeatureToggleName",
                        "testAnotherFeatureToggleName" });
        Mockito.when(featureToggleServiceMock.isFeatureActive("testFeatureToggleName"))
                .thenReturn(true);
        Mockito.when(featureToggleServiceMock.isFeatureActive("testAnotherFeatureToggleName"))
                .thenReturn(true);

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, handlerMethodMock);

        // THEN
        Assert.assertEquals(true, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.times(1))
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureToggleServiceMock, Mockito.times(1))
                .isFeatureActive("testFeatureToggleName");
        Mockito.verify(featureToggleServiceMock, Mockito.times(1))
                .isFeatureActive("testAnotherFeatureToggleName");
    }

    @Test
    public void preHandleInvokeMethodNoFeatureAnnotation() throws Exception {
        // GIVEN
        Mockito.when(handlerMethodMock.toString()).thenReturn("test-method-name");
        Mockito.when(handlerMethodMock.getMethodAnnotation(FeatureToggle.class))
                .thenReturn(null);

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, handlerMethodMock);

        // THEN
        Assert.assertEquals(true, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.times(1))
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureToggleServiceMock, Mockito.never())
                .isFeatureActive(Mockito.any());
    }

    @Test
    public void preHandleInvokeMethodNotAHandler() throws Exception {
        // GIVEN
        // Nothing

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, "test");

        // THEN
        Assert.assertEquals(true, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.never())
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureToggleServiceMock, Mockito.never())
                .isFeatureActive(Mockito.any());
    }
}
