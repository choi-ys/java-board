package io.example.board.config.test;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import io.example.board.config.jpa.DataJpaAuditorConfig;
import io.example.board.config.p6spy.P6spyLogMessageFormatConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import java.lang.annotation.*;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오후 10:30
 */
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfiguration.class, DataJpaAuditorConfig.class})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataJpaTestConfig {
}
