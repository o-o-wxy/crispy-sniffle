package com.bookcat.douban.controller;

import com.bookcat.douban.entity.*;
import com.bookcat.douban.formbean.*;
import com.bookcat.douban.repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UserController {
    private final UserRepository repository;
    private final ActivityRepository activityRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final FriendRepository friendRepository;
    private final RankRepository rankRepository;
    UserController(UserRepository repository, ActivityRepository activityRepository, BookRepository bookRepository, CommentRepository commentRepository, FriendRepository friendRepository, RankRepository rankRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.activityRepository = activityRepository;
        this.commentRepository = commentRepository;
        this.friendRepository = friendRepository;
        this.rankRepository = rankRepository;
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

    @GetMapping("/score/{userid}/{bookid}/{score}")
    @ResponseBody
    int score(@PathVariable int userid,@PathVariable int bookid,@PathVariable int score){
        int ret = repository.score(score,bookid);
        repository.rating(userid,bookid,score);
        return  ret;
    }

    //修改资料 (未写
    @GetMapping("/modify")
    String modifyGet(Model model, HttpServletRequest request) {
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
        return "modify";
    }

    //麦圈
    @GetMapping("/moments")
    String momentsGet(Model model, HttpServletRequest request) {
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

        List<CommentEntity> commentEntityList = commentRepository.findAll();
        List<Comment> commentList = new ArrayList<>();
        Collections.reverse(commentEntityList);
        for (CommentEntity comment:commentEntityList) {
            Comment newComment = new Comment();
            BeanUtils.copyProperties(comment,newComment);
            commentList.add(newComment);
        }
        model.addAttribute("comments",commentList);
        return "moments";
    }

    //个人中心
    @GetMapping("/userCenter")
    String userCenterGet(Model model, HttpServletRequest request) {
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

        List<CommentEntity> commentEntityList = commentRepository.findAll();
        List<Comment> commentList = new ArrayList<>();
        Collections.reverse(commentEntityList);
        for (CommentEntity comment:commentEntityList) {
            Comment newComment = new Comment();
            BeanUtils.copyProperties(comment,newComment);
            commentList.add(newComment);
        }
        model.addAttribute("comments",commentList);

        int id = Integer.parseInt(model.getAttribute("userId").toString());
        String sig = repository.findByUserId(id).getUserSummary();
        if(sig != null){
            model.addAttribute("userSummary",sig);
        }

        List<Friend> friendList = new ArrayList<>();
        for (Comment comment:commentList) {
            FriendsEntity friendsEntity = friendRepository.findByFriendIdAndUserId(comment.getUserId(),id);
            if(friendsEntity != null){
                Friend newFriend = new Friend();
                BeanUtils.copyProperties(friendsEntity,newFriend);
                friendList.add(newFriend);
            }
            else{
                continue;
            }
        }
        model.addAttribute("friends",friendList);

        return "userCenter";
    }

    //发表评论
    @PostMapping("/comment")
    public ModelAndView commentPost(Model model, HttpServletRequest request) {
        int bookid = 0;
        String title = request.getParameter("title");
        String context = request.getParameter("context");
        int uid = Integer.parseInt(request.getParameter("uid"));
        String uname = request.getParameter("uname");

        commentRepository.comment(bookid,title,context,uid,uname);

        return new ModelAndView("redirect:/moments");
    }

    //麦友
    @GetMapping("/friends")
    String friendsGet(Model model, HttpServletRequest request) {
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

        int id = Integer.parseInt(model.getAttribute("userId").toString());
        List<FriendsEntity> friendsEntityList = friendRepository.findAllByUserId(id);
        List<Friend> friendList = new ArrayList<>();
        Collections.reverse(friendsEntityList);
        for (FriendsEntity friend:friendsEntityList) {
            Friend newFriend = new Friend();
            BeanUtils.copyProperties(friend,newFriend);
            friendList.add(newFriend);
        }
        model.addAttribute("friends",friendList);
        List<User> userList = new ArrayList<>();
        for(Friend friend: friendList){
            UsersEntity userEntity = repository.findByUserId(friend.getFriendId());
            User newUser = new User();
            BeanUtils.copyProperties(userEntity,newUser);
            userList.add(newUser);
        }
        model.addAttribute("users",userList);
        return "friends";
    }

    //好友请求同意
    @GetMapping("/agree/{uid}/{fid}")
    public ModelAndView agreePost(@PathVariable int uid,@PathVariable int fid){
        friendRepository.agree(uid,fid);
        friendRepository.agree(fid,uid);

        return new ModelAndView("redirect:/friends");
    }

    //好友请求忽略
    @GetMapping("/disagree/{uid}/{fid}")
    public ModelAndView disagreePost(@PathVariable int uid,@PathVariable int fid){
        friendRepository.disagree(uid,fid);
        friendRepository.disagree(fid,uid);

        return new ModelAndView("redirect:/friends");
    }

    //好友删除
    @GetMapping("/delete/{uid}/{fid}")
    public ModelAndView deletePost(@PathVariable int uid,@PathVariable int fid){
        friendRepository.delete(uid,fid);
        friendRepository.delete(fid,uid);

        return new ModelAndView("redirect:/friends");
    }

    //排行榜
//    @GetMapping("/rank")
//    String rankGet(Model model, HttpServletRequest request) {
//        if(request.getCookies()!=null){
//            for (Cookie cookie:request.getCookies()) {
//                if(cookie.getName().equals("userName"))
//                    model.addAttribute("userName",cookie.getValue());
//                if (cookie.getName().equals("userId"))
//                    model.addAttribute("userId",cookie.getValue());
//            }
//        }
//        if(model.getAttribute("userName")==null)
//            model.addAttribute("userName","登录/注册");
//        if(model.getAttribute("userId")==null)
//            model.addAttribute("userId",0);
//
//        int id = Integer.parseInt(model.getAttribute("userId").toString());
//        List<RatingsEntity> ratingsEntityList = rankRepository.countRes();
//        List<Rating> ratingList = new ArrayList<>();
//        Collections.reverse(ratingList);
//        for (FriendsEntity friend:friendsEntityList) {
//            Friend newFriend = new Friend();
//            BeanUtils.copyProperties(friend,newFriend);
//            friendList.add(newFriend);
//        }
//        model.addAttribute("rank",friendList);
//
//        List<User> userList = new ArrayList<>();
//        for(Friend friend: friendList){
//            UsersEntity userEntity = repository.findByUserId(friend.getFriendId());
//            User newUser = new User();
//            BeanUtils.copyProperties(userEntity,newUser);
//            userList.add(newUser);
//        }
//        model.addAttribute("users",userList);
//
//        return "rank";
//    }

}
