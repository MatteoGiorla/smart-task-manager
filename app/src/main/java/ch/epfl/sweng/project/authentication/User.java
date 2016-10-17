package ch.epfl.sweng.project.authentication;

/**
 * User Information given by Tequila
 */

public final class User {
    public final String sciper;
    public final String gaspar;
    public final String mail;
    public final String firstName;
    public final String lastName;

    public User(String sciper, String gaspar, String mail, String firstName, String lastName) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
