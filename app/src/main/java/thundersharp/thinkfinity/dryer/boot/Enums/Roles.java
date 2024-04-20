package thundersharp.thinkfinity.dryer.boot.Enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The {@code Roles} annotation defines integer constants representing different roles
 * within a system. These role constants can be used to annotate fields or methods
 * to provide semantic meaning to their associated values.
 * <p>
 * The annotation is marked with {@code @Retention(RetentionPolicy.RUNTIME)}
 * to ensure that the constants can be accessed and used reflectively at runtime.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     &#064;Roles.OEM
 *     private int userRole;
 *
 *     // Accessing role constants
 *     int oemRole = Roles.OEM;
 *     int managerRole = Roles.MANAGER;
 *     int userRole = Roles.USER;
 * </pre>
 * </p>
 * <p>
 * The constants defined in this annotation include:
 * <ul>
 *     <li>{@link #OEM} - Represents the OEM (Original Equipment Manufacturer) role.</li>
 *     <li>{@link #MANAGER} - Represents the Manager role.</li>
 *     <li>{@link #USER} - Represents the User role.</li>
 * </ul>
 * </p>
 * <p>
 * Use these constants to assign specific roles or check the role type in your application logic.
 * </p>
 * <p>
 * Note: Annotations in Java are not intended to be used as regular enums; they provide
 * a way to associate metadata with declarations.
 * </p>
 *
 * @see Retention
 * @see RetentionPolicy
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Roles {

    /**
     * Represents the OEM (Original Equipment Manufacturer) role.
     */
    int OEM = 0;

    /**
     * Represents the Manager role.
     */
    int MANAGER = 1;

    /**
     * Represents the User role.
     */
    int USER = 2;
}
