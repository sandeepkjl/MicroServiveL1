package com.wipro.monolothic;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {


    Map<String,User> users=new HashMap<String,User>();

    public RegisterController()
    {
        User uu=new User("geetha","geetha","geetha@gmail.com");
        users.put("geetha", uu);
    }


    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    @ResponseBody
    public String registerUser(@ModelAttribute("userid")String userid, @ModelAttribute("password") String password, @ModelAttribute("email")String email )
    {
        User u=new User(userid,password,email);
        users.put(userid, u);
        return "<html><body bgcolor='coral'>Registered Successfully"+"<a href='http://localhost:7070/index.html'>home to login</a>"+"</body></html>";

    }

    @RequestMapping(value = "/users/all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,User> getAllRegisteredUsers()
    {
        return users; //map
    }


    @RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable("userid")String userid)
    {
        return users.get(userid);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("userid")String userid, @ModelAttribute("password") String password, HttpServletRequest request)
    {
        User uu=users.get(userid);
        request.getSession().setAttribute("user", uu);

        if (users.get(userid)!=null)
        {
            if(users.get(userid).getPassword().equals(password))
            {
                return "Trade";
            }
            else
            {
                return "PasswordError";
            }

        }
        else
        {
            return "Sorry";
        }

    }


}
