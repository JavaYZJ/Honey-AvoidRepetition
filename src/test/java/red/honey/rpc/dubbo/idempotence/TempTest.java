package red.honey.rpc.dubbo.idempotence;

import lombok.extern.slf4j.Slf4j;
import red.honey.rpc.dubbo.idempotence.annotation.AvoidRepetition;

@Slf4j
public class TempTest {



    @AvoidRepetition
    public void test() {
        log.info("I need to avoid repeat");
    }
}
