package red.honey.rpc.dubbo.idempotence.service.impl;

import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import red.honey.rpc.dubbo.idempotence.entity.Invocation;
import red.honey.rpc.dubbo.idempotence.service.AbstractAvoidRepetitionTemplate;
import red.honey.rpc.dubbo.idempotence.service.RepetitionService;

import java.util.Arrays;

/**
 * @author yangzhijie
 */
public class RedisAvoidRepetitionImpl extends AbstractAvoidRepetitionTemplate implements RepetitionService {

    /**
     * 1000毫秒过期，1000ms内的重复请求会认为重复
     */
    private final long expireTime = 1000;
    private final long expireAt = System.currentTimeMillis() + expireTime;

    private StringRedisTemplate stringRedisTemplate;

    public RedisAvoidRepetitionImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean isRepetition(ProceedingJoinPoint pjp) {
        if (pjp == null) {
            throw new IllegalArgumentException("pjp must not be null");
        }
        return checkForRepetition(pjp);
    }

    @Override
    protected Invocation convert(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        String method = pjp.getSignature().getName();
        String userId = RpcContext.getContext().getAttachment("userId");
        String appId = RpcContext.getContext().getAttachment("appId");

        return Invocation.builder().methodName(method).userId(userId).appId(appId).params(Arrays.toString(args)).build();
    }

    @Override
    protected boolean doCheck(Invocation invocation) {
        return checkByRedis(invocation);
    }


    private boolean checkByRedis(Invocation invocation) {
        String key = "honey-idempotence: A=" + invocation.getAppId() + "U=" + invocation.getUserId() + "M=" + invocation.getMethodName() + "P=" + invocation.getParams();
        String val = "expireAt@" + expireAt;
        // NOTE:直接SETNX不支持带过期时间，所以设置+过期不是原子操作，极端情况下可能设置了就不过期了
        // 后面相同请求可能会误以为需要去重，所以这里使用底层API，保证SETNX+过期时间是原子操作
        Boolean firstSet = stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(key.getBytes(), val.getBytes(), Expiration.milliseconds(expireTime),
                RedisStringCommands.SetOption.SET_IF_ABSENT));

        final boolean isConsiderDup;
        isConsiderDup = firstSet == null || !firstSet;
        return isConsiderDup;
    }
}
