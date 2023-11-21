package HiK.HiKServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="learning_content_id")
    private LearningContent learningContent;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private ZonedDateTime timestamp;

    public Sentence(Long sentenceId, String srcSentence, String place, String listener, int intimacy, String translatedSentence, String voiceFile) {
        this.id = sentenceId;
        this.srcSentence = srcSentence;
        this.place = place;
        this.listener = listener;
        this.intimacy = intimacy;
        this.translatedSentence = translatedSentence;
        this.voiceFile = voiceFile;
        timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
