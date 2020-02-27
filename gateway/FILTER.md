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

## GatewayFilter工厂类
Spring gateway 有很多内置的filter工程类，通过配置文件可以直接使用。




## GatewayFilter : 需要通过spring.cloud.routes.filters 配置在具体路由下，
只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上


## GatewayFilterFactory


## GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，
最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，
它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，
系统初始化时加载，并作用在每个路由上
