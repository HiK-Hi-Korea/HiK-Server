package HiK.HiKServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Table(name = "user")
@Entity
public class User {

    @Id
    private String userId;

    @Column
    private String username;
    @Column
    private int age;
    @Column
    private String gender;
    @Column
    private String nation;
    @Column
    private String language;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LearningContent> learningContentList = new ArrayList<>();
}
