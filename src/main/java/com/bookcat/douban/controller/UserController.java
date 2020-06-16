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
import java.util.regex.Pattern;

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
    String loginGet(Model model,HttpServletRequest request) {
        model.addAttribute("msg",request.getParameter("msg"));
        model.addAttribute("success",request.getParameter("success"));
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView loginPost(HttpServletRequest request, HttpServletResponse response, Model model) {
        if(!Pattern.compile("[0-9]*").matcher(request.getParameter("inputSid")).matches()){
            ModelAndView newone =  new ModelAndView("redirect:/login");
            newone.addObject("msg","id为数字");
            return newone;
        }
        int sid = Integer.parseInt(request.getParameter("inputSid"));
        String password = request.getParameter("inputPassword");
        UsersEntity user = repository.findByUserId(sid);
        System.out.println(sid+"  "+"   "+password);
        if(user == null) {
            ModelAndView newone =  new ModelAndView("redirect:/login");
            newone.addObject("msg","用户不存在");
            return newone;
        }
        System.out.println(user.getPassword());
        System.out.println(password);
        if(!user.getPassword().equals(password)) {
            ModelAndView newone =  new ModelAndView("redirect:/login");
            newone.addObject("msg","密码错误");
            return newone;
        }
        Cookie cookie1 = new Cookie("userId",Integer.toString(sid));
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("userName",user.getUserName());
        response.addCookie(cookie2);
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/register")
    String registerGet(Model model,HttpServletRequest request) {
        model.addAttribute("msg",request.getParameter("msg"));
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerPost(HttpServletRequest request, HttpServletResponse response) {
        if(!Pattern.compile("[0-9]*").matcher(request.getParameter("inputSid")).matches()){
            ModelAndView newone =  new ModelAndView("redirect:/register");
            newone.addObject("msg","id为数字");
            return newone;
        }
        int sid = Integer.parseInt(request.getParameter("inputSid"));
        if(repository.existsById(sid)){
            ModelAndView newone =  new ModelAndView("redirect:/register");
            newone.addObject("msg","用户id已存在");
            return newone;
        }
        String name = request.getParameter("inputName");
        String password = request.getParameter("inputPassword");
        String email = request.getParameter("inputEmail");
        UsersEntity entity = new UsersEntity();
        entity.setPassword(password);
        entity.setUserName(name);
        entity.setUserId(sid);
        entity.setEmail(email);
        entity.setUserSummary("世界上任何书籍都不能带给你好运，但是它们能让你悄悄成为你自己。");
        repository.save(entity);
        ModelAndView newone =  new ModelAndView("redirect:/login");
        newone.addObject("success","注册成功，要记住自己的用户名哦！"+sid);
        return newone;
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
    public ModelAndView score(@PathVariable int userid,@PathVariable int bookid,@PathVariable int score){
        int ret = repository.score(score,bookid);
        repository.rating(userid,bookid,score);
        ModelAndView newone =  new ModelAndView("redirect:/book/"+bookid);
        newone.addObject("success","成功修改评分为"+score);
        return newone;
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

        return "moments";
    }

    //个人中心
    //个人中心
    @GetMapping("/userCenter/{id}")
    String userCenterGet(Model model, HttpServletRequest request,@PathVariable int id) {
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

        if(id == 0){
            return "login";
        }

        else{
            UsersEntity userEntity = repository.findByUserId(id);
            User user = new User();
            BeanUtils.copyProperties(userEntity,user);
            model.addAttribute("user",user);

            List<CommentEntity> commentEntityList = commentRepository.findAllByUserId(id);
            List<Comment> commentList = new ArrayList<>();
            Collections.reverse(commentEntityList);
            for (CommentEntity comment:commentEntityList) {
                Comment newComment = new Comment();
                BeanUtils.copyProperties(comment,newComment);
                commentList.add(newComment);
            }
            model.addAttribute("comments",commentList);

            int uid = Integer.parseInt(model.getAttribute("userId").toString());
            FriendsEntity friendsEntity = friendRepository.ifFriend(uid,id);
            if(friendsEntity != null){
                model.addAttribute("isFriend",1);
            }
            else{
                model.addAttribute("isFriend",0);
            }

            return "userCenter";
        }
    }

    //删除动态
    @GetMapping("/delComment/{uid}/{id}")
    public ModelAndView delCommentPost(@PathVariable int uid,@PathVariable int id){
        commentRepository.delComment(uid,id);

        return new ModelAndView("redirect:/userCenter/"+uid);
    }

    //发表评论
    @PostMapping("/comment")
    public ModelAndView commentPost(Model model, HttpServletRequest request) {
        int bookid = Integer.parseInt(request.getParameter("bookid"));
        String title = request.getParameter("title");
        String context = request.getParameter("context");
        int uid = Integer.parseInt(request.getParameter("uid"));
        String uname = request.getParameter("uname");

        commentRepository.comment(bookid,title,context,uid,uname);

        if(bookid==0)
            return new ModelAndView("redirect:/moments");
        else
            return new ModelAndView("redirect:/book/"+bookid);
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

    //发送好友请求
    @GetMapping("/addFriend/{uid}/{fid}")
    public ModelAndView addFriendPost(@PathVariable int uid,@PathVariable int fid){
        friendRepository.addFriend(uid,fid);
        friendRepository.addFriend2(fid,uid);

        return new ModelAndView("redirect:/friends");
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
    @GetMapping("/rank")
    String rankGet(Model model, HttpServletRequest request) {
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

        List<RankEntity> rankEntityList = rankRepository.countRes();
        List<Rank> rankList = new ArrayList<>();
        for(RankEntity rank : rankEntityList){
            Rank newRank = new Rank();
            BeanUtils.copyProperties(rank,newRank);
            rankList.add(newRank);
        }
        model.addAttribute("ranks",rankList);

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

        return "rank";
    }

}
