package com.taskmanager.app.controller;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.dto.UserData;
import com.taskmanager.app.service.MyProjectService;
import com.taskmanager.app.service.MyTaskService;
import com.taskmanager.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@RequestMapping("/user")
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyProjectService myProjectService;
    @Autowired
    private MyTaskService myTaskService;
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/profile")
    public String getProfile(OAuth2AuthenticationToken token, Model model) {
        OAuth2User auth2User = token.getPrincipal();
        if(!Objects.isNull(auth2User)) {
            UserData userData = userService.findByEmail(auth2User.getAttribute("email"));
            model.addAttribute("userData", userData);
            List<MyProject> projects = userData.getProjects();
            model.addAttribute("projectSize", projects.size());
            int result = 0;
            for(MyProject project : projects) {
                for(MyTask myTask : project.getTasks()) {
                    result += 1;
                }
            }
            model.addAttribute("taskSize",result);
        }

        return "profile";
    }
    @RequestMapping("/login/oauth2/code/google")
    public RedirectView oauth2Login() {
        System.out.println("hello");
        return new RedirectView("/app/dashboard");
    }
}
