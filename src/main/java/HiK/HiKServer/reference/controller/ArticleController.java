package HiK.HiKServer.reference.controller;

import HiK.HiKServer.reference.dto.ArticleForm;
import HiK.HiKServer.reference.entity.Article;
import HiK.HiKServer.reference.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm(){

        return "articles/news";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm articleForm){
        // System.out.println(articleForm.toString());
        log.info(articleForm.toString());
        // 1. DTO를 엔티티로 변환
        Article article = articleForm.toEntity();
        // System.out.println(article.toString());
        log.info(article.toString());

        // 2. 리파지토리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        // System.out.println(saved.toString());
        log.info(saved.toString());
        return "redirect:/articles/"+saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id= "+id);
        // 1. id를 조회해 데이터 가져오기
        // Optional<Article> articleEntity = articleRepository.findById(id);
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        // 1. DB에서 모든 Article 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
        // 2. 가져온 Article 묶음을 모델에 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 사용자에게 보여 줄 뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        Article articleEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("articles", articleEntity);
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());
        //1. DTO를 엔티티로 변환하기
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        //2. 엔티티를 DB에 저장하기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target != null){
            articleRepository.save(articleEntity);
        }
        //3. 수정결과 페이지로 리다이렉트 하기
        return"redirect:/articles/"+articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!");
        // 1. 삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        // 2. 대상 엔티티 삭제하기
        if (target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제되었습니다.");
        }
        // 3. 결과페이지로 리다이렉트하기
        return "redirect:/articles";
    }
}
