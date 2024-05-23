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
        UserEntity carereceiver = (UserEntity) authentication.getPrincipal();

        ServiceDto sdto = serviceService.getService(serviceId);
        UserEntity caregiver = sdto.getCaregiver();

        int grade = carereceiver.getGrade();
        int maxReservationTime = 0;
        if (grade == 1 || grade == 2) {
            maxReservationTime = 4;
        } else if (grade == 3 || grade == 4 || grade == 5) {
            maxReservationTime = 3;
        }

        LocalDate startDateLocalDate = startDate.toLocalDate();
        List<ScheduleEntity> eventsOnStartDate = scheduleRepository.findByCarereceiverAndStartDateBetween(
                carereceiver, startDateLocalDate.atStartOfDay(), startDateLocalDate.atTime(LocalTime.MAX));

        ModelMap map = new ModelMap();
        boolean isReservation = true;

        if (eventsOnStartDate.size() >= maxReservationTime) {
            String message = "하루 이용 가능 서비스 신청 시간을 초과했습니다.";
            map.put("message", message);
            isReservation = false;
        }
        map.put("isReservation", isReservation);

        if (isReservation) {
            ScheduleDto dto = ScheduleDto.builder()
                    .carereceiver(carereceiver)
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
}
