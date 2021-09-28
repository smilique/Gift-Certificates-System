package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class SpringMvcApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String PROFILE_PARAMETER = "spring.profiles.active";
    private static final String CURRENT_PROFILE = "dev";
    private static final String THROW_EXCEPTION_PARAMETER = "throwExceptionIfNoHandlerFound";
    private static final String THROW_EXCEPTIONS = "true";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{DevJdbcConfig.class, ProdJdbcConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter(PROFILE_PARAMETER, CURRENT_PROFILE);
        servletContext.setInitParameter(THROW_EXCEPTION_PARAMETER, THROW_EXCEPTIONS);
        servletContext.setInitParameter("server.error.whitelabel.enabled", "false");
        servletContext.setInitParameter("spring.mvc.throw-exception-if-no-handler-found","true");
        super.onStartup(servletContext);
    }
}
