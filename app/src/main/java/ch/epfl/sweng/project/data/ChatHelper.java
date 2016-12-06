package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.chat.Message;

public interface ChatHelper {

    void updateChat(Task task, Message newMessage);

    void retrieveMessages(String mail, Task task);

    void removeListener();
}
