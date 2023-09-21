package org.example.login;
import org.example.custom_errors.BadUserNameOrPasswordException;

import java.util.Map;

 public class LoginService{

     private static User currentUser;
     private static final Map<String, User> userDataBase = UserInMemoryDatabase.createSingleton().getUserDataBase();

     public static User getCurrentUser() {
         return currentUser;
     }

     static User returnLoggedUser(Map<String, String> params) throws BadUserNameOrPasswordException {
         for (Map.Entry<String, User> entry : userDataBase.entrySet()) {
             if (entry.getValue().getUsername().equals(params.get("username"))) {
                 if (entry.getValue().getPassword().equals(params.get("password"))) {
                     currentUser = entry.getValue();
                     return currentUser;
                 }
             }
         }
         throw new BadUserNameOrPasswordException();
     }

}

