package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.serviceBoard.ServiceDto;
import com.cteam.seniorlink.serviceBoard.ServiceService;
import com.cteam.seniorlink.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final ScheduleRepository scheduleRepository;
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
    public List<Map<String, Object>> findByServiceId(@PathVariable("serviceId") int serviceId) {
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
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        ServiceDto sdto = serviceService.getService(serviceId);
        UserEntity caregiver = sdto.getCaregiver();
        System.out.println(caregiver.getName());

        Integer grade = currentUser.getGrade();
        int maxReservationTime = 0;

        ModelMap map = new ModelMap();
        boolean isReservation = true;

        // 현재 사용자가 서비스 제공자인지 확인
        boolean isCaregiver = currentUser.getUserId() == caregiver.getUserId();

        // 요양등급이 null인 경우 예약 불가 메시지 설정, 단, 서비스 제공자 예외
        if (grade == null) {
            if (!isCaregiver) {
                String message = "예약을 진행하려면 먼저 마이페이지에서 요양 등급을 지정해주세요.";
                map.put("message", message);
                isReservation = false;
            } else {
                maxReservationTime = Integer.MAX_VALUE;
            }
        } else {
            if (grade == 1 || grade == 2) {
                maxReservationTime = 4;
            } else if (grade == 3 || grade == 4 || grade == 5) {
                maxReservationTime = 3;
            }

            LocalDate startDateLocalDate = startDate.toLocalDate();
            List<ScheduleEntity> eventsOnStartDate = scheduleRepository.findByCarereceiverAndStartDateBetween(
                    currentUser, startDateLocalDate.atStartOfDay(), startDateLocalDate.atTime(LocalTime.MAX));

            if (eventsOnStartDate.size() >= maxReservationTime) {
                String message = "하루 이용 가능 서비스 신청 시간을 초과했습니다.";
                map.put("message", message);
                isReservation = false;
            }
        }

        map.put("isReservation", isReservation);

        if (isReservation) {
            ScheduleDto dto = ScheduleDto.builder()
                    .carereceiver(currentUser)
                    .caregiver(caregiver)
                    .serviceId(serviceId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .requestMsg(requestMsg)
                    .build();

            scheduleService.save(dto);
        }

        return map;
    }


    // 예약 취소
    @GetMapping("/del/{scheduleId}")
    @ResponseBody
    public void del(@PathVariable("scheduleId") long scheduleId) {
        scheduleService.del(scheduleId);
    }

    // 예약 상태 변경 (0: 대기중, 1: 수락, 2: 요양보호사가 취소, 3: 시니어가 취소)
    // 요양보호사 - 예약 수락
    @PostMapping("/accept/{scheduleId}")
    public String acceptSchedule(@PathVariable Long scheduleId) {
        scheduleService.updateScheduleStatus(scheduleId, 1);
        return "redirect:/user/mypage";
    }

    // 요양보호사 - 예약 거절
    @PostMapping("/reject/{scheduleId}")
    public String rejectSchedule(@PathVariable Long scheduleId) {
        scheduleService.updateScheduleStatus(scheduleId, 2);
        return "redirect:/user/mypage";
    }

    // 시니어 - 예약 취소
    @PostMapping("/cancel/{scheduleId}")
    public String cancelSchedule(@PathVariable Long scheduleId) {
        scheduleService.updateScheduleStatus(scheduleId, 3);
        return "redirect:/user/mypage";
    }
}
