package org.example.login;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository {

    private static Map<String, User> userDataBase;
    private static InMemoryUserRepository instance;

    public Map<String, User> getUserDataBaseValues() {
        return userDataBase;
    }

    private InMemoryUserRepository(Map<String, User> userDataBase) {
        InMemoryUserRepository.userDataBase = userDataBase;
    }

    private static Map<String, User> usersBasicData() {
        HashMap<String, User> usersData = new HashMap<>();
        User user1 = new User("0001", "User", "User1", UserType.COMMON_USER);
        usersData.put(user1.userId(), user1);
        User user2 = new User("0002", "Admin", "Admin1", UserType.ADMIN);
        usersData.put(user2.userId(), user2);

        return usersData;
    }

    public static InMemoryUserRepository getDatabase() {
        if (instance == null) {
            instance = new InMemoryUserRepository(usersBasicData());
        }
        return instance;
    }


}
