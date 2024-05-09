package com.cteam.seniorlink.certification;


import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

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
        ArrayList<CertificationDto> list = certificationService.getAll();
        model.addAttribute("list", list);
    }

    //글 작성 폼
    @GetMapping("/add")
    public void addForm() {
    }

    //글 작성
    @PostMapping("/add")
    public String add(CertificationDto cdto, Principal principal) {
        UserDto uDto = userService.getMember(principal.getName());

        MultipartFile f = cdto.getF();
        String imgPath = f.getOriginalFilename();
        File newFile = new File(path + imgPath);

        try {
            f.transferTo(newFile);
            cdto.setImgPath(imgPath);

            UserEntity userEntity = uDto.toEntity();
            cdto.setCaregiver(userEntity);

            certificationService.save(cdto);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        return "redirect:/certification/list";
    }

    //수정 폼
    @GetMapping("/edit")
    public void editForm(long certificationId, Model model) {
        CertificationDto cdto = certificationService.getService(certificationId);
        model.addAttribute("cdto", cdto);
    }

    //수정 완료
    @PostMapping("/edit")
    public String edit(CertificationDto cdto, Principal principal) {
        CertificationDto orign = certificationService.getService(cdto.getCertificationId());
        UserDto uDto = userService.getMember(principal.getName());
        cdto.setCaregiver(UserEntity.builder()
                .username(uDto.getUsername())
                .build());

        MultipartFile f = cdto.getF();
        String imgPath = f.getOriginalFilename();
        File newFile = new File(path + imgPath);

        try {
            f.transferTo(newFile);
            cdto.setImgPath(imgPath);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        cdto.setName(orign.getName());
        cdto.setOrganization(orign.getOrganization());

        certificationService.save(cdto);

        return "redirect:/certification/list";
    }

    //삭제
    @GetMapping("/del")
    public String del(long certificationId) {
        certificationService.del(certificationId);

        return "redirect:/certification/list";
    }

}
