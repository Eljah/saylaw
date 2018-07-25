package com.github.eljah.saylaw.controller;

import com.github.eljah.saylaw.model.ExtractOfRegistry;
import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;
import com.github.eljah.saylaw.service.ShareService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Controller
public class ShareController {
    @Autowired
    ShareService shareService;

    @Value("${parse.xls.columns.share.date.format}")
    String dateFormat;

    @Value("${parse.xls.columns.sheet.to.use}")
    Integer sheetToUse;

    @Value("${parse.xls.columns.number.to.skip.until}")
    Integer skipColumns;

    @Value("${parse.xls.columns.share.name}")
    Integer shareNameColumn;

    @Value("${parse.xls.columns.share.area}")
    Integer shareAreaColumn;

    @Value("${parse.xls.columns.share.floor}")
    Integer shareFloorColumn;

    @Value("${parse.xls.columns.share.owner}")
    Integer shareOwnerColumn;

    @Value("${parse.xls.columns.share.ownership.certificate}")
    Integer shareOwnershipCetificateColumn;

    @Value("${parse.xls.columns.share.ownership.certificate.number}")
    Integer shareOwnershipCetificateColumnNumber;

    @Value("${parse.xls.columns.share.ownership.certificate.extract.number}")
    Integer shareOwnershipCetificateColumnExtractNumber;

    @Value("${parse.xls.columns.share.ownership.certificate.date}")
    Integer shareOwnershipCetificateColumnDate;

    @Value("${parse.xls.columns.share.ownership.certificate.extract.date}")
    Integer shareOwnershipCetificateColumnExtractDate;

    @Value("${parse.xls.columns.share.ownership.certificate.cadastral.number}")
    Integer shareOwnershipCetificateColumnCadastralNumber;

    @Value("${parse.xls.columns.share.ownership.certificate.full.address}")
    Integer shareOwnershipCetificateColumnFullAddress;

    @Value("${parse.xls.columns.share.ownership.certificate.file.name}")
    Integer shareOwnershipCetificateColumnFileName;

    @Value("${parse.xls.columns.share.owner.denominator}")
    Integer shareDenominatorColumn;

    @Value("${parse.xls.columns.share.owner.nominator}")
    Integer shareNominatorColumn;

    @GetMapping("/")
    public String index() {
        return "shareupload";
    }

    @Transactional
    @PostMapping("/shareupload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            Set<Share> shareList = new LinkedHashSet<>();
            InputStream stream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(sheetToUse);

            Iterator<Row> rowIterator = sheet.iterator();
            Map<String, Share> sharesMap = new HashMap<String, Share>() {
            };
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // Skip read heading
                if (row.getRowNum() <= skipColumns) {
                    continue;
                }
                if (row.getCell(shareNameColumn) != null && row.getCell(shareNameColumn).toString() != null)
                    if (row.getCell(shareNameColumn).toString() != "") {
                        Share share;
                        share = new Share();
                        share.setName(row.getCell(shareNameColumn).toString());
                        share.setType(Share.ShareType.RESIDENTAL); //todo fix proper parsing
                        share.setArea(new Double(row.getCell(shareAreaColumn).toString()));
                        share.setFloor(new Integer(row.getCell(shareFloorColumn).toString()));
                        share.setActive(true);
                        shareList.add(share);
                    } else break;
//                sharesMap.put(row.getCell(1).toString(),share);
            }

            for (Share share : shareList) {
                sharesMap.put(share.getName(), share);
            }

