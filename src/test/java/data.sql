--- # 회원 생성 (게시글 작성자)
insert into member_tb (id, created_at, created_by, updated_at, updated_by, certify, email, enabled, name, nickname, password)
values (null, '2022-03-21T16:49:38.166', NULL, '2022-03-21T16:49:38.166', NULL, false, 'project.log.062@gmail.com', false, 'choi-ys', 'whypie', 'password');

--- # 회원 권한 부여 (게시글 작성자)
insert into member_role_tb (member_id, roles)
values (1, 'MEMBER');

--- # 게시글 생성
insert into post_tb (id, created_at, created_by, updated_at, updated_by, content, deleted, display, member_id, title, view_count)
values (null, '2022-03-21T16:49:38.195', NULL, '2022-03-21T16:49:38.195', NULL, 'java.io.StringReader@c662c312', false, true, 1, '게시글 제목', 0);

--- # 회원 생성 (댓글 작성자)
insert into member_tb (id, created_at, created_by, updated_at, updated_by, certify, email, enabled, name, nickname, password)
values (null, '2022-03-21T16:49:38.198', NULL, '2022-03-21T16:49:38.198', NULL, false, 'ys.choi@naver.com', false, 'choi-ys', 'whypie', 'password');

--- # 회원 권한 부여 (댓글 작성자)
insert into member_role_tb (member_id, roles)
values (2, 'MEMBER');

--- # 댓글 생성
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:1', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:2', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:3', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:4', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:5', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:6', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:7', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:8', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:9', 2, 1);
insert into comment_tb (id, created_at, created_by, updated_at, updated_by, content, member_id, post_id) values (null, '2022-03-21T16:52:55.372', NULL, '2022-03-21T16:52:55.372', NULL, '댓글 내용:10', 2, 1);

---
select
    *
from
    post_tb as post
    left outer join comment_tb as comment
        on post.id = comment.post_id
    left outer join member_tb as member
        on post.member_id = member.id
where
    post.id = 1

---
SELECT          post0_.id             AS col_0_0_,
                post0_.title          AS col_1_0_,
                post0_.content        AS col_2_0_,
                post0_.view_count     AS col_3_0_,
                post0_.created_at     AS col_4_0_,
                post0_.updated_at     AS col_5_0_,
                post0_.member_id      AS col_6_0_,
                .[*]                  AS col_7_0_,
                member2_.id           AS id1_2_0_,
                comments3_.id         AS id1_0_1_,
                member2_.created_at   AS created_2_2_0_,
                member2_.created_by   AS created_3_2_0_,
                member2_.updated_at   AS updated_4_2_0_,
                member2_.updated_by   AS updated_5_2_0_,
                member2_.certify      AS certify6_2_0_,
                member2_.email        AS email7_2_0_,
                member2_.enabled      AS enabled8_2_0_,
                member2_.name         AS name9_2_0_,
                member2_.nickname     AS nicknam10_2_0_,
                member2_.password     AS passwor11_2_0_,
                comments3_.created_at AS created_2_0_1_,
                comments3_.created_by AS created_3_0_1_,
                comments3_.updated_at AS updated_4_0_1_,
                comments3_.updated_by AS updated_5_0_1_,
                comments3_.content    AS content6_0_1_,
                comments3_.member_id  AS member_i7_0_1_,
                comments3_.post_id    AS post_id8_0_1_
FROM            post_tb post0_
LEFT OUTER JOIN comment_tb comment1_
ON              (
                                post0_.id=comment1_.post_id)
INNER JOIN      member_tb member2_
ON              post0_.member_id=member2_.id
INNER JOIN      comment_tb comments3_
ON              post0_.id=comments3_.post_id
WHERE           (
                                lower(post0_.title) LIKE ? escape '!')
AND             (
                                lower(post0_.content) LIKE ? escape '!')
AND             member2_.name=?
AND             post0_.created_at>=?
AND             post0_.updated_at<=?
LIMIT           ?