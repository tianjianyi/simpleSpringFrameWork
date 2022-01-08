# simpleSpringFrameWork
一个用来学习spring知识用的简单玩具级spring框架
手写简易版仿Spring框架，实现了IOC，AOP，MVC等模块，用来学习和加深对Spring框架的理解。代码简单结构清晰。

##### 1.ICO和依赖注入

用单例模式实现了Bean的容器类，通过反射可以通过Component，Controller，Repository，Service等注解进行类的生成和注入，并且可以通过@Autowired注解方式进行注入，可以通过指定@Autowired的value值来进行不同实例的依赖注入。

##### 2.AOP

基于CGLib动态代理实现了AOP（面向切面编程），使用@Aspect注解来配置切面，能实现对同一个方法的多次增强，并通过@Order注解配置增强的顺序，并且通过AspectJ框架的pointcut表达式进行灵活的切面表达式解析，提高了整个aop框架的灵活性，并使功能更加强大。

##### 3.MVC

实现了DispatcherServlet解析和处理请求的主要流程，能够从HttpServletRequest对@RequestMapping,@RequestParam注解的方法参数进行解析，并可以通过RequestMapping实现二级访问路径，并能将路径与Controller方法实例映射。通过责任链模式来依次调用处理器对请求进行处理，通过不同的渲染器对不同的请求及其处理的结果进行渲染。

