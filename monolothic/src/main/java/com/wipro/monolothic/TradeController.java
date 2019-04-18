package com.wipro.monolothic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TradeController {


    User user = new User();

    Map<String, Double> companies = new HashMap<String, Double>();


    Map<String, Trade> trades = new HashMap<String, Trade>();


    public TradeController() {

        companies.put("WIPRO", 298.45);

        companies.put("INFY", 949.95);

        companies.put("TCS", 2713.70);

        companies.put("TECHM", 485.85);

    }

    @RequestMapping(value = "/trade/do", method = RequestMethod.POST)
    @ResponseBody
    public String tradeDo(@ModelAttribute("ticker") String ticker, @ModelAttribute("qty") int qty, HttpServletRequest request) {

        Double price = companies.get(ticker);

        Trade t = new Trade(ticker, price, qty);

        double total = price * qty;

        t.setTotalCost(total);


        trades.put(ticker, t);

        user = (User) request.getSession().getAttribute("user");


        double bal = user.getBlanace() - total;

        user.setBalance(bal);


        return "<html><body bgcolor='coral'>Traded Successfully " + user.getUserId() + " your balance now is:: " + user.getBlanace() + " <BR><a href='http://localhost:7070/index.html'>Exit</a><BR><a href='http://localhost:7070/Trade.html'>Trade Again</a><br>" + "</body></html>";


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
