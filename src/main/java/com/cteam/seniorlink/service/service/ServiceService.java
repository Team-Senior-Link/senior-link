package com.cteam.seniorlink.service.service;

import com.cteam.seniorlink.domain.service.ServiceEntity;
import com.cteam.seniorlink.dto.service.ServiceDto;
import com.cteam.seniorlink.repository.service.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    //서비스 글 작성, 수정
    public ServiceDto save(ServiceDto serviceDto) {
        ServiceEntity s = serviceRepository.save(new ServiceEntity(serviceDto.getServiceId(), serviceDto.getTitle()
                , serviceDto.getIntroduction(), serviceDto.getCareer(), serviceDto.getSpecialty(), serviceDto.getLocation(), serviceDto.getDayFree()
                , serviceDto.getTimeFree(), serviceDto.getCreatedAt(), serviceDto.getUpdatedAt(), serviceDto.getCaregiver()));
        return new ServiceDto(s.getServiceId(), s.getTitle(), s.getIntroduction(), s.getCareer()
                , s.getSpecialty(), s.getLocation(), s.getDayFree(), s.getTimeFree()
                , s.getCreatedAt(), s.getUpdatedAt(), s.getCaregiver());
    }

    //pk로 검색
    public ServiceDto getService(long serviceId) {
        ServiceEntity s = serviceRepository.findById(serviceId).orElse(null);
        if (s == null) {
            return null;
        } else {
            return new ServiceDto(s.getServiceId(),s.getTitle(),s.getIntroduction(),s.getCareer(),s.getSpecialty()
                    ,s.getLocation(),s.getDayFree(),s.getTimeFree(),s.getCreatedAt(),s.getUpdatedAt(),s.getCaregiver());
        }
    }

    //전체 검색
    public ArrayList<ServiceDto> getAll() {
        List<ServiceEntity> list = serviceRepository.findAll();
        ArrayList<ServiceDto> list2 = new ArrayList<>();
        for (ServiceEntity s : list) {
            list2.add(new ServiceDto(s.getServiceId(), s.getTitle(), s.getIntroduction(), s.getCareer(), s.getSpecialty()
                    , s.getLocation(), s.getDayFree(), s.getTimeFree(), s.getCreatedAt(), s.getUpdatedAt(), s.getCaregiver()));
        }

        return list2;
    }

    // 요양보호사 이름으로 검색
    public List<ServiceDto> getByCaregiverName(String caregiver) {
        List<ServiceEntity> list = serviceRepository.findByCaregiverName(caregiver);
        ArrayList<ServiceDto> list2 = new ArrayList<>();
        for (ServiceEntity s : list) {
            list2.add(new ServiceDto().toDto(s));
        }
        return list2;
    }

    //서비스 글 삭제
    public void del(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
