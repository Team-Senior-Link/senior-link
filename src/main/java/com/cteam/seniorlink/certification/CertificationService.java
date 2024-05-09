package com.cteam.seniorlink.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CertificationService {
    private final CertificationRepository certificationRepository;

    // 글 작성, 수정
    public CertificationDto save(CertificationDto certificationDto) {
        CertificationEntity c = certificationRepository.save(new CertificationEntity().toEntity(certificationDto));
        return new CertificationDto().toDto(c);
    }

    //pk로 검색
    public CertificationDto getCertification(long certificationId) {
        CertificationEntity c = certificationRepository.findById(certificationId).orElse(null);
        if (c == null) {
            return null;
        } else {
            return new CertificationDto().toDto(c);
        }
    }

    //전체 검색
    public ArrayList<CertificationDto> getAll() {
        List<CertificationEntity> list = certificationRepository.findAll();
        ArrayList<CertificationDto> list2 = new ArrayList<>();
        for (CertificationEntity c : list) {
            list2.add(new CertificationDto().toDto(c));
        }

        return list2;
    }


    // 글 삭제
    public void del(long certificationId){
        certificationRepository.deleteById(certificationId);
    }

}
