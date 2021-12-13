package io.example.board.domain.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:47 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchRequest {
    private String title;
    private String content;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Pageable pageable;

    public PostSearchRequest(String title,
                             String content,
                             String writerName,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             Pageable pageable
    ) {
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pageable = pageable;
    }
}
