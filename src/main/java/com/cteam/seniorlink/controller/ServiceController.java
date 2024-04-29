package com.cteam.seniorlink.controller;

import com.cteam.seniorlink.dto.service.ServiceDto;
import com.cteam.seniorlink.service.service.ServiceService;
import com.cteam.seniorlink.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
@RequestMapping("/service")
public class ServiceController {

    private final ServiceService serviceService;
    private final UserService userService;

    //서비스 글 목록
    @RequestMapping("/list")
    public void list(ModelMap map) {
        ArrayList<ServiceDto> list = serviceService.getAll();
        map.addAttribute("list", list);
    }

    //글 작성 폼
    @GetMapping("/add")
    public void addForm() {
    }

    //글 작성
    @PostMapping("/add")
    public String add(ServiceDto s) {
        serviceService.save(s);
        return "redirect:/service/list";
    }

    //수정 폼
    @GetMapping("/edit")
    public void editForm(long serviceId, ModelMap map) {
        ServiceDto s = serviceService.getService(serviceId);
        map.addAttribute("s", s);
    }

    //수정 완료
    @PostMapping("/edit")
    public String edit(ServiceDto s){
        ServiceDto s2 = serviceService.getService(s.getServiceId());
        s2.setTitle(s.getTitle());
        s2.setIntroduction(s.getIntroduction());
        s2.setCareer(s.getCareer());
        s2.setSpecialty(s.getSpecialty());
        s2.setLocation(s.getLocation());
        s2.setDayFree(s.getDayFree());
        s2.setTimeFree(s.getTimeFree());
        serviceService.save(s2);
        return "redirect:/service/list";
    }

    //삭제
    @RequestMapping("/del")
    public String del(Long serviceId) {
        serviceService.del(serviceId);
        return "redirect:/service/list";
    }

    //요양보호사 이름으로 검색
    @RequestMapping("/getByCaregiverName")
    public String getByCaregiverName(String caregiver, ModelMap map){
        List<ServiceDto> list = serviceService.getByCaregiverName(caregiver);
        map.addAttribute("list",list);
        return "service/list";
    }

}
