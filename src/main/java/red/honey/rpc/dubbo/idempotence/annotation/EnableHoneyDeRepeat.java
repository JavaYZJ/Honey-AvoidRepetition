package red.honey.rpc.dubbo.idempotence.annotation;

import org.springframework.context.annotation.Import;
import red.honey.rpc.dubbo.idempotence.config.HoneyIdempotenceAutoConfig;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HoneyIdempotenceAutoConfig.class)
public @interface EnableHoneyDeRepeat {
}
