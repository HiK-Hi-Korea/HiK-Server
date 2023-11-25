package HiK.HiKServer.LearningContents.domain;

import HiK.HiKServer.Translator.domain.Sentence;
import HiK.HiKServer.User.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
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

    private String place;
    private String listener;
    private LocalDateTime timestamp;

    public void addSentence(Sentence sentence) {
        sentenceList.add(sentence);
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setTimestamp(LocalDateTime timestamp){ this.timestamp = timestamp;}
    public void setPlace(String place) {
        this.place = place;
    }
    public void setListener(String listener) {
        this.listener = listener;
    }
}
