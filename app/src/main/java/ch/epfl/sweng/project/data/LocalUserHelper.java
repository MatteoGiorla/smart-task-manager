package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.User;

/**
 * Local proxy that behave as firebase but does not require
 * any internet connection.
 * Mostly use to mock firebase in order to run tests faster.
 */
public class LocalUserHelper implements UserHelper{

    @Override
    public User retrieveUserInformation() {
        return new User(User.DEFAULT_EMAIL);
    }
}
