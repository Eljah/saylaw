package com.github.eljah.saylaw.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by eljah32 on 7/29/2018.
 */
public class DocxViewResolver implements ViewResolver {

    @Autowired
    DocxBinaryGenerator docxBinaryGenerator;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {

        return new DocxView(s,docxBinaryGenerator);
    }
}