package red.honey.rpc.dubbo.idempotence.annotation;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AvoidRepetition {

}
