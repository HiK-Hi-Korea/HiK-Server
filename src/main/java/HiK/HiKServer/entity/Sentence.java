package HiK.HiKServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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


//    public Sentence(String srcSentence, String desSituation, String translatedSentence, String voiceFile) {
//        this.srcSentence = srcSentence;
//        this.desiredSituation = desSituation;
//        this.translatedSentence = translatedSentence;
//        this.voiceFile = voiceFile;
//    }

}
