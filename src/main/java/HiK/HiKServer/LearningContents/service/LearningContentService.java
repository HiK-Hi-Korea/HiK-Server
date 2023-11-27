package HiK.HiKServer.LearningContents.service;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.LearningContentDto;
import HiK.HiKServer.LearningContents.dto.SentenceListDto;
import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.Translator.domain.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LearningContentService {

    @Autowired
    private LearningContentRepository learningContentRepository;

    public List<SentenceListDto>  getSentenceList(Long lcId){
        LearningContent learningContent = learningContentRepository.findById(lcId).orElseThrow(()->new IllegalAccessError("해당 학습 컨텐츠를 찾을 수 없습니다."));
        List<Sentence> sentenceList = learningContent.getSentenceList();
        List<SentenceListDto> sentenceListDtos = new ArrayList<>();
        for (Sentence sentence :sentenceList){
            sentenceListDtos.add(new SentenceListDto(sentence.getId(), sentence.getSrcSentence(), sentence.getPlace(), sentence.getListener(), sentence.getIntimacy(), sentence.getTranslatedSentence(), sentence.getVoiceFile(), sentence.getTimestamp()));
        }
        return sentenceListDtos;
    }
}
