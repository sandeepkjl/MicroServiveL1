package com.wipro.TradeService;


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
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TradeController {


    //User user = new User();

    Map<String, Double> companies = new HashMap<String, Double>();


    Map<String, Trade> trades = new HashMap<String, Trade>();




    public TradeController() {

        companies.put("WIPRO", 298.45);

        companies.put("INFY", 949.95);

        companies.put("TCS", 2713.70);

        companies.put("TECHM", 485.85);

    }

    @Autowired
    private RestTemplate restTemplate;

    //for eureka discovery service
    @Autowired
    private DiscoveryClient discoveryClient;

    //for ribbon load balancer
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${pivotal.registerservice.name}")
     protected String registerService;

    User user;

    @RequestMapping(value = "/trade/do", method = RequestMethod.POST)
    @ResponseBody
    public String tradeDo(@ModelAttribute("ticker") String ticker, @ModelAttribute("qty") int qty, HttpServletRequest request) {

        Double price = companies.get(ticker);
        Trade t = new Trade(ticker, price, qty);
        double total = price * qty;
        t.setTotalCost(total);


        trades.put(ticker, t);


        //use below two lines for eureka
        /*List<ServiceInstance> instances= discoveryClient.getInstances(registerService);
        URI uri=instances.get(0).getUri();*/

        //use below 3 lines code for ribbon
        ServiceInstance instance = loadBalancerClient.choose(registerService);
        URI uri = URI.create(String.format("http://%s:%s",
                instance.getHost(),instance.getPort()));

        System.out.println("Trade-Service.tradedo() .URI========="+uri);
        String url=uri.toString()+"/users/all";
        System.out.println("========================================");
        System.out.println("Trade-Service.tradedo() .URI========="+url);

        Map<String,Object> aa=new HashMap<String,Object>();

        ResponseEntity<String> result = restTemplate.getForEntity(url,String.class,aa);

        if (result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();

        } else {

            return null;
        }

    }


    @RequestMapping(value = "/trade/all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Trade> getAllRegisteredTrades() {

        return trades;

    }


    @RequestMapping(value = "/trade/{ticker}", method = RequestMethod.GET)
    @ResponseBody
    public Trade getTrade(@PathVariable("ticker") String ticker) {

        return trades.get(ticker);

    }


}
