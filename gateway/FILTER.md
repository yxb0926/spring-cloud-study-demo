# Filter 学习

```
在路由处理之前，需要经过pre类型的过滤器处理，处理返回响应之后，可以由
post类型的过滤器处理。

在pre类型的过滤器可以做参数效验、权限效验、流量监控、日志输出、协议转换等，
在post类型的过滤器中可以做响应内容、响应头的修改，日志输出，流量监控等。
```

## filter的作用


## filter生命周期
spring gateway 有 pre 和 post 两种类型的filter。客户端的请求
先经过pre类型的filter， 然后将请求转发到具体的后端服务，收到业务服务
的响应之后，再经过post类型的filter处理，最后返回响应到客户端。

## Filter 分类
- GatewayFilter: 只对指定的路由起作用,需要配置到具体的router规则中
- GlobalFilter: 全局过滤器，对所有的路由均起作用,无需进行配置

## 如何编写自定义的Filter
### GatewayFilter 编写:
自定义GatewayFilter又有两种实现方式，一种是直接 实现GatewayFilter接口，
另一种是 继承AbstractGatewayFilterFactory类 ,任意选一种即可

- 实现GatewayFilter接口
```aidl
package com.winture.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * token校验过滤器
 * @Version V1.0
 */
public class AuthorizeGatewayFilter implements GatewayFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "token";
    private static final String AUTHORIZE_UID = "uid";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        String uid = headers.getFirst(AUTHORIZE_UID);
        if (token == null) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (uid == null) {
            uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
        }

        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String authToken = stringRedisTemplate.opsForValue().get(uid);
        if (authToken == null || !authToken.equals(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}

```
如何使用AuthorizeGatewayFilter呢? 这个需要在Application 中调用
```aidl
package com.winture.gateway;

import com.winture.gateway.filter.AuthorizeGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(r ->
                r.path("/user/list")
                .uri("http://localhost:8077/api/user/list")
                .filters(new AuthorizeGatewayFilter())
                .id("user-service"))
            .build();
    }
}
```

- 继承AbstractGatewayFilterFactory类


### GlobalFilter 编写:
xxx




















## Reference
[1] https://juejin.im/post/5c17aa436fb9a049de6d482d
[2] https://segmentfault.com/a/1190000016227780
