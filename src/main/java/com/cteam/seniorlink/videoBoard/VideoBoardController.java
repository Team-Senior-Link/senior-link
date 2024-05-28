package com.cteam.seniorlink.videoBoard;

import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/videoBoard")
public class VideoBoardController {
    private final VideoBoardService videoBoardService;
    private final UserService userService;

    //글 목록
    @GetMapping("/list")
    public void list(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<VideoBoardEntity> paging = this.videoBoardService.getVideoBoardList(page - 1);
        model.addAttribute("paging", paging);
    }

    //select box로 검색 (제목)
    @GetMapping("/search")
    public String getbyOption(String type, String option, Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<VideoBoardDto> paging = videoBoardService.getByOption(type, option, page - 1);
        model.addAttribute("paging", paging);
        model.addAttribute("type", type);
        model.addAttribute("option", option);

        return "videoBoard/list";
    }

    //글 작성 폼
    @GetMapping("/add")
    public void addForm() {
    }

    // 글 작성
    @PostMapping("/add")
    public String uploadVideo(VideoBoardDto vdto, Principal principal) {
        String youtubeId = extractYoutubeId(vdto.getYoutubeUrl());

        if (youtubeId != null) {
            UserDto uDto = userService.getMember(principal.getName());
            UserEntity userEntity = uDto.toEntity();
            vdto.setWriter(userEntity);

            LocalDateTime createAt = LocalDateTime.now();
            vdto.setCreatedAt(createAt);

            vdto.setYoutubeId(youtubeId);

            videoBoardService.save(vdto);
        }

        return "redirect:/videoBoard/list";
    }


    // URL에서 YouTube 비디오의 ID를 추출
    private String extractYoutubeId(String url) {
        String youtubeId = null;
        if (url.contains("youtu.be/")) {
            youtubeId = url.substring(url.indexOf("youtu.be/") + 9);
        } else if (url.contains("youtube.com/watch?v=")) {
            youtubeId = url.substring(url.indexOf("youtube.com/watch?v=") + 20);
        }

        return youtubeId;
    }

    //수정 폼
    @GetMapping("/edit")
    public String editForm(long videoBoardId, Model model, Principal principal) {
        VideoBoardDto vdto = videoBoardService.getVideoBoard(videoBoardId);
        model.addAttribute("vdto", vdto);

        if (principal != null) {
            UserDto udto = userService.getMember(principal.getName());
            model.addAttribute("currentUser", udto);
        }

        return "videoBoard/edit";
    }

    //수정 완료
    @PostMapping("/edit")
    public String edit(VideoBoardDto vdto, Principal principal) {
        String youtubeId = extractYoutubeId(vdto.getYoutubeUrl());

        if (youtubeId != null) {
            UserDto uDto = userService.getMember(principal.getName());
            UserEntity userEntity = uDto.toEntity();
            vdto.setWriter(userEntity);

            vdto.setYoutubeId(youtubeId);

            LocalDateTime updatedAt = LocalDateTime.now();
            vdto.setUpdatedAt(updatedAt);

            videoBoardService.save(vdto);
        }

        return "redirect:/videoBoard/list";
    }

    //삭제
    @GetMapping("/del")
    public String del(long videoBoardId) {
        videoBoardService.del(videoBoardId);

        return "redirect:/videoBoard/list";
    }
}