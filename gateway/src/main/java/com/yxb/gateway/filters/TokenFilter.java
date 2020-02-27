package com.yxb.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*
* 自定义的全局filter，不用在配置文件中配置，
* 只需在application注入到Spring Ioc 即可在全部router生效
*
* GlobalFilter 特别适合做权限认证，日志打印，监控 等事情
* */
public class TokenFilter implements GlobalFilter, Ordered {

    Logger logger= LoggerFactory.getLogger( TokenFilter.class );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null || token.isEmpty()) {
            logger.info( "token is empty..." );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    /*
    * 设置该filter的优先级。
    * 数字约小优先级越高。
    * */
    @Override
    public int getOrder() {
        return -100;
    }
}
