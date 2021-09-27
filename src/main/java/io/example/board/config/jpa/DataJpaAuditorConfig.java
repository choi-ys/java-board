package io.example.board.config.jpa;

import io.example.board.domain.rdb.base.AuditorWAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 8:58
 * @apiNote : JWT 토큰 검증 후 SecurityContextHolder에 Authentication 설정된 principal의 값을 이용하여 @CreatedBy, @UpdateBy의 Meta 데이터 Auditing
 */
@Configuration
@EnableJpaAuditing
public class DataJpaAuditorConfig {

    /**
     * TODO: 용석(2021-09-27) {@Link Auditor}를 상속받은 Entity의 save, dirty Checking이 발생할 경우,
     * 해당 설정 미 동작 이슈로 인한 대체 설정 {@link AuditorWAwareImpl} 추가
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorWAwareImpl();
    }
}
