package com.cteam.seniorlink.serviceBoard;

import com.cteam.seniorlink.domain.user.UserEntity;
import com.cteam.seniorlink.dto.user.UserDto;
import com.cteam.seniorlink.service.user.UserService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping("/service")
public class ServiceController {

    private final ServiceService serviceService;
    private final UserService userService;

    @Value("${spring.servlet.multipart.location}")
    private String path;

    //서비스 글 목록
    @GetMapping("/list")
    public void list(Model model) {
        ArrayList<ServiceDto> list = serviceService.getAll();
        model.addAttribute("list", list);
    }

    //글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public void addForm() {
    }

    // 글 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String add(ServiceDto sdto, Principal principal) {
        UserDto uDto = userService.getMember(principal.getName());

        if (uDto.isStatus()) {
            sdto.setCaregiver(UserEntity.builder()
                    .username(uDto.getUsername())
                    .status(uDto.isStatus())
                    .build());

            MultipartFile f = sdto.getF();
            String profileImgPath = f.getOriginalFilename();
            File newFile = new File(path + profileImgPath);

            try {
                f.transferTo(newFile);
                sdto.setProfileImgPath(profileImgPath);

                LocalDateTime currentTime = LocalDateTime.now();
                sdto.setCreatedAt(currentTime);
                sdto.setUpdatedAt(currentTime);

                serviceService.save(sdto);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            return "redirect:/service/list";
        } else {
            return null;
        }
    }

    //수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public void editForm(long serviceId, Model model) {
        ServiceDto sdto = serviceService.getService(serviceId);
        model.addAttribute("sdto", sdto);
    }

    //수정 완료
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit")
    public String edit(ServiceDto sdto, Principal principal) {
        ServiceDto origin = serviceService.getService(sdto.getServiceId());
        UserDto uDto = userService.getMember(principal.getName());
        sdto.setCaregiver(UserEntity.builder()
                .username(uDto.getUsername())
                .build());

        MultipartFile f = sdto.getF();
        String profileImgPath = f.getOriginalFilename();
        File newFile = new File(path + profileImgPath);

        try {
            f.transferTo(newFile);
            sdto.setProfileImgPath(profileImgPath);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        sdto.setTitle(origin.getTitle());
        sdto.setIntroduction(origin.getIntroduction());
        sdto.setCareer(origin.getCareer());
        sdto.setSpecialty(origin.getSpecialty());
        sdto.setLocation(origin.getLocation());
        sdto.setDayFree(origin.getDayFree());
        sdto.setTimeFree(origin.getTimeFree());
        sdto.setUpdatedAt(LocalDateTime.now());

        serviceService.save(sdto);

        return "redirect:/service/list";
    }

    //삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/del")
    public String del(Long serviceId) {
        serviceService.del(serviceId);

        return "redirect:/service/list";
    }

    //select box로 검색 (요양보호사, 지역)
    @GetMapping("/search")
    public String getByOption(String type, String option, Model model) {
        ArrayList<ServiceDto> list = serviceService.getByOption(type, option);
        model.addAttribute("list", list);

        return "service/list";
    }

}
