package com.cteam.seniorlink.certification;


import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public void list(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        List<CertificationDto> list = certificationService.getByCaregiver(userEntity);
        model.addAttribute("list", list);
    }

    //글 작성 폼
    @GetMapping("/add")
    public void addForm() {}

    //글 작성
    @PostMapping("/add")
    public String add(CertificationDto cdto, Principal principal) {
        UserDto uDto = userService.getMember(principal.getName());
        UserEntity userEntity = uDto.toEntity();
        cdto.setCaregiver(userEntity);

        LocalDateTime createAt = LocalDateTime.now();
        cdto.setCreatedAt(createAt);

        MultipartFile f = cdto.getF();
        String imgPath = f.getOriginalFilename();
        File newFile = new File(path + imgPath);
        try {
            f.transferTo(newFile);
            cdto.setImgPath(imgPath);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        certificationService.save(cdto);

        return "redirect:/certification/list";
    }

    //수정 폼
    @GetMapping("/edit")
    public String editForm(long certificationId, Model model, Principal principal) {
        UserDto udto = userService.getMember(principal.getName());
        CertificationDto cdto = certificationService.getCertification(certificationId);

        model.addAttribute("currentUser", udto);
        model.addAttribute("cdto", cdto);

        return "certification/edit";
    }

    //수정 완료
    @PostMapping("/edit")
    public String edit(CertificationDto cdto, Principal principal, @RequestParam("f") MultipartFile f) {
        UserDto uDto = userService.getMember(principal.getName());
        CertificationDto origin = certificationService.getCertification(cdto.getCertificationId());

        UserEntity userEntity = uDto.toEntity();
        cdto.setCaregiver(userEntity);

        if (f != null && !f.isEmpty()) {
            try {
                String imgPath = f.getOriginalFilename();
                File newFile = new File(path + imgPath);
                f.transferTo(newFile);
                cdto.setImgPath(imgPath);

                if (origin.getImgPath() != null && !origin.getImgPath().isEmpty()) {
                    File delFile = new File(path + origin.getImgPath());
                    if (delFile.exists()) {
                        delFile.delete();
                    }
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        } else {
            cdto.setImgPath(origin.getImgPath());
        }

        certificationService.save(cdto);

        return "redirect:/certification/list";
    }

    //이미지 읽기
    @GetMapping("/readImg")
    public ResponseEntity<byte[]> readImg(String imgPath) {
        if (imgPath == null || imgPath.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        File f = new File(path + imgPath);
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<byte[]> result = null;
        try {
            header.add("contentType", Files.probeContentType(f.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    //삭제
    @GetMapping("/del")
    public String del(long certificationId) {
        CertificationDto origin = certificationService.getCertification(certificationId);
        File delFile = new File(path + origin.getImgPath());
        delFile.delete();
        certificationService.del(certificationId);

        return "redirect:/certification/list";
    }

}
