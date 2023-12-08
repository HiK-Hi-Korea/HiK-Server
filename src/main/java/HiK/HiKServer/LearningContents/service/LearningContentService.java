package HiK.HiKServer.LearningContents.service;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.*;
import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.Translator.domain.Sentence;
import HiK.HiKServer.Translator.dto.TranslationForm;
import HiK.HiKServer.Translator.service.*;
import HiK.HiKServer.gpt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LearningContentService {
    @Value("${chatGPT.apikey}")
    private String chatGPT_apiKey;
    private final static String COMPLETION_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private final static String MODEL="gpt-4";

    @Autowired
    private LearningContentRepository learningContentRepository;
    @Autowired
    TranslationService translationService;

    public List<SentenceListDto>  getSentenceList(Long lcId){
        LearningContent learningContent = learningContentRepository.findById(lcId).orElseThrow(()->new IllegalAccessError("해당 학습 컨텐츠를 찾을 수 없습니다."));
        List<Sentence> sentenceList = learningContent.getSentenceList();
        List<SentenceListDto> sentenceListDtos = new ArrayList<>();
        for (Sentence sentence :sentenceList){
            sentenceListDtos.add(new SentenceListDto(sentence.getId(), sentence.getSrcSentence(), sentence.getPlace(), sentence.getListener(), sentence.getIntimacy(), sentence.getTranslatedSentence(), sentence.getVoiceFile(), sentence.getTimestamp()));
        }
        return sentenceListDtos;
    }

    @Transactional
    public ChangeSentenceResponseDto transSentenceInContent(String userId, ChangeSentenceDto dto) throws IOException {
        String srcSentence = dto.getInput_sentence();
        String prev_place = dto.getPrev_place();
        String prev_listener = dto.getPrev_listener();
        int prev_intimacy = dto.getPrev_intimacy();
        String cur_place = dto.getCur_place();
        String cur_listener = dto.getCur_listener();
        int cur_intimacy = dto.getCur_intimacy();

        GptPrompt_Learning gptPrompt_trans = new GptPrompt_Learning(srcSentence, prev_place, prev_listener, prev_intimacy, cur_place, cur_listener, cur_intimacy);

        String system = gptPrompt_trans.getSystem();
        String prompt = gptPrompt_trans.getPrompt();

        String targetSentence = translationService.createTargetSentence(system, prompt);

        //log.info("system: "+system, "\nprompt: "+prompt);
        log.info("다른 상황에서 적용된 target Sentece: "+targetSentence);
        if (targetSentence == null) {
            throw new IllegalStateException("chat gpt - generate diff trans sentence 실패!");
        }
        // TTS 해서 voiceFile 받아오기 (파일 경로 반환)
        // 할 것인지?
        String voiceFile = translationService.getTTS(targetSentence);
        return new ChangeSentenceResponseDto(targetSentence, voiceFile);
    }

    @Transactional
    public ChangeSentenceResponseDto getOriginalTranslate(TranslationForm dto) throws IOException {
        String srcSentence = dto.getSourceSentence();
        String place = dto.getPlace();
        String listener = dto.getListener();
        int intimacy = dto.getIntimacy();
        GptPrompt_Learning gptPrompt_trans = new GptPrompt_Learning(srcSentence, place, listener, intimacy);

        log.info("Learning Content - Translation Start");
        // Chain of Responsibility 패턴 이용
        PromptHandler university_handler = new PromptHandler_Universty();
        PromptHandler school_handler = new PromptHandler_School();
        PromptHandler online_transaction_handler = new PromptHandler_OnlineTransaction();
        PromptHandler online_chatting_handler = new PromptHandler_OnlineChatting();
        PromptHandler general_handler = new PromptHandler_General();
        university_handler.setSuccessor(school_handler);
        school_handler.setSuccessor(online_transaction_handler);
        online_transaction_handler.setSuccessor(online_chatting_handler);
        online_chatting_handler.setSuccessor(general_handler);

        university_handler.handleRequest(gptPrompt_trans, place, listener);

        String system = gptPrompt_trans.getSystem();
        String prompt = gptPrompt_trans.getPrompt();

//        String targetSentence = translationService.createTargetSentence(system, prompt);
        String temptargetSentence = translationService.createTargetSentence(system, prompt);
        String targetSentence = temptargetSentence.replace("<Output>","");

        //log.info("system: "+system, "\nprompt: "+prompt);
        log.info("Learning Content - Other Situation Translation(다른상황에서 적용된 target Sentece):  "+targetSentence);
        if (targetSentence == null) {
            throw new IllegalStateException("chat gpt - generate diff trans sentence 실패!");
        }

        String voiceFile = translationService.getTTS(targetSentence);
        return new ChangeSentenceResponseDto(targetSentence, voiceFile);
    }
    public ReasonResponseDto getTransReason(String userId, ReasonRequestDto requestDto){
        String input_sentence= requestDto.getInput_sentence();
        String input_place = requestDto.getInput_place();
        String input_listener = requestDto.getInput_listener();
        int input_intimacy = requestDto.getInput_intimacy();
        String translated_sentence = requestDto.getTranslated_sentence();
        String place = requestDto.getPlace();
        String listener = requestDto.getListener();
        int intimacy = requestDto.getIntimacy();

        GptPrompt_Learning gptPrompt_reason = new GptPrompt_Learning(input_sentence, input_place, input_listener, input_intimacy, translated_sentence, place, listener, intimacy);
        String system = gptPrompt_reason.getSystem();
        String prompt = gptPrompt_reason.getPrompt();

        String transReason = translationService.createTargetSentence(system, prompt);

        if (transReason == null){
            throw new IllegalStateException("chat gpt - generate reason 실패!");
        }
        return new ReasonResponseDto(transReason);
    }
}