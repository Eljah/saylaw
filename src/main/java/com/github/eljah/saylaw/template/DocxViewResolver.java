package com.github.eljah.saylaw.template;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by eljah32 on 7/29/2018.
 */
public class DocxViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {

        return new DocxView(s);
    }
}