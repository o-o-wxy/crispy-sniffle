package com.bookcat.douban.controller;

import com.bookcat.douban.entity.*;
import com.bookcat.douban.formbean.Activity;
import com.bookcat.douban.formbean.Book;
import com.bookcat.douban.repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BookController {
    private final BookRepository repository;
    private final ActivityRepository activityRepository;
    private final ESRepository esRepository;
    private final CommentRepository commentRepository;
    private final RatingsRepository ratingsRepository;
    private final SaleRepository saleRepository;
    BookController(BookRepository repository,CommentRepository commentRepository,SaleRepository saleRepository, RatingsRepository ratingsRepository, ActivityRepository activityRepository,ESRepository esRepository){
        this.repository = repository;
        this.saleRepository =saleRepository;
        this.ratingsRepository = ratingsRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
        this.esRepository = esRepository;
    }
    //首页
    @GetMapping("/index")
    String indexGet(Model model, HttpServletRequest request) {
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        List<ActivityEntity> activityEntityList = activityRepository.findBySelected(1);
        List<Activity> activityList = new ArrayList<>();
        for (ActivityEntity activityEntity:activityEntityList){
            Activity newOne = new Activity();
            BeanUtils.copyProperties(activityEntity,newOne);
            activityList.add(newOne);
        }
        model.addAttribute("activityList",activityList);

        //热门书籍
        List<BooksEntity> hotBookEntityList = repository.findHot();
        List<Book>  hotBooks = new ArrayList<>();
        if (hotBookEntityList.size()!=0) {
            for (BooksEntity booksEntity : hotBookEntityList) {
                Book book = new Book();
                BeanUtils.copyProperties(booksEntity, book);
                hotBooks.add(book);
            }
        }
        model.addAttribute("hotBooks",hotBooks);
        //推荐书籍
        List<BooksEntity> recommendBookEntityList = repository.findRecommend();
        List<Book>  recommendBooks = new ArrayList<Book>();

        if (recommendBookEntityList.size()!=0) {
            for (BooksEntity booksEntity : recommendBookEntityList) {
                Book book = new Book();
                BeanUtils.copyProperties(booksEntity, book);
                recommendBooks.add(book);
            }
        }
        model.addAttribute("recommendBooks",recommendBooks);
        //新书
        List<BooksEntity> latestBookEntityList = repository.findLatest();
        List<Book>  latestBooks = new ArrayList<Book>();

        if (latestBookEntityList.size()!=0) {
            for (BooksEntity booksEntity : latestBookEntityList) {
                Book book = new Book();
                BeanUtils.copyProperties(booksEntity, book);
                latestBooks.add(book);
            }
        }
        model.addAttribute("latestBooks",latestBooks);
        return "index";
    }

    //活动
    @GetMapping("/act")
    String actGet(Model model,HttpServletRequest request){
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        List<ActivityEntity> activityEntityList = activityRepository.findAll();
        List<Activity> activityList = new ArrayList<>();
        for (ActivityEntity activity:activityEntityList) {
            Activity newActivity = new Activity();
            BeanUtils.copyProperties(activity,newActivity);
            activityList.add(newActivity);
        }
        model.addAttribute("activityList",activityList);
        return "act";
    }
    //具体的活动
    @GetMapping("/act/{id}")
    String actContentGet(Model model,HttpServletRequest request,@PathVariable int id){
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        ActivityEntity activityEntity = activityRepository.findById(id);
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityEntity,activity);
        model.addAttribute("activity",activity);
        return "actContent";
    }
    //搜索结果
    @PostMapping("/search")
    String searchPost(Model model,HttpServletRequest request){
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        String key = request.getParameter("key");
        model.addAttribute("bookList",esRepository.findBykey(key));
        return "search";
    }

    @GetMapping("/book/{id}")
    String bookGet(Model model,HttpServletRequest request,@PathVariable int id){
        String username = null;
        int userid = 0;
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName")) {
                    model.addAttribute("userName", cookie.getValue());
                    username = cookie.getValue();
                }
                if (cookie.getName().equals("userId")) {
                    model.addAttribute("userId", cookie.getValue());
                    userid = Integer.parseInt(cookie.getValue());
                }
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        model.addAttribute("book",repository.findById(id));
        List<CommentEntity> commentEntityList = commentRepository.findAllByBookId(id);
        Collections.reverse(commentEntityList);
        model.addAttribute("comments",commentEntityList);
        if(userid != 0){
            RatingsEntity ratingsEntity = ratingsRepository.findByBookIdAndAndUserId(id,userid);
            if(ratingsEntity != null) {
                model.addAttribute("rating", ratingsEntity.getScore());
            }
            else {
                model.addAttribute("rating",0.0);
            }
        }
        model.addAttribute("success",request.getParameter("success"));
        List<SaleEntity> saleEntities = saleRepository.findByBookid(id);
        model.addAttribute("saleEntities",saleEntities);

        return "book";
    }

    @PostMapping("/sale")
    public ModelAndView sale(HttpServletRequest request){
        String href = request.getParameter("href");
        String name = request.getParameter("uname");
        int uid = Integer.parseInt(request.getParameter("uid"));
        int bookid = Integer.parseInt(request.getParameter("bookid"));
        SaleEntity entity = new SaleEntity();
        entity.setBookid(bookid);
        entity.setHref(href);
        entity.setUserid(uid);
        entity.setUsername(name);
        saleRepository.save(entity);
        ModelAndView newone =  new ModelAndView("redirect:/book/"+bookid);
        return newone;
    }

    //麦圈活动
    @GetMapping("/activity")
    String activityGet(Model model, HttpServletRequest request) {
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        return "activity";
    }

    //发布动态
    @GetMapping("/pub")
    String pubGet(Model model, HttpServletRequest request) {
        if(request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()) {
                if(cookie.getName().equals("userName"))
                    model.addAttribute("userName",cookie.getValue());
                if (cookie.getName().equals("userId"))
                    model.addAttribute("userId",cookie.getValue());
            }
        }
        if(model.getAttribute("userName")==null)
            model.addAttribute("userName","登录/注册");
        if(model.getAttribute("userId")==null)
            model.addAttribute("userId",0);
        return "pub";
    }
}
