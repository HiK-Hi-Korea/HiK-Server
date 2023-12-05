package HiK.HiKServer.User.service;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.LearningContentDto;
import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.User.domain.User;
import HiK.HiKServer.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<LearningContent> getLearningContentsByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getLearningContentList();
    }
}