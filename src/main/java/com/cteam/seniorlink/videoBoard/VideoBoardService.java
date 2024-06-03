package com.cteam.seniorlink.videoBoard;

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
public class VideoBoardService {
    private final VideoBoardRepository videoBoardRepository;

    // 작성, 수정
    public VideoBoardDto save(VideoBoardDto videoBoardDto) {
        VideoBoardEntity v = videoBoardRepository.save(new VideoBoardEntity().toEntity(videoBoardDto));
        return new VideoBoardDto().toDto(v);
    }

    // pk로 검색
    public VideoBoardDto getVideoBoard(long videoBoardId) {
        VideoBoardEntity v = videoBoardRepository.findById(videoBoardId).orElse(null);
        return new VideoBoardDto().toDto(v);
    }

    //전체 검색
    public ArrayList<VideoBoardDto> getAll() {
        List<VideoBoardEntity> list = videoBoardRepository.findAll();
        ArrayList<VideoBoardDto> list2 = new ArrayList<>();
        for (VideoBoardEntity v : list) {
            list2.add(new VideoBoardDto().toDto(v));
        }
        return list2;
    }

    //페이지네이션
    public Page<VideoBoardEntity> getVideoBoardList(int page){
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "videoBoardId"));
        return  this.videoBoardRepository.findAll(pageable);
    }

    // select box로 검색(제목)
    public Page<VideoBoardDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "videoBoardId"));
        Page<VideoBoardEntity> list;
        if (Objects.equals("title", type)) {
            list = videoBoardRepository.findByTitleContains(option, pageable);
        }  else {
            list = videoBoardRepository.findAll(pageable);
        }
        return list.map(VideoBoardDto::toDto);
    }

    // 글 삭제
    public void del(long videoBoardId){
        videoBoardRepository.deleteById(videoBoardId);
    }

}
