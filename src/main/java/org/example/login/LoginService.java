package org.example.login;
import org.example.customexceptions.InvalidCredentials;

import java.util.Map;

 public class LoginService{
     private User currentUser;
     private static final Map<String, User> userDataBase = InMemoryUserRepository.getDatabase().getUserDataBaseValues();

     //TODO logic behind accessing logged user
     User getCurrentUser() {
         return currentUser;
     }

      User returnLoggedUser(Map<String, String> params)  {
         for (Map.Entry<String, User> entry : userDataBase.entrySet()) {
             if (entry.getValue().username().equals(params.get("username"))) {
                 if (entry.getValue().password().equals(params.get("password"))) {
                     currentUser = entry.getValue();
                     return currentUser;
                 }
             }
         }
         throw new InvalidCredentials();
     }

}

