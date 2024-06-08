package com.cteam.seniorlink.certification;


import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserService;
import com.cteam.seniorlink.user.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/certification")
@PreAuthorize("isAuthenticated()")
public class CertificationController {

    private final UserService userService;
    private final CertificationService certificationService;


    @Value("${spring.servlet.multipart.location}")
    private String path;

    //글 목록
    @GetMapping("/list")
    public void list(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        if (userEntity.getRole() == UserRole.ADMIN) {
            List<CertificationDto> list = certificationService.getAll();
            model.addAttribute("list", list);
        } else {
            List<CertificationDto> list = certificationService.getByCaregiver(userEntity);
            model.addAttribute("list", list);
        }
    }

    //글 작성 폼
    @GetMapping("/add")
    public void addForm() {
    }

    // 글 작성
    @PostMapping("/add")
    public String add(MultipartFile workContractFile,
                      MultipartFile certificateFile,
                      MultipartFile healthCheckupFile,
                      MultipartFile criminalHistoryFile,
                      MultipartFile privacyConsentFile,
                      Principal principal) {

        UserDto uDto = userService.getMember(principal.getName());
        UserEntity userEntity = uDto.toEntity();

        CertificationDto cdto = new CertificationDto();
        cdto.setCaregiver(userEntity);
        cdto.setCreatedAt(LocalDateTime.now());

        try {
            String workContractFileName = saveFile(workContractFile);
            String certificateFileName = saveFile(certificateFile);
            String healthCheckupFileName = saveFile(healthCheckupFile);
            String criminalHistoryFileName = saveFile(criminalHistoryFile);
            String privacyConsentFileName = saveFile(privacyConsentFile);

            cdto.setWorkContract(workContractFileName);
            cdto.setCertificate(certificateFileName);
            cdto.setHealthCheckup(healthCheckupFileName);
            cdto.setCriminalHistory(criminalHistoryFileName);
            cdto.setPrivacyConsent(privacyConsentFileName);

            certificationService.save(cdto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/certification/list";
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" + originalFileName; // 이름 충돌 방지
        File newFile = new File(path + newFileName);
        file.transferTo(newFile);
        return newFileName;
    }

    //수정 폼
    @GetMapping("/edit")
    public String editForm(long certificationId, Model model, Principal principal) {
        UserDto udto = userService.getMember(principal.getName());
        CertificationDto cdto = certificationService.getCertification(certificationId);

        model.addAttribute("currentUser", udto);
        model.addAttribute("cdto", cdto);


        System.out.println("@@@@@@@Current user roles: " + udto.getRole());

        return "certification/edit";
    }

    //수정 완료
    @PostMapping(value = "/edit", consumes = "multipart/form-data")
    public String edit(CertificationDto cdto, Principal principal,
                       MultipartFile workContractFile,
                       MultipartFile certificateFile,
                       MultipartFile healthCheckupFile,
                       MultipartFile criminalHistoryFile,
                       MultipartFile privacyConsentFile) {
//        UserDto uDto = userService.getMember(principal.getName());
        CertificationDto origin = certificationService.getCertification(cdto.getCertificationId());

        cdto.setCaregiver(origin.getCaregiver());

        try {
            if (workContractFile != null && !workContractFile.isEmpty()) {
                String workContractFileName = saveFile(workContractFile);
                cdto.setWorkContract(workContractFileName);
                deleteFileIfExists(origin.getWorkContract());
            } else {
                cdto.setWorkContract(origin.getWorkContract());
            }

            if (certificateFile != null && !certificateFile.isEmpty()) {
                String certificateFileName = saveFile(certificateFile);
                cdto.setCertificate(certificateFileName);
                deleteFileIfExists(origin.getCertificate());
            } else {
                cdto.setCertificate(origin.getCertificate());
            }

            if (healthCheckupFile != null && !healthCheckupFile.isEmpty()) {
                String healthCheckupFileName = saveFile(healthCheckupFile);
                cdto.setHealthCheckup(healthCheckupFileName);
                deleteFileIfExists(origin.getHealthCheckup());
            } else {
                cdto.setHealthCheckup(origin.getHealthCheckup());
            }

            if (criminalHistoryFile != null && !criminalHistoryFile.isEmpty()) {
                String criminalHistoryFileName = saveFile(criminalHistoryFile);
                cdto.setCriminalHistory(criminalHistoryFileName);
                deleteFileIfExists(origin.getCriminalHistory());
            } else {
                cdto.setCriminalHistory(origin.getCriminalHistory());
            }

            if (privacyConsentFile != null && !privacyConsentFile.isEmpty()) {
                String privacyConsentFileName = saveFile(privacyConsentFile);
                cdto.setPrivacyConsent(privacyConsentFileName);
                deleteFileIfExists(origin.getPrivacyConsent());
            } else {
                cdto.setPrivacyConsent(origin.getPrivacyConsent());
            }

            certificationService.save(cdto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/certification/list";
    }


    // 수정된후 기존 파일 삭제
    private void deleteFileIfExists(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File delFile = new File(path + fileName);
            if (delFile.exists()) {
                delFile.delete();
            }
        }
    }

    //다운로드
    @GetMapping("/down")
    public ResponseEntity<byte[]> down(String fname) {
        if (fname == null || fname.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        File f = new File(path + fname);
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<byte[]> result = null;
        try {
            header.add("contentType", Files.probeContentType(f.toPath()));
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fname, "UTF-8") + "\"");
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    // 게시글 삭제
    @GetMapping("/del")
    public String del(long certificationId) {
        certificationService.del(certificationId);

        return "redirect:/certification/list";
    }

}