package HiK.HiKServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Table(name = "sentence")
@Entity
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sentenceId;

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

    @ManyToOne
    @JoinColumn(name = "learningContent_id")
    private LearningContent learningContent;

    public Sentence(Long sentenceId, String srcSentence, String place, String listener, int intimacy, String translatedSentence, String voiceFile) {
        this.sentenceId = sentenceId;
        this.srcSentence = srcSentence;
        this.place = place;
        this.listener = listener;
        this.intimacy = intimacy;
        this.translatedSentence = translatedSentence;
        this.voiceFile = voiceFile;
        timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    }
}
