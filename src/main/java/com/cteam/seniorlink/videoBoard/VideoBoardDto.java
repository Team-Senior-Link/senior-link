package com.cteam.seniorlink.videoBoard;

import com.cteam.seniorlink.user.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoBoardDto {
    private Long videoBoardId;

    private UserEntity writer;

    private String title;

    private String description;

    private String youtubeId;

    private String youtubeUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static VideoBoardDto toDto(VideoBoardEntity v){
        return VideoBoardDto.builder()
                .videoBoardId(v.getVideoBoardId())
                .writer(v.getWriter())
                .title(v.getTitle())
                .description(v.getDescription())
                .youtubeId(v.getYoutubeId())
                .youtubeUrl(v.getYoutubeUrl())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .build();
    }
}
