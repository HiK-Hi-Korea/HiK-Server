package HiK.HiKServer.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
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
}
