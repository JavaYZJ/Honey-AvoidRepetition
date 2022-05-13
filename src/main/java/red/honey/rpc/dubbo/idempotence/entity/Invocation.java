package red.honey.rpc.dubbo.idempotence.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author yangzhijie
 */
@Data
@Builder
public class Invocation {

    private String methodName;

    private String userId;

    private String appId;

    private String params;

}
