package org.aleksid.wikime.controller;


import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.service.ArticlesService;
import org.aleksid.wikime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class MainController {

    private final ArticlesService articlesService;
    private final UserService userService;

    public MainController(ArticlesService articlesService, UserService userService) {
        this.articlesService = articlesService;
        this.userService = userService;
    }

    @GetMapping("/wikime")
    public String greeting() {
        return "welcome";
    }

    @GetMapping("/articles")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model) {
        List<Article> articles;
        if (filter != null && !filter.isEmpty()) {
            articles = articlesService.getArticlesContainingTags(articlesService.createTagsFromRequest(filter));
            if (articles.isEmpty()){
                model.addAttribute("not_found_message", "No articles found");
            }
        } else {
            articles = articlesService.getAllArticles();
        }
        System.out.println(articles);
        model.addAttribute("articles", articles);
        return "articles";
    }

    @GetMapping("/article")
    public String showArticle(
            @RequestParam(required = false, defaultValue = "noid") String id, Model model) {
        if (id.equals("noid")) return "redirect:/articles";
        Article article = articlesService.getArticleByID(Integer.parseInt(id));
        if (article.getId() != 0) model.addAttribute("article", article);
        return "article";
    }

    //не уверен стоило ли рзаносить 2 мапинга по 2м методам и на каком уровне разделять логику
    //сейчас для /new id нет и ставится 0 по уолчанию, а сервис для id=0 выдаёт пустую статью,
    //запрашивая её у ArticleUtil, для чего там создан отдельный метод.
    //Возможно правильнее для возврата пустой статьи сделать отдельный метод уже в сервисе.
    @GetMapping(value = {"/edit", "/new"})
    public String add(
            @RequestParam(required = false, defaultValue = "0") String id,
            Model model) {
        Article article = articlesService.getArticleByID(Integer.parseInt(id));
        model.addAttribute("article", article);
        return "edit";
    }

    @PostMapping("/edit")
    public String save(
            @RequestParam String id,
            @RequestParam String header,
            @RequestParam String tags,
            @RequestParam String text,
            @RequestParam (required = false) String author,
            @AuthenticationPrincipal User loggedUser
    ) {
        int articleId = Integer.valueOf(id);
        Article article = articlesService.constructAndReturn(Integer.parseInt(id), header, tags, text);

        if (author.equals( "<none>")) {
            article.setAuthor(loggedUser);
        } else {
            article.setAuthor(userService.getUserByName(author));
        }

        if (articleId == 0) {//new message
            final Article article1 = articlesService.addArticle(article);
            articleId = article1.getId();
        } else {//update existing
            final Article update = articlesService.update(article);
            articleId = update.getId();
        }
        return "redirect:/article?id=" + articleId;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String id) {
        articlesService.deleteArticleByID(Integer.parseInt(id));
        return "redirect:/articles";
    }
}