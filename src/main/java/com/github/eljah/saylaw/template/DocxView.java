package com.github.eljah.saylaw.template;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by eljah32 on 7/29/2018.
 */
public class DocxView extends AbstractDocxView {
    String template;
    DocxBinaryGenerator docxBinaryGenerator;

    DocxView(String template, DocxBinaryGenerator docxBinaryGenerator) {
        this.template = template;
        this.docxBinaryGenerator=docxBinaryGenerator;
    }


    @Override
    protected void buildDocxDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + model.get("__filename") + ".docx\"");
        docxBinaryGenerator.prepareBinaryOutputStream(this.template, model, response.getOutputStream());
    }

    public void buildDocxDocument(Map<String, Object> model, OutputStream outputStream) throws Exception {
        docxBinaryGenerator.prepareBinaryOutputStream(this.template, model, outputStream);
    }

}
