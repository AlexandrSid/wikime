package org.aleksid.wikime.kafka;

import org.aleksid.wikime.model.User;
import org.aleksid.wikime.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {
    private final UserService userService;

    private final SendKafkaMessageTask messageTask;

    private static final Logger logger = LogManager.getLogger(KafkaService.class);

    public KafkaService(UserService userService, SendKafkaMessageTask messageTask) {
        this.userService = userService;
        this.messageTask = messageTask;
    }


    public void registrationAnnouncement(User user) {
        String username = user.getUsername();
        Long id = userService.getUserByName(username).getId();
        try {
            messageTask.send(String.format("User %s has been registered and got id = %d", username, id));
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
