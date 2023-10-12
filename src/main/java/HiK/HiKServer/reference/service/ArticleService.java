package HiK.HiKServer.reference.service;

import HiK.HiKServer.reference.dto.ArticleForm;
import HiK.HiKServer.reference.entity.Article;
import HiK.HiKServer.reference.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index(){
        return articleRepository.findAll();
    }

    public Article create(ArticleForm dto){
        Article article = dto.toEntity();
        if (article.getId() != null){
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto){
        // 1. DTO -> Entity로 변환하기
        Article article = dto.toEntity();
        log.info("id: {}, title: {} ",id , article.toString());

        // 2. id 로 db에 존재하는 entity인지 확인
        Article target = articleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리하기
        if (target == null || id != article.getId()){
            log.info("잘못된 요청! id: {}, title: {} ",id , article.toString());
            return null;
        }

        // 4. 업데이트 및 정상 처리하기
        target.patch(article); // 기존 데이터에 새로운 데이터 붙이기(일부 데이터만 수정되는 경우)
        Article updated = articleRepository.save(article);
        return updated;
    }

    public Article delete(Long id){
        // repository 에 id에 해당하는 아티클 객체 찾기
        Article target = articleRepository.findById(id).orElse(null);
        // 오류 처리
        if (target == null){
            return null;
        }
        // 있으면 삭제해주기 (정상처리)
        articleRepository.delete(target);
        return target;
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos){
        // 1. DTO 묶음을 엔티티 묶음으로 바꾸기
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());
        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream().forEach(article -> articleRepository.save(article));
        // 3. 강제 예외 발생시키기
        articleRepository.findById(-1L).orElseThrow(()-> new IllegalArgumentException("결제 실패!"));
        // 4. 결과값 반환하기
        return articleList;
    }
}
