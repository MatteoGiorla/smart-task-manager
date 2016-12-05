package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;

interface ChatHelper {

    public void updateChat(Task task);

    public void retrieveMessages(User user, Task task);
}
