package io.nextunit.core.featuretoggle.interceptor;

import io.nextunit.core.featuretoggle.annotation.FeatureToggle;
import io.nextunit.core.featuretoggle.service.FeatureToggleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FeatureToggleInterceptor prevents invoking a method, if there is a {@link FeatureToggle}
 * annotation and the feature is not activated.
 */
public class FeatureToggleInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private FeatureToggleService featureToggleService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            FeatureToggle annotation = ((HandlerMethod) handler)
                    .getMethodAnnotation(FeatureToggle.class);

            if (annotation != null) {
                logger.info("Feature toggle check for method " +
                        handler.toString());
                for (String featureName: annotation.value()) {
                    if (!featureToggleService.isFeatureActive(featureName)) {

                        logger.info("Feature " + featureName + " is not activated." +
                                        "Handler will not be invoked.");

                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
