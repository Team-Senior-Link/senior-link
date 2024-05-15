package com.cteam.seniorlink.serviceBoard;

import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping("/service")
public class ServiceController {

    private final UserService userService;
    private final ServiceService serviceService;

    @Value("${spring.servlet.multipart.location}")
    private String path;

    //서비스 글 목록
    @GetMapping("/list")
    public void list(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<ServiceEntity> paging = this.serviceService.getServiceList(page - 1);
        model.addAttribute("paging", paging);
    }

    //select box로 검색 (요양보호사, 지역)
    @GetMapping("/search")
    public String getbyOption(String type, String option, Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<ServiceDto> paging = serviceService.getByOption(type, option, page - 1);
        model.addAttribute("paging", paging);
        model.addAttribute("type", type);
        model.addAttribute("option", option);

        return "service/list";
    }

    //글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public void addForm(Model model, Principal principal) {
        UserDto uDto = userService.getMember(principal.getName());
        model.addAttribute("isStatus", uDto.isStatus());
    }

    // 글 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String add(ServiceDto sdto, Principal principal) {
        UserDto uDto = userService.getMember(principal.getName());
        UserEntity userEntity = uDto.toEntity();
        sdto.setCaregiver(userEntity);

        LocalDateTime createAt = LocalDateTime.now();
        sdto.setCreatedAt(createAt);

        MultipartFile f = sdto.getF();
        if (f != null && !f.isEmpty()) {
            String imgPath = f.getOriginalFilename();
            File newFile = new File(path + imgPath);

            try {
                f.transferTo(newFile);
                sdto.setImgPath(imgPath);
                serviceService.save(sdto);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        } else {
            sdto.setImgPath("");
        }
        serviceService.save(sdto);

        return "redirect:/service/list";
    }

    //수정 폼, 상세 페이지
    @GetMapping("/edit")
    public String editForm(long serviceId, Model model, Principal principal) {
        ServiceDto sdto = serviceService.getService(serviceId);
        model.addAttribute("sdto", sdto);

        if (principal != null) {
            UserDto udto = userService.getMember(principal.getName());
            model.addAttribute("currentUser", udto);
        }

        return "service/edit";
    }

    //수정 완료
    @PostMapping("/edit")
    public String edit(ServiceDto sdto, Principal principal, @RequestParam("f") MultipartFile f) {
        UserDto uDto = userService.getMember(principal.getName());
        ServiceDto origin = serviceService.getService(sdto.getServiceId());

        UserEntity userEntity = uDto.toEntity();
        sdto.setCaregiver(userEntity);

        LocalDateTime updatedAt = LocalDateTime.now();
        sdto.setUpdatedAt(updatedAt);

        if (f != null && !f.isEmpty()) {
            try {
                String imgPath = f.getOriginalFilename();
                File newFile = new File(path + imgPath);
                f.transferTo(newFile);
                sdto.setImgPath(imgPath);

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
            sdto.setImgPath(origin.getImgPath());
        }
        serviceService.save(sdto);

        return "redirect:/service/list";
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/del")
    public String del(long serviceId) {
        ServiceDto origin = serviceService.getService(serviceId);
        File delFile = new File(path + origin.getImgPath());
        delFile.delete();
        serviceService.del(serviceId);

        return "redirect:/service/list";
    }
}
