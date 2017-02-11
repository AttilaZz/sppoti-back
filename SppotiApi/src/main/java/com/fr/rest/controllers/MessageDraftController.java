/**
 *
 */
package com.fr.rest.controllers;

import com.fr.entities.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fr.aop.TraceAuthentification;
import com.fr.rest.service.MessageControllerService;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@RestController
@RequestMapping("/message/draft")
public class MessageDraftController {

    @Autowired
    private MessageControllerService messageControllerService;

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    // private static final String ATT_USER_ID = "USER_ID";

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> updateUser(@PathVariable("id") Long id, @RequestBody Message newMsg) {

        if (id > 0) {
            Message oldMsg = messageControllerService.findMessageById(id);
            if (oldMsg != null) {
                Message updated = new Message();
                // prepare data for update
                if (newMsg.getContent() != null) {
                    updated.setContent(newMsg.getContent());
                }
                if (newMsg.getObject() != null) {
                    updated.setObject(newMsg.getObject());
                }

                // if (messageControllerService.updateMessage(updated)) {
                // LOGGER.info("UPDATE: success");
                // return new ResponseEntity<Message>(updated, HttpStatus.OK);
                // }

            }
            return new ResponseEntity<Message>(HttpStatus.NO_CONTENT);
        }

        // BAD MESSAGE ID
        LOGGER.info("UPDATE: bad argument");
        return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
    }
}
