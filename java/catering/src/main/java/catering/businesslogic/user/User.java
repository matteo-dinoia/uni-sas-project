package catering.businesslogic.user;

import javafx.collections.FXCollections;
import catering.persistence.PersistenceManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {

    private static Map<Integer, User> loadedUsers = FXCollections.observableHashMap();

    public enum Role {SERVIZIO, CUOCO, CHEF, ORGANIZZATORE}

    private int id;
    private String username;
    private final Set<Role> roles;

    public User() {
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }
    public boolean isCook() { return roles.contains(Role.CUOCO); }

    public String getUserName() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(username);
        if (!roles.isEmpty()) {
            result.append(": ");

            for (User.Role r : roles)
                result.append(r.toString()).append(" ");
        }
        return result.toString();
    }

    // STATIC METHODS FOR PERSISTENCE

    public static User loadUserById(int uid) {
        if (loadedUsers.containsKey(uid)) return loadedUsers.get(uid);

        User load = new User();
        String userQuery = "SELECT * FROM Users WHERE id='"+uid+"'";
        PersistenceManager.executeQuery(userQuery, (rs) -> {
            load.id = rs.getInt("id");
            load.username = rs.getString("username");
        });
        if (load.id > 0) {
            loadedUsers.put(load.id, load);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + load.id;
            PersistenceManager.executeQuery(roleQuery, (rs) -> {
                String abbr = rs.getString("role_id");
                User.Role role = getRoleFromAbbreviation(abbr.charAt(0));
                if(role != null) load.roles.add(role);
            });
        }
        return load;
    }

    public static User loadUser(String username) {
        User u = new User();
        String userQuery = "SELECT * FROM Users WHERE username='"+username+"'";
        PersistenceManager.executeQuery(userQuery, (rs) -> {
            u.id = rs.getInt("id");
            u.username = rs.getString("username");
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, (rs) -> {
                String abbr = rs.getString("role_id");
                User.Role role = getRoleFromAbbreviation(abbr.charAt(0));
                if(role != null) u.roles.add(role);
            });
        }
        return u;
    }

    private static User.Role getRoleFromAbbreviation(char abbr){
        switch (abbr) {
            case 'c':
                return User.Role.CUOCO;
            case 'h':
                return User.Role.CHEF;
            case 'o':
                return User.Role.ORGANIZZATORE;
            case 's':
                return User.Role.SERVIZIO;
            default:
                return null;
        }
    }
}
