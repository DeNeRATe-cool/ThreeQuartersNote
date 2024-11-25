package database.user;

import java.util.List;

public interface IUser {

    /**
     * sign up
     * @param username username
     * @param password password
     * @param rePassword double check password
     * @return true if sign up successfully
     *         false if username duplicated
     * @throws IllegalArgumentException if parameters wrong or password not match
     */
    boolean signUp(String username, String password, String rePassword) throws IllegalArgumentException;

    /**
     * login if user exists and password matches
     * and remember the username and password if needed
     * @param username input username
     * @param password input password
     * @param rememberMe true if remember me for the later login action
     *                   false and clean the json file
     * @return true if action successfully
     * @throws IllegalArgumentException empty/null parameters
     */
    boolean login(String username, String password, boolean rememberMe) throws IllegalArgumentException;

    /**
     * get remember.json file content
     * @return null if nothing written
     *         list of String if exists
     *         [username, password]
     */
    List<String> checkIfRemember();

    /**
     * get avatar of given username
     * if wrong params, bytes of noAvatar.png will be returned
     * if successfully catch, the avatar will be stored locally
     * and can be visited by ./cache/image/{username}.png
     * @param username username
     * @return bytes of avatar
     */
    byte[] getAvatar(String username);

    /**
     * update new avatar of user
     * @param username username
     * @param path path of new avatar picture
     * @return true if update successfully
     *         false if user doesn't exist
     * @throws IllegalArgumentException illegal parameters
     */
    boolean updateAvatar(String username, String path) throws IllegalArgumentException;

}
