package org.aleksid.wikime.controller;


import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.service.ArticlesService;
import org.aleksid.wikime.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;


@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    private final ArticlesService articlesService;
    private final UserService userService;

    public MainController(ArticlesService articlesService, UserService userService) {
        this.articlesService = articlesService;
        this.userService = userService;
    }

    @GetMapping("/wikime")
    public String greeting() {
        logger.info("Main page");//оставлю это здесь, как пример того как быть не должно
        //всё-таки логгирование - это сквозная функциональность
        //UPD, AspectJ - зло, по мнению сообщества
        return "welcome";
    }

    @GetMapping("/articles")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model) {
        List<Article> articles;
        if (filter != null && !filter.isEmpty()) {

            logger.info(String.format("Articles requested with filter %s", filter));

            articles = articlesService.getArticlesContainingTags(articlesService.createTagsFromRequest(filter));
            if (articles.isEmpty()){
                model.addAttribute("not_found_message", "No articles found");
            }
        } else {

            logger.info("All articles requested");

            articles = articlesService.getAllArticles();
        }
//        System.out.println(articles);
        model.addAttribute("articles", articles);
        return "articles";
    }

    @GetMapping("/article")
    public String showArticle(
            @RequestParam(required = false, defaultValue = "noid") String id, Model model) {

        if (id.equals("noid")) return "redirect:/articles";

        Article article = articlesService.getArticleByID(Integer.parseInt(id));
        if (article.getId() != 0) model.addAttribute("article", article);

          logger.info(String.format("Article with id = %s has been requested ", id));

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

        if (id.equals("0")) {
            logger.info("New article adding form requested");
        } else {
            logger.info(String.format("Requested article edit form for id = %s", id));
        }

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

            logger.info(String.format("Article with id = %d changed author, new author is %s", articleId, loggedUser.getUsername()));

        } else {
            article.setAuthor(userService.getUserByName(author));
        }

        if (articleId == 0) {//new message
            final Article article1 = articlesService.addArticle(article);
            articleId = article1.getId();

            logger.info(String.format("New article was added by %s and got id %d", loggedUser.getUsername(), articleId));

        } else {//update existing
            final Article update = articlesService.update(article);
            articleId = update.getId();

            logger.info(String.format("Article id = %d was changed by %s", articleId, loggedUser.getUsername()));

        }
        return "redirect:/article?id=" + articleId;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String id) {
        articlesService.deleteArticleByID(Integer.parseInt(id));

        logger.info(String.format("Article id = %s has been removed", id));

        return "redirect:/articles";
    }
}