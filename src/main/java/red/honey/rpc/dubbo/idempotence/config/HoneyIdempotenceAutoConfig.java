package red.honey.rpc.dubbo.idempotence.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import red.honey.rpc.dubbo.idempotence.aspect.AvoidRepeatAspect;
import red.honey.rpc.dubbo.idempotence.service.RepetitionService;
import red.honey.rpc.dubbo.idempotence.service.impl.RedisAvoidRepetitionImpl;

/**
 * @author yangzhijie
 */
@Configurable
public class HoneyIdempotenceAutoConfig {

    @Bean
    @DependsOn("repetitionService")
    @ConditionalOnBean(RepetitionService.class)
    public AvoidRepeatAspect avoidRepeatAspect(RepetitionService repetitionService) {
        return new AvoidRepeatAspect(repetitionService);
    }

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public RepetitionService repetitionService(StringRedisTemplate stringRedisTemplate) {
        return new RedisAvoidRepetitionImpl(stringRedisTemplate);
    }
}
