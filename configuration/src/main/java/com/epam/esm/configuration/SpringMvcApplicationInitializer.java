package com.epam.esm.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class SpringMvcApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String THROW_EXCEPTION_PARAMETER = "throwExceptionIfNoHandlerFound";
    private static final String THROW_EXCEPTIONS = "true";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter(THROW_EXCEPTION_PARAMETER, THROW_EXCEPTIONS);
        super.onStartup(servletContext);
    }
}
