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
@Table(name = "learning_content")
@Entity
public class LearningContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningContentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "learning_content", cascade = CascadeType.ALL)
    private List<Sentence> sentences = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }
}
