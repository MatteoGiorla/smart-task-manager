package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.User;

public class LocalUserHelper implements UserHelper{
    @Override
    public User retrieveUserInformation() {
        return new User(User.DEFAULT_EMAIL);
    }
}
