package com.github.eljah.saylaw.template;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by eljah32 on 7/29/2018.
 */
public class DocxView extends AbstractDocxView {
    String template;

    DocxView(String template) {
        this.template = template;
    }

    @Override
    protected void buildDocxDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"" + model.get("__filename") + ".docx\"");

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

                            if (text != null && text.contains("needle")) {
                                text = text.replace("needle", "haystack");
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
        docx.write(response.getOutputStream());

    }
}
