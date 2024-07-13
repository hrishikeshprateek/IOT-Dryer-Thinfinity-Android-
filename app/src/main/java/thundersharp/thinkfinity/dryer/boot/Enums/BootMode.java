package thundersharp.thinkfinity.dryer.boot.Enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BootMode {
    int BOOT_FOR_DEVICE_CHANGE = 0;
    int BOOT_FOR_PASSWORD_LESS_AUTH = 1;
}
