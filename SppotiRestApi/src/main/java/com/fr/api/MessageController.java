package com.fr.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */
@RestController
@RequestMapping("/messages")
class MessageController {

   /* private final MessageControllerService messageControllerService;

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    @Autowired
     MessageController(MessageControllerService messageControllerService) {
        this.messageControllerService = messageControllerService;
    }

    @RequestMapping(value = "/sent/{bottomMajId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     ResponseEntity<MessageRequestDTO> getSentMessages(@PathVariable("bottomMajId") int bottomMajId,
                                                             HttpServletRequest request) {

        MessageRequestDTO response = new MessageRequestDTO();

        // page number is not valid
        if (bottomMajId < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        List<MessageEntity> sentMessages = messageControllerService.getSentUserMessages(userId, bottomMajId);

        if (sentMessages.isEmpty()) {
            return new ResponseEntity<MessageRequestDTO>(HttpStatus.NO_CONTENT);
        }

        response.setSentMessages(sentMessages);

        LOGGER.info("UserDTO DATA has been returned ");
        return new ResponseEntity<MessageRequestDTO>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/received/{bottomMajId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     ResponseEntity<MessageRequestDTO> getReceivedMessages(@PathVariable("bottomMajId") int bottomMajId,
                                                                 HttpServletRequest request) {

        MessageRequestDTO response = new MessageRequestDTO();

        // page number is not valid
        if (bottomMajId < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        List<MessageEntity> receivedMessages = messageControllerService.getReceivedUserMessages(userId, bottomMajId);

        response.setReceivedMessages(receivedMessages);

        if (receivedMessages.isEmpty()) {
            return new ResponseEntity<MessageRequestDTO>(response, HttpStatus.NO_CONTENT);
        }

        LOGGER.info("UserDTO DATA has been returned ");
        return new ResponseEntity<MessageRequestDTO>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     ResponseEntity<MessageEntity> addPost(@RequestBody MessageRequestDTO newMessage, UriComponentsBuilder ucBuilder,
                                                 HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = messageControllerService.getUserById(userId);
        LOGGER.info("LOGGED MessageEntity for UserDTO: => " + userId);

        MessageEntity newMsg = null;

        if (newMessage.getMsg() == null) {
            LOGGER.info("ADD: MessageEntity content is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        newMsg = new MessageEntity(newMessage.getMsg());
        newMsg.setUserMessage(user);

        if (newMessage.getReceivedId() == null) {
            LOGGER.info("ADD: MessageEntity received id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        newMsg.setReceiver_id(newMessage.getReceivedId());

        if (messageControllerService.saveMessage(newMsg)) {
            LOGGER.info("ADD: MessageEntity has been saved: => " + newMsg);
            return new ResponseEntity<MessageEntity>(newMsg, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
     ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {

        // bad argument parameter
        if (id < 1) {
            LOGGER.info("DELETE: Bad message argument parameter ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // nothing to delete
        if (messageControllerService.findMessageById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if (messageControllerService.deteleMessageById(id)) {
            LOGGER.info("DELETE: MessageEntity with id (" + id + ") has been deleted: ");
            return new ResponseEntity<>(HttpStatus.OK);

        }
        // database problem
        LOGGER.info("DELETE: Database deleted problem !!");
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }*/
}
