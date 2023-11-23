package HiK.HiKServer.User.domain;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {

    @Id
    private String id;
    private String email;

    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private int age;
    @Column(nullable = true)
    private String gender;
    @Column(nullable = true)
    private String nation;
    @Column(nullable = true)
    private String language;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LearningContent> learningContentList = new ArrayList<>();

    public User(String id, String email){
        this.id = id;
        this.email =email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    //    // login
//    private String loginId;
//    private String password;
//    private UserRole role;
//
//    // OAuth 로그인에 사용
//    private String provider;
//    private String providerId;

}
