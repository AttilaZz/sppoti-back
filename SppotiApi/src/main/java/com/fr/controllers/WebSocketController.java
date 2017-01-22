/**
 *
 */
package com.fr.controllers;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fr.dto.demo.CalcInput;
import com.fr.dto.demo.Result;

/**
 * Created by: Wail DJENANE on Nov 13, 2016
 */

@Controller
public class WebSocketController {

    @MessageMapping("/add")
    @SendTo("/topic/showResult")
    public Result addNum(CalcInput input) throws Exception {
        Thread.sleep(1000);
        Result result = new Result(input.getNum1() + "+" + input.getNum2() + "=" + (input.getNum1() + input.getNum2()));
        return result;
    }

    @MessageMapping("/trade")
    @SendToUser("/queue/position-updates")
    public Result executeTrade(Message input, Principal principal) throws InterruptedException {
        // ...
        Thread.sleep(1000);
        Result result = new Result(input.getValue());
        System.out.println("PRINT RECEIVED: " + input.getValue());
        return result;
    }

    // @MessageExceptionHandler
    // @SendToUser(destinations = "/queue/errors", broadcast = false)
    // public ApplicationError handleException(MyBusinessException exception) {
    // // ...
    // return appError;
    // }

    @RequestMapping("/start")
    public String start() {
        return "start";
    }

    @MessageMapping("/search")
    @SendToUser // <- maps to "/user/queue/search"
    public String search(@Payload String xxx) {
        return "TEST1234";
    }

    /* Inner Class for Messaging */
    private static class Message {

        private String message;

        public String getValue() {
            return message;
        }

    }

}