            rowIterator = sheet.iterator();
            List<OwnerShare> ownerShareList = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // Skip read heading
                if (row.getRowNum() <= skipColumns) {
                    continue;
                }
                if (row.getCell(shareNameColumn) != null && row.getCell(shareNameColumn).toString() != null)
                    if (row.getCell(shareNameColumn).toString() != "") {

                        String shareName = row.getCell(shareNameColumn).toString();
                        Owner owner = new Owner();
                        OwnerShare ownerShare = new OwnerShare();
                        ownerShareList.add(ownerShare);
                        String ownerFullName = "";
                        try {
                            ownerFullName = row.getCell(shareOwnerColumn).toString();
                            StringTokenizer st = new StringTokenizer(ownerFullName);
                            owner.setLastName(st.nextToken());
                            owner.setFirstName(st.nextToken());
                            String secondName = "";
                            if (st.hasMoreTokens()) {
                                secondName = secondName + st.nextToken();
                            }
                            if (st.hasMoreTokens()) {
                                secondName = secondName + " " + st.nextToken();
                            }
                            owner.setSecondName(secondName);
                        } catch (NullPointerException e) {
                            ownerFullName = "Исполком г. Казань";
                            owner.setLastName(ownerFullName);
                        } catch (java.util.NoSuchElementException elementException) {
                            ownerFullName = "Исполком г. Казань";
                            owner.setLastName(ownerFullName);
                        }

                        ownerShare.setActive(true);
                        ownerShare.setOwner(owner);
                        owner.setOwnerShare(ownerShare);
                        Share shareToAddToOwner = sharesMap.get(shareName);
                        if (shareToAddToOwner != null) {
                            ownerShare.setShare(shareToAddToOwner);
                            shareToAddToOwner.
                                    getOwnerShare().
                                    add(ownerShare);

                        }
                        ExtractOfRegistry extractOfRegistry = new ExtractOfRegistry();
                        extractOfRegistry.setOwnershipCertificate(row.getCell(shareOwnershipCetificateColumn).toString());
                        extractOfRegistry.setExtractNumber(row.getCell(shareOwnershipCetificateColumn).toString());
                        extractOfRegistry.setCadastralNumber(row.getCell(shareOwnershipCetificateColumnCadastralNumber).toString());
                        extractOfRegistry.setOwnershipCertificateNumber(row.getCell(shareOwnershipCetificateColumnNumber).toString());
                        Cell fullAddress = row.getCell(shareOwnershipCetificateColumnFullAddress);
                        if (fullAddress != null)
                            extractOfRegistry.setRegistryAddress(fullAddress.toString());
                        extractOfRegistry.setRegistryExtractFilename(row.getCell(shareOwnershipCetificateColumnFileName).toString());
                        try {
                            extractOfRegistry.setOwnershipCertificateDate(new SimpleDateFormat(dateFormat).parse(row.getCell(shareOwnershipCetificateColumnDate).toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            extractOfRegistry.setRegistryExtractDate(new SimpleDateFormat(dateFormat).parse(row.getCell(shareOwnershipCetificateColumnExtractDate).toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        extractOfRegistry.setExtractNumber(row.getCell(shareOwnershipCetificateColumnExtractNumber).toString());
                        ownerShare.setExtractOfRegistry(extractOfRegistry);

                        ownerShare.setShareNominator((int)Float.parseFloat(row.getCell(shareNominatorColumn).toString()));
                        ownerShare.setShareDenominator((int)Float.parseFloat(row.getCell(shareDenominatorColumn).toString()));
                        ownerShare.setShareValue((double) ownerShare.getShareNominator() / (double) ownerShare.getShareDenominator());
                    }
            }

//            Integer sharedShareCount = 0;
//            String previousShareName = "";
//            List<OwnerShare> currentSharedShare = new ArrayList<>();
//
//            for (OwnerShare ownerShare : ownerShareList) {
//                if (ownerShare.getShare() != null && ownerShare.getShare().getName() != null && ownerShare.getShare().getName().equals(previousShareName)) {
//                    sharedShareCount++;
//                    currentSharedShare.add(ownerShare);
//                    System.out.println(sharedShareCount);
//                } else {
//                    for (OwnerShare ownerShareCurrent : currentSharedShare) {
//                        ownerShareCurrent.setShareNominator(row.getCell(shareDenominatorColumn));
//                        ownerShareCurrent.setShareDenominator(sharedShareCount);
//                        ownerShareCurrent.setShareValue(1 / (double) sharedShareCount);
//                    }
//                    sharedShareCount = 0;
//                    currentSharedShare = new ArrayList<>();
//                    sharedShareCount++;
//                    currentSharedShare.add(ownerShare);
//                    System.out.println(sharedShareCount);
//                }
//                previousShareName = ownerShare.getShare().getName();
//            }
//
//            for (OwnerShare ownerShareCurrent : currentSharedShare) {
//                ownerShareCurrent.setShareNominator(1);
//                ownerShareCurrent.setShareDenominator(sharedShareCount);
//                ownerShareCurrent.setShareValue(1 / (double) sharedShareCount);
//            }

            shareService.createShares(shareList);
            shareService.createOwnerShares(ownerShareList);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

            shareService.calculateShareValues();
            shareService.calculateOwnerShareValues();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping("/showAll")
    public String showAll(Model model) {

        model.addAttribute("all", shareService.showAll());
        return "showAll";
    }

    @GetMapping("/addShare")
    public String addShare(Model model) {

        Share share=new Share();
        share.setActive(true);
        share.setType(Share.ShareType.RESIDENTAL);
        model.addAttribute("share", share);
        return "addShare";
    }

    @PostMapping("/addShare")
    @Transactional
    public String addShare(@ModelAttribute Share share) {
        share.setActive(true);
        shareService.save(share);
        shareService.createOwnerShares(share.getOwnerShare(),share);
        shareService.calculateShareValues();
        shareService.calculateOwnerShareValues();
        return "redirect:showAll";
    }
}
