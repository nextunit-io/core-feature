# core-feature
Spring boot feature configurations and toggle 

## Activate Feature toggle package

To activate the feature toggle, just use the ```@ComponentScan``` of spring boot and scan the component:

```Java
package io.nextunit.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan({"io.nextunit.core.featuretoggle"})
public class Application extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```

## Use the annotation

To use it just add the annotation ```@FeatureToggle({"feature-1", "feature-2"})``` at a method. 

E.g.:

```Java
@RestController 
public class RestController {
    @RequestMapping("/")
    @FeatureToggle({"feature-1", "feature-2"})
    public String test() {
        return "test";
    }
}
``` 

## Other functions

There are some other functions provided by the ```FeatureToggleService```. You can activate and deactivate features.

### Activate feature

To activate a feature just use the ```FeatureToggleService.activateFeature()``` function.

```Java
@Service
public class TestClass {
    @Autowired
    private FeatureToggleService featureToggleService;
    
    pubic void someMethod() {
        [...]
        featureToggleService.activateFeature("feature-name");
    }
}
```

### Deactivate feature

To deactivate a feature just use the ```FeatureToggleService.deactivateFeature()``` function.

```Java
@Service
public class TestClass {
    @Autowired
    private FeatureToggleService featureToggleService;
    
    pubic void someMethod() {
        [...]
        featureToggleService.deactivateFeature("feature-name");
    }
}
```
