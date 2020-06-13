package com.bookcat.douban.controller;

import com.bookcat.douban.entity.ActivityEntity;
import com.bookcat.douban.entity.BooksEntity;
import com.bookcat.douban.entity.UsersEntity;
import com.bookcat.douban.formbean.Activity;
import com.bookcat.douban.formbean.Book;
import com.bookcat.douban.formbean.User;
import com.bookcat.douban.repositories.ActivityRepository;
import com.bookcat.douban.repositories.BookRepository;
import com.bookcat.douban.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private final UserRepository repository;
    private final ActivityRepository activityRepository;
    private final BookRepository bookRepository;
    UserController(UserRepository repository,ActivityRepository activityRepository,BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping("/user/0")
    String book0Get(Model model) {
        return "login";
    }
    @GetMapping("/login")
    String loginGet(Model model) {
        return "login";
    }

    @PostMapping("/login")
    String loginPost(HttpServletRequest request, HttpServletResponse response, Model model) {
        int sid = Integer.parseInt(request.getParameter("inputSid"));
        String password = request.getParameter("inputPassword");
        UsersEntity user = repository.findByUserId(sid);
        System.out.println(sid+"  "+"   "+password);
        if(user == null) {
            model.addAttribute("msg", "用户不存在");
            return "login";
        }
        System.out.println(user.getPassword());
        System.out.println(password);
        if(!user.getPassword().equals(password)) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }
        model.addAttribute("userId",Integer.toString(sid));
        model.addAttribute("userName",user.getUserName());
        Cookie cookie1 = new Cookie("userId",Integer.toString(sid));
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("userName",user.getUserName());
        response.addCookie(cookie2);
        List<ActivityEntity> activityEntityList = activityRepository.findBySelected(1);
        List<Activity> activityList =new ArrayList<>();
        for (ActivityEntity activityEntity:activityEntityList){
            Activity newOne = new Activity();
            BeanUtils.copyProperties(activityEntity,newOne);
            activityList.add(newOne);
        }
        model.addAttribute("activityList",activityList);

        //热门书籍
        List<BooksEntity> hotBookEntityList = bookRepository.findHot();
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
        List<BooksEntity> recommendBookEntityList = bookRepository.findRecommend();
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
        List<BooksEntity> latestBookEntityList = bookRepository.findLatest();
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



    @PostMapping("/add")
    @ResponseBody
    int add(@RequestBody User user){
        UsersEntity userEntity = new UsersEntity();
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userEntity = this.repository.save(userEntity);

        if(userEntity != null)
            return 1;
        else
            return 0;
    }

    @PostMapping("/modify")
    @ResponseBody
    int modify(@RequestBody User user){
        UsersEntity userEntity = this.repository.findByUserId(user.getUserId());

        if(userEntity != null){
            userEntity.setPassword(user.getPassword());
            userEntity = this.repository.save(userEntity);
            return 1;
        }

        else
            return 0;
    }



}
