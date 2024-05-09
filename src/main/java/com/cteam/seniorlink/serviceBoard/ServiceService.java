package com.cteam.seniorlink.serviceBoard;

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

    // select box로 검색(요양보호사, 지역)
    public ArrayList<ServiceDto> getByOption(String type, String option) {
        ArrayList<ServiceDto> list2 = new ArrayList<>();
        if (type.equals("caregiver")) {
            List<ServiceEntity> list = serviceRepository.findByCaregiverNameContains(option);
            for (ServiceEntity s : list) {
                list2.add(new ServiceDto().toDto(s));
            }
            return list2;
        } else if (type.equals("location")) {
            List<ServiceEntity> list = serviceRepository.findByLocationContains(option);
            for (ServiceEntity s : list) {
                list2.add(new ServiceDto().toDto(s));
            }
            return list2;
        } else {
            List<ServiceEntity> list = serviceRepository.findAll();
            for (ServiceEntity s : list) {
                list2.add(new ServiceDto().toDto(s));
            }
            return list2;
        }
    }

    //서비스 글 삭제
    public void del(long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
