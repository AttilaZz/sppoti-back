/**
 *
 */
package com.fr.api;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fr.commons.dto.demo.CalcInput;
import com.fr.commons.dto.demo.Result;

/**
 * Created by: Wail DJENANE on Nov 13, 2016
 */

@Controller
class WebSocketController {

    @MessageMapping("/add")
    @SendTo("/topic/showResult")
    Result addNum(CalcInput input) throws Exception {
        Thread.sleep(1000);
        return new Result(input.getNum1() + "+" + input.getNum2() + "=" + (input.getNum1() + input.getNum2()));
    }

    @MessageMapping("/trade")
    @SendToUser("/queue/position-updates")
    Result executeTrade(Message input, Principal principal) throws InterruptedException {
        // ...
        Thread.sleep(1000);
        Result result = new Result(input.getValue());
        System.out.println("PRINT RECEIVED: " + input.getValue());
        return result;
    }

    // @MessageExceptionHandler
    // @SendToUser(destinations = "/queue/errors", broadcast = false)
    //  ApplicationError handleException(MyBusinessException exception) {
    // // ...
    // return appError;
    // }

    @RequestMapping("/start")
    String start() {
        return "start";
    }

    @MessageMapping("/search")
    @SendToUser
        // <- maps to "/user/queue/search"
    String search(@Payload String xxx) {
        return "TEST1234";
    }

    /* Inner Class for Messaging */
    private static class Message {

        private String message;

        String getValue() {
            return message;
        }

    }

}
