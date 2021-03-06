package io.example.board.domain.rdb.member;

import static javax.persistence.FetchType.EAGER;

import io.example.board.domain.rdb.base.Auditor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author : choi-ys
 * @date : 2021/09/16 6:47 오후
 */

@Entity
@Table(
    name = "member_tb",
    uniqueConstraints = @UniqueConstraint(
        name = "MEMBER_EMAIL_UNIQUE",
        columnNames = "email"
    )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(
        name = "member_role_tb",
        joinColumns = @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "TB_MEMBER_ROLE_MEMBER_ID_FOREIGN_KEY")
        )
    )
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    @Column(name = "memberStatus", nullable = false, length = 30)
    private MemberStatus memberStatus;

    @Column(name = "certify", nullable = false)
    private boolean certify;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/20 11:57 오후
    // * --------------------------------------------------------------
    public Member(String email, String password, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.memberStatus = MemberStatus.ACCEPTED_BUT_DISABLED;
        addRoles(Set.of(MemberRole.MEMBER));
    }

    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/09/20 11:57 오후
    // * --------------------------------------------------------------
    public void addRoles(Set<MemberRole> additionRoleSet) {
        roles.addAll(additionRoleSet);
    }

    public void removeRoles(Set<MemberRole> removalRoleSet) {
        roles.removeAll(removalRoleSet);
    }

    public Set<SimpleGrantedAuthority> mapToSimpleGrantedAuthority() {
        final String rolePrefix = "ROLE_";
        return roles.stream()
            .map(it -> new SimpleGrantedAuthority(rolePrefix + it))
            .collect(Collectors.toSet());
    }
}
