package red.honey.rpc.dubbo.idempotence.aspect;


import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import red.honey.rpc.dubbo.idempotence.exception.RepeatException;
import red.honey.rpc.dubbo.idempotence.service.RepetitionService;

/**
 * @author yangzhijie
 */
@Aspect
@Slf4j
public class AvoidRepeatAspect {


    private RepetitionService repetitionService;

    public AvoidRepeatAspect(RepetitionService repetitionService) {
        this.repetitionService = repetitionService;
    }

    @Pointcut("@annotation(red.honey.rpc.dubbo.idempotence.annotation.AvoidRepetition)")
    public void avoidRepeatPointcut() {
    }


    @Around("avoidRepeatPointcut()")
    public void avoidRepeatPointcutProcess(ProceedingJoinPoint pjp) {
        try {
            boolean repetition = repetitionService.isRepetition(pjp);
            if (!repetition) {
                pjp.proceed();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("方法:{} 触发防重", pjp.getSignature().getName());
                }
            }
        } catch (RepeatException e) {
            // 将其视为无重复放行，因为防重校验失败不能影响主业务流程执行
            if (log.isInfoEnabled()) {
                log.info("防重校验过程发生异常 : {}", e.getMessage());
            }
            try {
                pjp.proceed();
            } catch (Throwable throwable) {
                if (log.isErrorEnabled()) {
                    log.error("aop happen error", throwable);
                }
                throw new RuntimeException("aop 切面执行过程中，发生异常", throwable);
            }
        } catch (Throwable throwable) {
            if (log.isErrorEnabled()) {
                log.error("aop happen error", throwable);
            }
            throw new RuntimeException("aop 切面执行过程中，发生异常", throwable);
        }
    }

}
