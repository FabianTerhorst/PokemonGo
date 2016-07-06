package dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Documented
public @interface Subcomponent {
    Class<?>[] modules() default {};
}
