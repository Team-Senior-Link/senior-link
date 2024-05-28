package com.cteam.seniorlink.videoBoard;

import com.cteam.seniorlink.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Table(name = "videoBoard")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VideoBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoBoardId;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity writer;

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String description;

    @Column
    private String youtubeId;

    @Column
    private String youtubeUrl;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public VideoBoardEntity toEntity(VideoBoardDto v){
        return VideoBoardEntity.builder()
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
