package io.nextunit.core.feature.interceptor;

import io.nextunit.core.feature.annotation.FeatureToggle;
import io.nextunit.core.feature.service.FeatureService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@SuppressWarnings("ConstantConditions")
@RunWith(PowerMockRunner.class)
public class FeatureToggleInterceptorTest {
    @InjectMocks
    private FeatureToggleInterceptor featureToggleInterceptor;

    @Mock
    private FeatureService featureServiceMock;

    @Mock
    private FeatureToggle featureToggleMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HandlerMethod handlerMethodMock;

    @Mock
    private Enumeration<String> headerNamesEnumerationMock;

    @Test(expected = NoHandlerFoundException.class)
    public void preHandleNotInvokeMethod() throws Exception {
        // GIVEN
        Mockito.when(handlerMethodMock.toString()).thenReturn("test-method-name");
        Mockito.when(handlerMethodMock.getMethodAnnotation(FeatureToggle.class))
                .thenReturn(featureToggleMock);
        Mockito.when(featureToggleMock.value()).thenReturn(new String[] { "testFeatureToggleName",
                        "testAnotherFeatureToggleName" });
        Mockito.when(featureServiceMock.isFeatureActive("testFeatureToggleName"))
                .thenReturn(true);
        Mockito.when(featureServiceMock.isFeatureActive("testAnotherFeatureToggleName"))
                .thenReturn(false);
        Mockito.when(requestMock.getMethod()).thenReturn("GET");
        Mockito.when(requestMock.getHeaderNames()).thenReturn(headerNamesEnumerationMock);
        Mockito.when(headerNamesEnumerationMock.hasMoreElements())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        Mockito.when(headerNamesEnumerationMock.nextElement())
                .thenReturn("header-1")
                .thenReturn("header-2");
        Mockito.when(requestMock.getHeader("header-1")).thenReturn("header-1-value");
        Mockito.when(requestMock.getHeader("header-2")).thenReturn("header-2-value");

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, handlerMethodMock);

        // THEN
        Assert.assertEquals(false, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.times(1))
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureServiceMock, Mockito.times(1))
                .isFeatureActive("testFeatureToggleName");
        Mockito.verify(featureServiceMock, Mockito.times(1))
                .isFeatureActive("testAnotherFeatureToggleName");
        Mockito.verify(requestMock, Mockito.times(1)).getHeader("header-1");
        Mockito.verify(requestMock, Mockito.times(1)).getHeader("header-2");
        Mockito.verify(headerNamesEnumerationMock, Mockito.times(2)).nextElement();
        Mockito.verify(headerNamesEnumerationMock, Mockito.times(3)).nextElement();
    }

    @Test
    public void preHandleInvokeMethodFeatureIsActive() throws Exception {
        // GIVEN
        Mockito.when(handlerMethodMock.toString()).thenReturn("test-method-name");
        Mockito.when(handlerMethodMock.getMethodAnnotation(FeatureToggle.class))
                .thenReturn(featureToggleMock);
        Mockito.when(featureToggleMock.value()).thenReturn(new String[] { "testFeatureToggleName",
                        "testAnotherFeatureToggleName" });
        Mockito.when(featureServiceMock.isFeatureActive("testFeatureToggleName"))
                .thenReturn(true);
        Mockito.when(featureServiceMock.isFeatureActive("testAnotherFeatureToggleName"))
                .thenReturn(true);

        // WHEN
        boolean isInvoked = featureToggleInterceptor
                .preHandle(requestMock, responseMock, handlerMethodMock);

        // THEN
        Assert.assertEquals(true, isInvoked);
        Mockito.verify(handlerMethodMock, Mockito.times(1))
                .getMethodAnnotation(FeatureToggle.class);
        Mockito.verify(featureServiceMock, Mockito.times(1))
                .isFeatureActive("testFeatureToggleName");
        Mockito.verify(featureServiceMock, Mockito.times(1))
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
        Mockito.verify(featureServiceMock, Mockito.never())
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
        Mockito.verify(featureServiceMock, Mockito.never())
                .isFeatureActive(Mockito.any());
    }
}
