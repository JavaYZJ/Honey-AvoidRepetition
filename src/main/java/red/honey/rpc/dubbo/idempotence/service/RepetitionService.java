package red.honey.rpc.dubbo.idempotence.service;

import org.apache.dubbo.common.extension.SPI;
import org.aspectj.lang.ProceedingJoinPoint;
import red.honey.rpc.dubbo.idempotence.exception.RepeatException;


/**
 * @author yangzhijie
 */
@SPI("redis")
public interface RepetitionService {


    /**
     * check it whether is repeat
     *
     * @param pjp the aop aspect ProceedingJoinPoint
     * @return the result of repetition
     */
    boolean isRepetition(ProceedingJoinPoint pjp) throws RepeatException;
}
