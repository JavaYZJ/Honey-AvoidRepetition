# Honey-AvoidRepetition
这是一款小而巧的防重组件，旨在解决RPC接口的幂等性问题。当前版本仅支持了Dubbo，后续将迭代支持gRPC、Thrift等等。
组件借助Dubbo的SPI机制，提高框架的扩展能力，以便支持开发者更多的防重/幂等性策略。组件目前仅内置了基于Redis的防重策略。

> 组件不强依赖与Redis，组件仅直接依赖与你的防重策略实现上的依赖。

 - [x] starter开箱即用
 - [x] SPI机制，插件式高扩展能力

## 使用方式
第一步：引入依赖

```java
 <dependency>
     <groupId>red.honey</groupId>
     <artifactId>rpc-dubbo-idempotence</artifactId>
     <version>0.0.1-SNAPSHOT</version>
 </dependency>
```

第二步：启动类打上注解EnableHoneyDeRepeat，开启防重功能
```java
@SpringBootApplication
@EnableHoneyDeRepeat
public class RpcDubboIdempotenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDubboIdempotenceApplication.class, args);
    }

}
```
第三步：在你需要保证防重/幂等性的方法上打上注解AvoidRepetition，如果你的整个类的所有方法都需要保证防重/幂等性，可直接将注解打在类上。

```java
	@AvoidRepetition
    public void test(String params) {
        log.info("I need to avoid repeat");
    }
```
> 注意！此时启用的将是内置的基于Redis的防重/幂等性校验策略，需配置你的Redis配置~
> 另外因为目前仅支持dubbo项目，所以你需配置你的dubbo相关的配置~


## 扩展
开发者想自定义防重/幂等性策略，如何do?  组件利用Dubbo的SPI机制，可以很好支持扩展。

```java
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
```
组件定义模板方法AbstractAvoidRepetitionTemplate，方便开发者扩展防重/幂等实现

```java
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
```
所以，开发者仅需自定义防重/幂等实现类 继承模板方法，实现RepetitionService接口即可，可以参见内置实现RedisAvoidRepetitionImpl

```java
public class RedisAvoidRepetitionImpl extends AbstractAvoidRepetitionTemplate implements RepetitionService{
}
```
最后不要忘记配置你的实现类，以让SPI发现加载
![SPI发现实现类](https://img-blog.csdnimg.cn/9a7873a91cf14bfd9ddaa4b1e47b9d80.png)


