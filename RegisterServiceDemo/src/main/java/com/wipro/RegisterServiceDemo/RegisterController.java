package com.wipro.RegisterServiceDemo;



import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RegisterController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
     private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${pivotal.tradeservicename.name}")
     protected String tradeService;



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
        return "<html><body bgcolor='coral'>Registered Successfully"+"<a href='http://localhost:8085/index.html'>home to login</a>"+"</body></html>";

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


    //method for understanding hystrix
    @HystrixCommand(fallbackMethod = "defaultCountries",
            commandProperties = {
            @HystrixProperty(
                    name="circuitBreaker.errorThresholdPercentage", value = "20"),
             @HystrixProperty(
                     name="circuitBreaker.sleepWindowInMilliseconds", value = "7000")
            })
    @RequestMapping(value = "/countries/all", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String displayCountries()
    {
        Map<String,Object> aa= new HashMap<String, Object>();

        ResponseEntity<String> result = restTemplate.getForEntity("https://restcountries.eu/rest/v2/all",String.class,aa);
        if(result.getStatusCode()== HttpStatus.OK)
        {
            return result.getBody().toString();
        }else{
            return "";
        }
    }

    //default method when fallback accurs at above method
    public String defaultCountries()
    {
        return "India:New Delhi Asia";
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    @ResponseBody
    public String loginUser(@ModelAttribute("userid")String userid, @ModelAttribute("password") String password, HttpServletRequest request, HttpServletResponse response)
    {
        User uu=users.get(userid);
        request.getSession().setAttribute("user", uu);

        if (users.get(userid)!=null) {
            if (users.get(userid).getPassword().equals(password)) {

                //use below two lines for eureka
                /*List<ServiceInstance> instances = discoveryClient.getInstances(tradeService);
                URI uri = instances.get(0).getUri();*/

                //use below 3 lines code for ribbon
                ServiceInstance instance = loadBalancerClient.choose(tradeService);
                URI uri = URI.create(String.format("http://%s:%s",
                        instance.getHost(),instance.getPort()));


                System.out.println("Register-Service.loginUser .URI=========" + uri);
                String url = uri.toString() + "/Trade.html";
                System.out.println("====================================");
                System.out.println("Register-Service.loginUser .URI=========" + uri);

                try {
                    response.sendRedirect(url);
                } catch (Exception e) {

                    System.out.println("Error in dispatching");
                }

                return "Error in dispatching";

            }
            else{
                return "<html><body bgcolor='coral'>your credential is wrong !!!!"+"<a href='http://localhost:8085/Login.html'>login</a>"+"</body></html>";
            }
        }
        else{
                return "<html><body bgcolor='coral'>Sorry your have not Registered !!!"+"<a href='http://localhost:8085/index.html'>HomePage</a>"+"</body></html>";

        }



    }

}
