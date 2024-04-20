package thundersharp.thinkfinity.dryer.boot.models;

public class LoginResponse {
    public String token;
    public String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
