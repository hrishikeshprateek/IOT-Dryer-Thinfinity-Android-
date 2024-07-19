package thundersharp.thinkfinity.dryer.users.core.model;

import java.util.List;

public class UserProfileData {

    private String id;
    private boolean isActive;
    private String name;
    private String email;
    private String phone;
    private String photoUrl;
    private int roles;
    private String address;
    private List<String> managedDevices;
    private List<String> managedUsers;

    public UserProfileData(String id, boolean isActive, String name, String email, String phone, String photoUrl, int roles, String address, List<String> managedDevices, List<String> managedUsers) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.roles = roles;
        this.address = address;
        this.managedDevices = managedDevices;
        this.managedUsers = managedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getManagedDevices() {
        return managedDevices;
    }

    public void setManagedDevices(List<String> managedDevices) {
        this.managedDevices = managedDevices;
    }

    public List<String> getManagedUsers() {
        return managedUsers;
    }

    public void setManagedUsers(List<String> managedUsers) {
        this.managedUsers = managedUsers;
    }
}
