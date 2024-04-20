package thundersharp.thinkfinity.dryer.boot.models;

public class UserAuthData {
    public String name;
    public int role;
    public String user_id;
    public String email;

    public UserAuthData(String name, int role, String user_id, String email) {
        this.name = name;
        this.role = role;
        this.user_id = user_id;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserAuthData{" +
                "name='" + name + '\'' +
                ", role=" + role +
                ", user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

