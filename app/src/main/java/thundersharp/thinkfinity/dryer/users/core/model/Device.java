package thundersharp.thinkfinity.dryer.users.core.model;

public class Device {

    private String id;
    private String device_name;
    private String device_type;
    private String activatedBy;
    private long subscriptionActivatedOn;
    private long subscriptionActiveTill;
    private String assignedTo;
    private boolean enabled;

    public Device() {
    }

    public Device(String id, String device_name, String device_type, String activatedBy, long subscriptionActivatedOn, long subscriptionActiveTill, String assignedTo, boolean enabled) {
        this.id = id;
        this.device_name = device_name;
        this.device_type = device_type;
        this.activatedBy = activatedBy;
        this.subscriptionActivatedOn = subscriptionActivatedOn;
        this.subscriptionActiveTill = subscriptionActiveTill;
        this.assignedTo = assignedTo;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }

    public long getSubscriptionActivatedOn() {
        return subscriptionActivatedOn;
    }

    public void setSubscriptionActivatedOn(long subscriptionActivatedOn) {
        this.subscriptionActivatedOn = subscriptionActivatedOn;
    }

    public long getSubscriptionActiveTill() {
        return subscriptionActiveTill;
    }

    public void setSubscriptionActiveTill(long subscriptionActiveTill) {
        this.subscriptionActiveTill = subscriptionActiveTill;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
