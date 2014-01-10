newrelic-rest
=============

This is the simplest possible application that would cause an exception when deployed on Wildfly with New Relic Java Agent. 
Without the New Relic Agent, this is the expected behavior:

	$ curl localhost:8080/newrelic-rest-problem/v1/echo -v
	* Adding handle: conn: 0x1e5bd60
	* Adding handle: send: 0
	* Adding handle: recv: 0
	* Curl_addHandleToPipeline: length: 1
	* - Conn 0 (0x1e5bd60) send_pipe: 1, recv_pipe: 0
	* About to connect() to localhost port 8080 (#0)
	*   Trying 127.0.0.1...
	* Connected to localhost (127.0.0.1) port 8080 (#0)
	> GET /newrelic-rest-problem/v1/echo HTTP/1.1
	> User-Agent: curl/7.32.0
	> Host: localhost:8080
	> Accept: */*
	> 
	< HTTP/1.1 200 OK
	< Connection: keep-alive
	< Content-Type: application/octet-stream
	< Content-Length: 14
	< 
	* Connection #0 to host localhost left intact
	echo echo echo

With the new Relic Agent, however, this is what we see on the client:

	$ curl localhost:8080/newrelic-rest-problem/v1/echo -I
	HTTP/1.1 500 Internal Server Error
	Connection: keep-alive
	Content-Type: text/html; charset=UTF-8
	Content-Length: 9865

And this is logged on the server:

	[31m10:23:32,094 ERROR [io.undertow.request] (default task-1) UT005023: Exception handling request to /newrelic-rest-problem/v1/echo: java.lang.annotation.AnnotationFormatError: Duplicate annotation for class: interface com.newrelic.agent.instrumentation.InstrumentedMethod: @com.newrelic.agent.instrumentation.InstrumentedMethod(dispatcher=false, instrumentationType=BuiltIn, instrumentationName=com.newrelic.agent.instrumentation.webservices.RestAnnotationVisitor)
		at sun.reflect.annotation.AnnotationParser.parseAnnotations2(AnnotationParser.java:94) [rt.jar:1.7.0_45]
		at sun.reflect.annotation.AnnotationParser.parseAnnotations(AnnotationParser.java:70) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.declaredAnnotations(Method.java:714) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.getDeclaredAnnotations(Method.java:707) [rt.jar:1.7.0_45]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findConstraints(AnnotationMetaDataProvider.java:468) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findExecutableMetaData(AnnotationMetaDataProvider.java:313) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMetaData(AnnotationMetaDataProvider.java:294) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMethodMetaData(AnnotationMetaDataProvider.java:280) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.retrieveBeanConfiguration(AnnotationMetaDataProvider.java:143) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfiguration(AnnotationMetaDataProvider.java:130) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfigurationForHierarchy(AnnotationMetaDataProvider.java:113) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.createBeanMetaData(BeanMetaDataManager.java:187) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.getBeanMetaData(BeanMetaDataManager.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.engine.ValidatorImpl.validate(ValidatorImpl.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.jboss.resteasy.plugins.validation.GeneralValidatorImpl.validate(GeneralValidatorImpl.java:68) [resteasy-validator-provider-11-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTarget(ResourceMethodInvoker.java:265) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:234) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:221) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:356) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:179) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.ServletContainerDispatcher.service(ServletContainerDispatcher.java:220) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:56) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:51) [resteasy-jaxrs-3.0.6.Final.jar:]
		at javax.servlet.http.HttpServlet.service(HttpServlet.java:790) [jboss-servlet-api_3.1_spec-1.0.0.Final.jar:1.0.0.Final]
		at io.undertow.servlet.handlers.ServletHandler.handleRequest(ServletHandler.java:87) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletSecurityRoleHandler.handleRequest(ServletSecurityRoleHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletDispatchingHandler.handleRequest(ServletDispatchingHandler.java:36) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.SecurityContextAssociationHandler.handleRequest(SecurityContextAssociationHandler.java:70)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.SSLInformationAssociationHandler.handleRequest(SSLInformationAssociationHandler.java:113) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AuthenticationCallHandler.handleRequest(AuthenticationCallHandler.java:52) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AbstractConfidentialityHandler.handleRequest(AbstractConfidentialityHandler.java:45) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletConfidentialityConstraintHandler.handleRequest(ServletConfidentialityConstraintHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.CachedAuthenticatedSessionHandler.handleRequest(CachedAuthenticatedSessionHandler.java:67) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.SecurityInitialHandler.handleRequest(SecurityInitialHandler.java:70) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.jacc.JACCContextIdHandler.handleRequest(JACCContextIdHandler.java:61)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.handleFirstRequest(ServletInitialHandler.java:240) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.dispatchRequest(ServletInitialHandler.java:227) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.access$000(ServletInitialHandler.java:73) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler$1.handleRequest(ServletInitialHandler.java:146) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.Connectors.executeRootHandler(Connectors.java:164) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.HttpServerExchange$1.run(HttpServerExchange.java:654) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_45]
		at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_45]
		at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_45]

	[0m[31m10:23:48,022 ERROR [io.undertow.request] (default task-2) UT005023: Exception handling request to /newrelic-rest-problem/v1/echo: java.lang.annotation.AnnotationFormatError: Duplicate annotation for class: interface com.newrelic.agent.instrumentation.InstrumentedMethod: @com.newrelic.agent.instrumentation.InstrumentedMethod(dispatcher=false, instrumentationType=BuiltIn, instrumentationName=com.newrelic.agent.instrumentation.webservices.RestAnnotationVisitor)
		at sun.reflect.annotation.AnnotationParser.parseAnnotations2(AnnotationParser.java:94) [rt.jar:1.7.0_45]
		at sun.reflect.annotation.AnnotationParser.parseAnnotations(AnnotationParser.java:70) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.declaredAnnotations(Method.java:714) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.getDeclaredAnnotations(Method.java:707) [rt.jar:1.7.0_45]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findConstraints(AnnotationMetaDataProvider.java:468) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findExecutableMetaData(AnnotationMetaDataProvider.java:313) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMetaData(AnnotationMetaDataProvider.java:294) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMethodMetaData(AnnotationMetaDataProvider.java:280) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.retrieveBeanConfiguration(AnnotationMetaDataProvider.java:143) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfiguration(AnnotationMetaDataProvider.java:130) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfigurationForHierarchy(AnnotationMetaDataProvider.java:113) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.createBeanMetaData(BeanMetaDataManager.java:187) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.getBeanMetaData(BeanMetaDataManager.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.engine.ValidatorImpl.validate(ValidatorImpl.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.jboss.resteasy.plugins.validation.GeneralValidatorImpl.validate(GeneralValidatorImpl.java:68) [resteasy-validator-provider-11-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTarget(ResourceMethodInvoker.java:265) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:234) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:221) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:356) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:179) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.ServletContainerDispatcher.service(ServletContainerDispatcher.java:220) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:56) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:51) [resteasy-jaxrs-3.0.6.Final.jar:]
		at javax.servlet.http.HttpServlet.service(HttpServlet.java:790) [jboss-servlet-api_3.1_spec-1.0.0.Final.jar:1.0.0.Final]
		at io.undertow.servlet.handlers.ServletHandler.handleRequest(ServletHandler.java:87) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletSecurityRoleHandler.handleRequest(ServletSecurityRoleHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletDispatchingHandler.handleRequest(ServletDispatchingHandler.java:36) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.SecurityContextAssociationHandler.handleRequest(SecurityContextAssociationHandler.java:70)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.SSLInformationAssociationHandler.handleRequest(SSLInformationAssociationHandler.java:113) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AuthenticationCallHandler.handleRequest(AuthenticationCallHandler.java:52) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AbstractConfidentialityHandler.handleRequest(AbstractConfidentialityHandler.java:45) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletConfidentialityConstraintHandler.handleRequest(ServletConfidentialityConstraintHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.CachedAuthenticatedSessionHandler.handleRequest(CachedAuthenticatedSessionHandler.java:67) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.SecurityInitialHandler.handleRequest(SecurityInitialHandler.java:70) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.jacc.JACCContextIdHandler.handleRequest(JACCContextIdHandler.java:61)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.handleFirstRequest(ServletInitialHandler.java:240) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.dispatchRequest(ServletInitialHandler.java:227) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.access$000(ServletInitialHandler.java:73) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler$1.handleRequest(ServletInitialHandler.java:146) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.Connectors.executeRootHandler(Connectors.java:164) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.HttpServerExchange$1.run(HttpServerExchange.java:654) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_45]
		at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_45]
		at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_45]

	[0m[31m10:23:51,574 ERROR [io.undertow.request] (default task-3) UT005023: Exception handling request to /newrelic-rest-problem/v1/echo: java.lang.annotation.AnnotationFormatError: Duplicate annotation for class: interface com.newrelic.agent.instrumentation.InstrumentedMethod: @com.newrelic.agent.instrumentation.InstrumentedMethod(dispatcher=false, instrumentationType=BuiltIn, instrumentationName=com.newrelic.agent.instrumentation.webservices.RestAnnotationVisitor)
		at sun.reflect.annotation.AnnotationParser.parseAnnotations2(AnnotationParser.java:94) [rt.jar:1.7.0_45]
		at sun.reflect.annotation.AnnotationParser.parseAnnotations(AnnotationParser.java:70) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.declaredAnnotations(Method.java:714) [rt.jar:1.7.0_45]
		at java.lang.reflect.Method.getDeclaredAnnotations(Method.java:707) [rt.jar:1.7.0_45]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findConstraints(AnnotationMetaDataProvider.java:468) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findExecutableMetaData(AnnotationMetaDataProvider.java:313) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMetaData(AnnotationMetaDataProvider.java:294) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMethodMetaData(AnnotationMetaDataProvider.java:280) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.retrieveBeanConfiguration(AnnotationMetaDataProvider.java:143) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfiguration(AnnotationMetaDataProvider.java:130) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfigurationForHierarchy(AnnotationMetaDataProvider.java:113) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.createBeanMetaData(BeanMetaDataManager.java:187) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.metadata.BeanMetaDataManager.getBeanMetaData(BeanMetaDataManager.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.hibernate.validator.internal.engine.ValidatorImpl.validate(ValidatorImpl.java:152) [hibernate-validator-5.0.2.Final.jar:5.0.2.Final]
		at org.jboss.resteasy.plugins.validation.GeneralValidatorImpl.validate(GeneralValidatorImpl.java:68) [resteasy-validator-provider-11-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTarget(ResourceMethodInvoker.java:265) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:234) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:221) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:356) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:179) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.ServletContainerDispatcher.service(ServletContainerDispatcher.java:220) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:56) [resteasy-jaxrs-3.0.6.Final.jar:]
		at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:51) [resteasy-jaxrs-3.0.6.Final.jar:]
		at javax.servlet.http.HttpServlet.service(HttpServlet.java:790) [jboss-servlet-api_3.1_spec-1.0.0.Final.jar:1.0.0.Final]
		at io.undertow.servlet.handlers.ServletHandler.handleRequest(ServletHandler.java:87) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletSecurityRoleHandler.handleRequest(ServletSecurityRoleHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletDispatchingHandler.handleRequest(ServletDispatchingHandler.java:36) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.SecurityContextAssociationHandler.handleRequest(SecurityContextAssociationHandler.java:70)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.SSLInformationAssociationHandler.handleRequest(SSLInformationAssociationHandler.java:113) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AuthenticationCallHandler.handleRequest(AuthenticationCallHandler.java:52) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.AbstractConfidentialityHandler.handleRequest(AbstractConfidentialityHandler.java:45) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.ServletConfidentialityConstraintHandler.handleRequest(ServletConfidentialityConstraintHandler.java:61) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.security.CachedAuthenticatedSessionHandler.handleRequest(CachedAuthenticatedSessionHandler.java:67) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.security.handlers.SecurityInitialHandler.handleRequest(SecurityInitialHandler.java:70) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at org.wildfly.extension.undertow.security.jacc.JACCContextIdHandler.handleRequest(JACCContextIdHandler.java:61)
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:25) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.handleFirstRequest(ServletInitialHandler.java:240) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.dispatchRequest(ServletInitialHandler.java:227) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler.access$000(ServletInitialHandler.java:73) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.servlet.handlers.ServletInitialHandler$1.handleRequest(ServletInitialHandler.java:146) [undertow-servlet-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.Connectors.executeRootHandler(Connectors.java:164) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at io.undertow.server.HttpServerExchange$1.run(HttpServerExchange.java:654) [undertow-core-1.0.0.Beta30.jar:1.0.0.Beta30]
		at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_45]
		at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_45]
		at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_45]

