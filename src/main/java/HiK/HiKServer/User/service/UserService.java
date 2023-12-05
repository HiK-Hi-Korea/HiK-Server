package HiK.HiKServer.User.service;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.LearningContentDto;
import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.User.domain.User;
import HiK.HiKServer.User.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LearningContentRepository learningContentRepository;

    public List<LearningContent> getUserContentList(String userId){
        List<LearningContent> learningContentList = learningContentRepository.getByUserId(userId);
        return learningContentList;
    }

    public List<LearningContentDto> getUserContentDtoList(String userId){
        List<LearningContent> learningContentList = learningContentRepository.getByUserId(userId);
        List<LearningContentDto> learningContentDtos = new ArrayList<>();
        for(LearningContent learningContent : learningContentList){
            learningContentDtos.add(new LearningContentDto(learningContent.getId(), learningContent.getPlace(), learningContent.getListener(), learningContent.getIntimacy(), learningContent.getTimestamp(), learningContent.getSentenceList().get(0).getSrcSentence()));
        }
        return learningContentDtos;
    }

    public void setUserAge(String userId, int age){
        User user = userRepository.findById(userId).orElseThrow();
        user.setAge(age);
        log.info("user set age: ", user.getAge());
    }
    public void setUserNation(String userId, String nation){
        User user = userRepository.findById(userId).orElseThrow();
        user.setNation(nation);
        log.info("user set nation: ", user.getNation());
    }
    public void setUserLanguage(String userId, String language){
        User user = userRepository.findById(userId).orElseThrow();
        user.setLanguage(language);
        log.info("user set language: ", user.getLanguage());
    }
    public void setUserGender(String userId, String gender){
        User user = userRepository.findById(userId).orElseThrow();
        user.setGender(gender);
        log.info("user set gender: ", user.getGender());
    }
//    public List<LearningContent> getLearningContentsByUserId(String userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        return user.getLearningContentList();
//    }
}