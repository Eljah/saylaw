package com.github.eljah.saylaw.template;


import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by eljah32 on 7/29/2018.
 */
abstract public class AbstractDocxView extends AbstractView {

    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";


    private String url;


    public AbstractDocxView() {
        setContentType(CONTENT_TYPE);
    }


    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }


    @Override
    protected final void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        buildDocxDocument(model, request, response);
    }

    public abstract void buildDocxDocument(Map<String, Object> model, OutputStream outputStream) throws Exception;

    protected abstract void buildDocxDocument(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception;


}