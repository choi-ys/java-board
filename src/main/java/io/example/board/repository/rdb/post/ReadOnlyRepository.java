package io.example.board.repository.rdb.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021/12/10 3:47 오후
 * @apiNote : Domain의 성격에 따라, JpaRepository가 extends 하는 CrudRepository, PagingAndSortingRepository의
 * 모든 기능을 사용할 필요가 없는 경우, maker interface인 Repository만 상속 받아,
 * CrudRepository, PagingAndSortingRepository의 조회 부분만 명시함으로써, read only repository를 custom하게 구현
 */
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    List<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    List<T> findAllById(Iterable<ID> ids);
}
