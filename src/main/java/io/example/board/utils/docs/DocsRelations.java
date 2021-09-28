package io.example.board.utils.docs;

/**
 * @author : choi-ys
 * @date : 2021-09-29 오전 1:33
 */
public enum DocsRelations {
    PROFILE("profile"),
    INDEX("index"),
    CREATE_MEMBER("create-member"),

    LOGIN("login"),
    REFRESH("refresh"),

    CREATE_POST("create-post"),
    GET_AN_POST("get-an-post"),
    UPDATE_AN_POST("update-an-post"),
    DELETE_AN_POST("delete-an-post");

    public String relation;

    DocsRelations(String relation) {
        this.relation = relation;
    }

    public String path() {
        String profileLink = "/docs/index.html#resources-";
        return profileLink + this.relation;
    }

    public String profileName(){
        return this.relation.concat("-").concat(PROFILE.relation);
    }
}
