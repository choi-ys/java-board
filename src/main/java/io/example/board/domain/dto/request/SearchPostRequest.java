package io.example.board.domain.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:47 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchPostRequest {
    private String title;
    private String content;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Pageable pageable;

    public SearchPostRequest(String title,
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
        this.pageable = pageNumberToIndex(pageable);
    }

    public static SearchPostRequest of(
            String title,
            String content,
            String writerName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Pageable pageable
    ) {
        return new SearchPostRequest(title, content, writerName, createdAt, updatedAt, pageNumberToIndex(pageable));
    }

    private static Pageable pageNumberToIndex(Pageable pageable) {
        if (!Objects.isNull(pageable)) {
            return pageable.getPageNumber() != 0 ?
                    pageable.withPage(pageable.getPageNumber() - 1) :
                    pageable;
        }
        return null;
    }
}
