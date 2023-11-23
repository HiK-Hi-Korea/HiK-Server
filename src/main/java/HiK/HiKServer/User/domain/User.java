package HiK.HiKServer.User.domain;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String name;
    private int age;
    private String gender;
    private String nation;
    private String language;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LearningContent> learningContentList = new ArrayList<>();

//    // login
//    private String loginId;
//    private String password;
//    private UserRole role;
//
//    // OAuth 로그인에 사용
//    private String provider;
//    private String providerId;

}
