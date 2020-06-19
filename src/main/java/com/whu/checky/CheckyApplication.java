package com.whu.checky;

import com.whu.checky.service.ParameterService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// 这个文件不改的话，添加war app一直不启动springboot这个项目，接口也是404
//public class CheckyApplication extends SpringBootServletInitializer {
public class CheckyApplication{
    @Autowired
    private ParameterService parameterService;

    public static void main(String[] args) {
        SpringApplication.run(CheckyApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        int httpsPort = Integer.parseInt(parameterService.getValueByParam("https").getParamValue());
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(httpsPort) {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                collection.addMethod("post");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors( redirectConnector(httpsPort));
        return tomcat;
    }

    private Connector redirectConnector(int httpsPort) {
        int httpPort = Integer.parseInt(parameterService.getValueByParam("http").getParamValue());
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return connector;
    }

    /*
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8090);
        return connector;
    }
    */



    /*
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CheckyApplication.class);
    }
    */
}
