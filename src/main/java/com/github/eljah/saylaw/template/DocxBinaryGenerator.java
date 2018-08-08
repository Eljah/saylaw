package com.github.eljah.saylaw.template;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.*;

/**
 * Created by eljah32 on 7/30/2018.
 */
@Component
@Slf4j
public class DocxBinaryGenerator {

    @Autowired
    Configuration configuration;

    public void prepareBinaryOutputStream(String template, Map<String, Object> model, OutputStream outputStream) throws IOException {
        XWPFDocument docx = new XWPFDocument(getClass().getClassLoader().getResourceAsStream("docs\\" + template + ".docx"));
        //using XWPFWordExtractor Class
        XWPFWordExtractor we = new XWPFWordExtractor(docx);

        int i = 0;
        for (XWPFParagraph p : docx.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    text = prepareReplacement(text, model);
                    List<String> items = Arrays.asList(text.split("\\s*\n\\s*"));
                    r.setText("",0);
                    for (String item: items)
                    {
                        r.setText(item);
                        r.addCarriageReturn();
                    }
                }
            }
        }
        for (XWPFTable tbl : docx.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            text = prepareReplacement(text, model);
                            List<String> items = Arrays.asList(text.split("\\s*\n\\s*"));
                            r.setText("",0);
                            for (String item: items)
                            {
                                r.setText(item);
                                r.addCarriageReturn();
                            }
                        }
                    }
                }
            }
        }
        docx.write(outputStream);
        ;
    }

    public String prepareReplacement(String textRun, Map<String, Object> model) {

        String toReturn = "";
        if (textRun != null) {
            try {
                log.info(String.format("Text in current word run: %s", textRun));
                Template t = new Template("templateName", new StringReader(textRun), configuration);
                //Template t = new Template("templateName", new StringReader("templatetemplate"), configuration);
                toReturn = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return toReturn;

    }

}
