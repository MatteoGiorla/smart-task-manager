package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.User;

/**
 * Interface that behave as a Proxy to a database
 */
public interface UserHelper {

    /**
     * Recover the information from the user in the
     * database and return it.
     *
     * @return the User recovered from the database
     */
    User retrieveUserInformation();
}