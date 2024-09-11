package developer.anurag.movitter.movitter_api;

import developer.anurag.movitter.datatypes.User;

public class LoginSession {
    private static User loggedUser,userToCreateAccount;

    public static void setLoggedUser(User user){
        loggedUser=user;
    }
    public static User getLoggedUser() {return loggedUser;}

    public static void setUserToCreateAccount(User user){
        userToCreateAccount=user;
    }
    public static User getUserToCreateAccount() {
        return userToCreateAccount;
    }
}
