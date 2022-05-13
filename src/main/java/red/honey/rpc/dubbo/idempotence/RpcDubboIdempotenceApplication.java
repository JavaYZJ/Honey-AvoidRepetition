package red.honey.rpc.dubbo.idempotence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import red.honey.rpc.dubbo.idempotence.annotation.EnableHoneyDeRepeat;

/**
 * @author yangzhijie
 */
@SpringBootApplication
@EnableHoneyDeRepeat
public class RpcDubboIdempotenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDubboIdempotenceApplication.class, args);
    }

}
