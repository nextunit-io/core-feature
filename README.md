# core-feature-toggle
Spring boot feature toggle annotation to toggle features on and off. 

To use it just add the annotation ```@FeatureToggle({"feature-1", "feature-2"})``` at a method. E
.g.:

```
@RestController 
public class RestController {
    @RequestMapping("/")
    @FeatureToggle({"test-feature-1", "test-feature-2"})
    public String test() {
        return "test";
    }
}
``` 
