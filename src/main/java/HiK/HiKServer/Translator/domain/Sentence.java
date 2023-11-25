package HiK.HiKServer.Translator.domain;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.User.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "sentence")
@Entity
public class Sentence {
    @Id
    @Column(name = "sentence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="learning_content_id")
    private LearningContent learning_content;

    @Column
    private String srcSentence;

    @Column
    private String place;

    @Column
    private String listener;

    @Column
    private int intimacy;

    @Column
    private String translatedSentence;

    @Column
    private String voiceFile;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column
//    private ZonedDateTime timestamp;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime timestamp;

    public Sentence(Long sentenceId, User user, String srcSentence, String place, String listener, int intimacy, String translatedSentence, String voiceFile) {
        this.id = sentenceId;
        this.user =user;
        this.srcSentence = srcSentence;
        this.place = place;
        this.listener = listener;
        this.intimacy = intimacy;
        this.translatedSentence = translatedSentence;
        this.voiceFile = voiceFile;
        this.timestamp = LocalDateTime.now();
    }
    public void setLearning_content(LearningContent learningContent){
        this.learning_content = learningContent;
    }
}
