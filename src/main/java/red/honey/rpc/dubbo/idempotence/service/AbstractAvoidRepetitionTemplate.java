package red.honey.rpc.dubbo.idempotence.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import red.honey.rpc.dubbo.idempotence.entity.Invocation;
import red.honey.rpc.dubbo.idempotence.exception.RepeatException;

/**
 * @author yangzhijie
 */
@Slf4j
public abstract class AbstractAvoidRepetitionTemplate {


    public final boolean checkForRepetition(ProceedingJoinPoint pjp) {
        try {
            Invocation invocation = this.convert(pjp);
            return doCheck(invocation);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("防重校验出现异常", e);
            }
            throw new RepeatException("防重校验出现异常", e);
        }
    }

    /**
     * 切面切点装换为框架核心Invocation
     *
     * @param pjp 切面切点
     * @return Invocation
     */
    protected abstract Invocation convert(ProceedingJoinPoint pjp);

    /**
     * 针对Invocation进行防重校验
     *
     * @param invocation Invocation
     * @return 是否重复
     */
    protected abstract boolean doCheck(Invocation invocation);

}
