package thundersharp.thinkfinity.dryer.users.core.model;

public class SupportTicket {
    private String uid;
    private String name;
    private boolean isForOem;
    private boolean checkDeviceLogs;
    private String contact_no;
    private long ticketTime;
    private String deviceID;
    private String supportTittle;
    private String supportType;
    private String alternateContact;
    private String description;
    private String status;

    public SupportTicket(String uid, String name, boolean isForOem, boolean checkDeviceLogs, String contact_no,
                         long ticketTime, String deviceID, String supportTittle, String supportType,
                         String alternateContact, String description, String status) {
        this.uid = uid;
        this.name = name;
        this.isForOem = isForOem;
        this.checkDeviceLogs = checkDeviceLogs;
        this.contact_no = contact_no;
        this.ticketTime = ticketTime;
        this.deviceID = deviceID;
        this.supportTittle = supportTittle;
        this.supportType = supportType;
        this.alternateContact = alternateContact;
        this.description = description;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isForOem() {
        return isForOem;
    }

    public void setForOem(boolean forOem) {
        isForOem = forOem;
    }

    public boolean isCheckDeviceLogs() {
        return checkDeviceLogs;
    }

    public void setCheckDeviceLogs(boolean checkDeviceLogs) {
        this.checkDeviceLogs = checkDeviceLogs;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public long getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(long ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSupportTittle() {
        return supportTittle;
    }

    public void setSupportTittle(String supportTittle) {
        this.supportTittle = supportTittle;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getAlternateContact() {
        return alternateContact;
    }

    public void setAlternateContact(String alternateContact) {
        this.alternateContact = alternateContact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}