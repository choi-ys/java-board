package io.example.board.repository.rdb.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/12/13 6:22 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {
    private Integer totalPages;
    private Long totalElementCount;
    private Integer currentPage;
    private Integer currentElementCount;
    private Integer perPageNumber;

    private Boolean firstPage;
    private Boolean lastPage;
    private Boolean hasNextPage;
    private Boolean hasPrevious;

    private List<T> embedded;

    private PageResponse(
            Integer totalPages,
            Long totalElementCount,
            Integer currentPage,
            Integer currentElementCount,
            Integer perPageNumber,
            Boolean firstPage,
            Boolean lastPage,
            Boolean hasNextPage,
            Boolean hasPrevious,
            List<T> embedded
    ) {
        this.totalPages = totalPages;
        this.totalElementCount = totalElementCount;
        this.currentPage = currentPage;
        this.currentElementCount = currentElementCount;
        this.perPageNumber = perPageNumber;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
        this.hasNextPage = hasNextPage;
        this.hasPrevious = hasPrevious;
        this.embedded = embedded;
    }

    public static <T> PageResponse mapTo(Page<T> page) {
        return new PageResponse(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getNumberOfElements(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getContent()
        );
    }
}
