package com.github.eljah.saylaw.controller;

import com.github.eljah.saylaw.model.ExtractOfRegistry;
import com.github.eljah.saylaw.model.Owner;
import com.github.eljah.saylaw.model.OwnerShare;
import com.github.eljah.saylaw.model.Share;
import com.github.eljah.saylaw.service.ShareService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.LockModeType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by eljah32 on 4/25/2018.
 */

@Controller
public class ShareController {
    @Autowired
    ShareService shareService;

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

            Set<Share> shareList=new LinkedHashSet<>();
            InputStream stream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            Map<String, Share> sharesMap=new HashMap<String, Share>(){};
            while (rowIterator.hasNext()) {
                Row row =rowIterator.next();
                // Skip read heading
                if (row.getRowNum() == 0) {
                    continue;
                }
                Share share;
                share = new Share();
                share.setName(row.getCell(1).toString());
                share.setType(Share.ShareType.RESIDENTAL); //todo fix proper parsing
                share.setArea(new Float(row.getCell(4).toString()));
                shareList.add(share);
//                sharesMap.put(row.getCell(1).toString(),share);
            }

            for (Share share: shareList)
            {
                sharesMap.put(share.getName(),share);
            }

            rowIterator = sheet.iterator();
            List<OwnerShare> ownerShareList=new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row =rowIterator.next();
                String shareName=row.getCell(1).toString();
                Owner owner=new Owner();
                OwnerShare ownerShare=new OwnerShare();
                ownerShareList.add(ownerShare);
                String ownerFullName="";
                try {
                    ownerFullName = row.getCell(3).toString();
                    StringTokenizer st = new StringTokenizer(ownerFullName);
                    owner.setLastName(st.nextToken());
                    owner.setFirstName(st.nextToken());
                    String secondName="";
                    if (st.hasMoreTokens())
                    {secondName=secondName+st.nextToken();}
                    if (st.hasMoreTokens())
                    {secondName=secondName+" "+st.nextToken();}
                    owner.setSecondName(secondName);
                }
                catch (NullPointerException e)
                {
                    ownerFullName="Исполком г. Казань";
                    owner.setLastName(ownerFullName);
                }
                catch (java.util.NoSuchElementException elementException)
                {
                    ownerFullName="Исполком г. Казань";
                    owner.setLastName(ownerFullName);
                }

                ownerShare.setActive(true);
                ownerShare.setOwner(owner);
                //share.setOwnerShare(ownerShare);
                Share shareToAddToOwner=sharesMap.get(shareName);
                if (shareToAddToOwner!=null) {
                    ownerShare.setShare(shareToAddToOwner);
                    shareToAddToOwner.
                            getOwnerShare().
                            add(ownerShare);

                }
                ExtractOfRegistry extractOfRegistry=new ExtractOfRegistry();
                extractOfRegistry.setOwnershipCertificate(row.getCell(5).toString());
                extractOfRegistry.setCadastralNumber(row.getCell(6).toString());
                extractOfRegistry.setRegistryAddress(row.getCell(7).toString());
                extractOfRegistry.setRegistryExtractFilename(row.getCell(8).toString());
                ownerShare.setExtractOfRegistry(extractOfRegistry);

            }

//                        for (OwnerShare ownerShare: ownerShareList)
//            {
//                System.out.println("OLOLO "+ownerShare.getOwner().getLastName());
//            }

            shareService.createShares(shareList);
            shareService.createOwnerShares(ownerShareList);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

            shareService.calculateShareValues();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
