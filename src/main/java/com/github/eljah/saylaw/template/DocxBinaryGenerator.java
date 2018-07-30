package com.github.eljah.saylaw.template;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by eljah32 on 7/30/2018.
 */
@Component
public class DocxBinaryGenerator {

    public void prepareBinaryOutputStream(String template, Map<String, Object> model, OutputStream outputStream) throws IOException {
        XWPFDocument docx = new XWPFDocument(getClass().getClassLoader().getResourceAsStream("docs\\" + template + ".docx"));
        //using XWPFWordExtractor Class
        XWPFWordExtractor we = new XWPFWordExtractor(docx);
        System.out.println(we.getText());

        int i = 0;
        for (XWPFParagraph p : docx.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    //i++;
                    String text = r.getText(0);
                    System.out.println(text);
                    //if (text != null && text.contains("ID")) {
                    //    text = text.replace("ID", "idiot");
                    //    r.setText(text, 0);
                    //}
                    for (String variable : model.keySet()) {
                        //System.out.println("${"+variable+"}");
                        //System.out.println(model.get(variable).toString());
                        if (text != null) {
                            text = text.replace("${"+variable+"}", model.get(variable).toString());
                            r.setText(text, 0);
                        }
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
                            for (String variable : model.keySet()) {
                                //System.out.println("${"+variable+"}");
                                //System.out.println(model.get(variable).toString());
                                if (text != null) {
                                    text = text.replace("${"+variable+"}", model.get(variable).toString());
                                    r.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        docx.write(outputStream); ;
    }

}
