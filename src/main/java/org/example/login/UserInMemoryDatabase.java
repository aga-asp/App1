package org.example.login;

import java.util.HashMap;
import java.util.Map;

public class UserInMemoryDatabase {

    private static Map<String, User> userDataBase;
    private static UserInMemoryDatabase instance;

    public Map<String, User> getUserDataBase() {
        return userDataBase;
    }

    private UserInMemoryDatabase(Map<String, User> userDataBase) {
        UserInMemoryDatabase.userDataBase = userDataBase;
    }

    private static Map<String, User> usersBasicData() {
        HashMap<String, User> usersData = new HashMap<>();
        User user1 = new User("0001", "User", "User1", UserType.COMMON_USER);
        usersData.put(user1.getUserId(), user1);
        User user2 = new User("0002", "Admin", "Admin1", UserType.ADMIN);
        usersData.put(user2.getUserId(), user2);

        return usersData;
    }

    public static UserInMemoryDatabase createSingleton() {
        if (instance == null) {
            instance = new UserInMemoryDatabase(usersBasicData());
        }
        return instance;
    }


}
