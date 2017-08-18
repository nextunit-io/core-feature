package io.nextunit.core.feature.interceptor;

import io.nextunit.core.feature.annotation.FeatureToggle;
import io.nextunit.core.feature.service.FeatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * FeatureToggleInterceptor prevents invoking a method, if there is a {@link FeatureToggle}
 * annotation and the feature is not activated.
 */
public class FeatureToggleInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private FeatureService featureService;

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
                    if (!featureService.isFeatureActive(featureName)) {

                        logger.info("Feature " + featureName + " is not activated." +
                                        "Handler will not be invoked.");

                        HttpHeaders headers = new HttpHeaders();
                        Collections.list(request.getHeaderNames()).forEach(headerName -> {
                            headers.add(headerName, request.getHeader(headerName));
                        });

                        throw new NoHandlerFoundException(
                                request.getMethod(),
                                request.getRequestURI(),
                                headers
                        );
                    }
                }
            }
        }

        return true;
    }
}
