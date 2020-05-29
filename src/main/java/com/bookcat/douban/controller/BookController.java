package com.bookcat.douban.controller;

import com.bookcat.douban.entity.ActivityEntity;
import com.bookcat.douban.entity.BooksEntity;
import com.bookcat.douban.formbean.Activity;
import com.bookcat.douban.formbean.Book;
import com.bookcat.douban.repositories.ActivityRepository;
import com.bookcat.douban.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    private final BookRepository repository;
    private final ActivityRepository activityRepository;
    BookController(BookRepository repository, ActivityRepository activityRepository){
        this.repository = repository;
        this.activityRepository = activityRepository;
    }

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
        List<Activity> activityList =new ArrayList<>();
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
    @PostMapping("/book")
    List<Book> findByKey(@RequestParam("key") String key) {
        List<BooksEntity> retEntity = repository.findByKey(key);
        List<Book>  ret = new ArrayList<Book>();

        if (retEntity.size()!=0) {
            for (BooksEntity booksEntity : retEntity) {
                Book book = new Book();
                BeanUtils.copyProperties(booksEntity, book);
                ret.add(book);
            }
        }
        return ret;
    }
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
//    @PostMapping("/find")
//    @ResponseBody
//    List<BooksEntity> findByName(@RequestParam("key") String key) {
//        return repository.findByNameOrsOrSubNameOrsOrSeriesOrpOrPublisherOrAuthorsOrTagsLike(key);
//    }

    @GetMapping("/book/{id}")
    Book findById(@PathVariable int id ) {
        BooksEntity retEntity = repository.findById(id);
        Book ret = new Book();
        BeanUtils.copyProperties(retEntity, ret);

        return ret;
    }

//    @GetMapping("/book/recommend")//推荐书籍
//    List<Book> findRecommend() {
//        List<BooksEntity> recommendBookEntityList = repository.findRecommend();
//        List<Book>  recommendBooks = new ArrayList<Book>();
//
//        if (recommendBookEntityList.size()!=0) {
//            for (BooksEntity booksEntity : recommendBookEntityList) {
//                Book book = new Book();
//                BeanUtils.copyProperties(booksEntity, book);
//                recommendBooks.add(book);
//            }
//        }
//        return recommendBooks;
//    }

//    @GetMapping("/book/hot")//热门书籍
//    List<Book> findHot() {
//        List<BooksEntity> hotBookEntityList = repository.findHot();
//        List<Book>  hotBooks = new ArrayList<>();
//        if (hotBookEntityList.size()!=0) {
//            for (BooksEntity booksEntity : hotBookEntityList) {
//                Book book = new Book();
//                BeanUtils.copyProperties(booksEntity, book);
//                hotBooks.add(book);
//            }
//        }
//        return ret;
//    }

//    @GetMapping("/book/latest")//新书
//    List<Book> findLatest() {
//        List<BooksEntity> latestBookEntityList = repository.findLatest();
//        List<Book>  latestBooks = new ArrayList<Book>();
//
//        if (latestBookEntityList.size()!=0) {
//            for (BooksEntity booksEntity : latestBookEntityList) {
//                Book book = new Book();
//                BeanUtils.copyProperties(booksEntity, book);
//                latestBooks.add(book);
//            }
//        }
//        return latestBooks;
//    }

    @PostMapping("/look/{id}")
    int look(@PathVariable int id ) {
        int ret = repository.look(id);
        return ret;
    }
}
