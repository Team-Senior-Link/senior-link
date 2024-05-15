package com.cteam.seniorlink.serviceBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    //서비스 글 작성, 수정
    public ServiceDto save(ServiceDto serviceDto) {
        ServiceEntity s = serviceRepository.save(new ServiceEntity().toEntity(serviceDto));
        return new ServiceDto().toDto(s);
    }

    //pk로 검색
    public ServiceDto getService(long serviceId) {
        ServiceEntity s = serviceRepository.findById(serviceId).orElse(null);
        if (s == null) {
            return null;
        } else {
            return new ServiceDto().toDto(s);
        }
    }

    //전체 검색
    public ArrayList<ServiceDto> getAll() {
        List<ServiceEntity> list = serviceRepository.findAll();
        ArrayList<ServiceDto> list2 = new ArrayList<>();
        for (ServiceEntity s : list) {
            list2.add(new ServiceDto().toDto(s));
        }

        return list2;
    }

    //페이지네이션
    public Page<ServiceEntity> getServiceList(int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "serviceId"));
        return  this.serviceRepository.findAll(pageable);
    }

    // select box로 검색(요양보호사, 지역)
    public Page<ServiceDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "serviceId"));
        Page<ServiceEntity> list;
        if (Objects.equals("caregiver", type)) {
            list = serviceRepository.findByCaregiverNameContains(option, pageable);
        } else if (Objects.equals("location", type)) {
            list = serviceRepository.findByLocationContains(option, pageable);
        } else {
            list = serviceRepository.findAll(pageable);
        }
        return list.map(ServiceDto::toDto);
    }

    //서비스 글 삭제
    public void del(long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
