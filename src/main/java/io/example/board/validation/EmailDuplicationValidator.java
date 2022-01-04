package io.example.board.validation;

import io.example.board.repository.rdb.member.MemberRepo;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : choi-ys
 * @date : 2022/01/04 11:38 오전
 */
@Component
public class EmailDuplicationValidator implements ConstraintValidator<EmailUnique, String> {

    private final MemberRepo memberRepo;
    private static final String DUPLICATED_MESSAGE = "이미 존재하는 이메일 입니다.";

    public EmailDuplicationValidator(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Override
    public void initialize(EmailUnique constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean isDuplicated = memberRepo.existsByEmail(email);

        if (isDuplicated) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(DUPLICATED_MESSAGE)
                    .addConstraintViolation();
        }

        return !isDuplicated;
    }
}
