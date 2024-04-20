package thundersharp.thinkfinity.dryer.boot.interfaces;

import thundersharp.thinkfinity.dryer.boot.models.LoginResponse;

public interface OnAuthEvent {
    void OnLoginSuccess(LoginResponse loginResponse);
    void OnLoginFailure(Exception e);
}
