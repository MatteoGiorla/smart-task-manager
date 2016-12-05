package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.chat.Message;

interface ChatHelper {

    void updateChat(Task task, Message newMessage);

    void retrieveMessages(User user, Task task);
}
