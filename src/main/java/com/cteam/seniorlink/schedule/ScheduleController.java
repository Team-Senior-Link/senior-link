package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.serviceBoard.ServiceDto;
import com.cteam.seniorlink.serviceBoard.ServiceService;
import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ServiceService serviceService;

    @RequestMapping("")
    public String schedule() {
        return "schedule/schedule";
    }

    // 돌봄 서비스 게시글의 serviceId를 넘김
    @RequestMapping("/serviceIdSchedule")
    public String getSchedulePage(@RequestParam("serviceId") long serviceId, Model model) {
        model.addAttribute("serviceId", serviceId);

        return "schedule/schedule";
    }

    // 돌봄 서비스 일정 목록
    @GetMapping("/list/{serviceId}")
    @ResponseBody
    public List<Map<String, Object>> findByRoomId(@PathVariable("serviceId") int serviceId) {
        List<ScheduleDto> list;

        list = scheduleService.getByServiceId(serviceId);

        List<Map<String, Object>> jsonList = new ArrayList<>();

        for (ScheduleDto schedule : list) {
            Map<String, Object> json = new HashMap<>();
            json.put("cal_Id", schedule.getScheduleId());
            json.put("start", schedule.getStartDate().toString());
            json.put("end", schedule.getEndDate().toString());
            jsonList.add(json);
        }
        return jsonList;
    }

    // 예약 신청
    @PostMapping("/add/{serviceId}")
    @ResponseBody
    public Map addEvent(@RequestBody List<Map<String, Object>> param, @PathVariable("serviceId") long serviceId) throws Exception {

        String startDateString = (String) param.get(0).get("start");
        String endDateString = (String) param.get(0).get("end");
        String requestMsg = (String) param.get(0).get("title");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity carereceiver = (UserEntity) authentication.getPrincipal();

        ServiceDto sdto = serviceService.getService(serviceId);
        UserEntity caregiver = sdto.getCaregiver();

        ScheduleDto dto = ScheduleDto.builder()
                .carereceiver(carereceiver)
                .caregiver(caregiver)
                .serviceId(serviceId)
                .startDate(startDate)
                .endDate(endDate)
                .requestMsg(requestMsg)
                .build();

        ScheduleDto s = scheduleService.save(dto);

        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", s.getScheduleId() + "");

        return map;
    }

    // 예약 취소
    @GetMapping("/del/{scheduleId}")
    @ResponseBody
    public void del(@PathVariable("scheduleId") long scheduleId) {
        scheduleService.del(scheduleId);
    }
}
