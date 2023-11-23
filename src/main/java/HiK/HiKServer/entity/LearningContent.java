package HiK.HiKServer.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "learning_content")
@Entity
public class LearningContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "learning_content", cascade = CascadeType.ALL)
    private List<Sentence> sentenceList = new ArrayList<>();

    public void addSentence(Sentence sentence) {
        sentenceList.add(sentence);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
