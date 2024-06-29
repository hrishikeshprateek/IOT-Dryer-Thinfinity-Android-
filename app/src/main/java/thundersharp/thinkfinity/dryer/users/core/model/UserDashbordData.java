package thundersharp.thinkfinity.dryer.users.core.model;

public class UserDashbordData {
    private int yourDeviceCount;
    private int privateRecipesCount;
    private int publicRecipeCount;
    private int jobSheetCount;
    private int activeDeviceCount;
    private int inactiveDeviceCount;

    public UserDashbordData() {
    }

    public UserDashbordData(int yourDeviceCount, int privateRecipesCount, int publicRecipeCount, int jobSheetCount, int activeDeviceCount, int inactiveDeviceCount) {
        this.yourDeviceCount = yourDeviceCount;
        this.privateRecipesCount = privateRecipesCount;
        this.publicRecipeCount = publicRecipeCount;
        this.jobSheetCount = jobSheetCount;
        this.activeDeviceCount = activeDeviceCount;
        this.inactiveDeviceCount = inactiveDeviceCount;
    }

    // Getters and Setters
    public int getYourDeviceCount() {
        return yourDeviceCount;
    }

    public void setYourDeviceCount(int yourDeviceCount) {
        this.yourDeviceCount = yourDeviceCount;
    }

    public int getPrivateRecipesCount() {
        return privateRecipesCount;
    }

    public void setPrivateRecipesCount(int privateRecipesCount) {
        this.privateRecipesCount = privateRecipesCount;
    }

    public int getPublicRecipeCount() {
        return publicRecipeCount;
    }

    public void setPublicRecipeCount(int publicRecipeCount) {
        this.publicRecipeCount = publicRecipeCount;
    }

    public int getJobSheetCount() {
        return jobSheetCount;
    }

    public void setJobSheetCount(int jobSheetCount) {
        this.jobSheetCount = jobSheetCount;
    }

    public int getActiveDeviceCount() {
        return activeDeviceCount;
    }

    public void setActiveDeviceCount(int activeDeviceCount) {
        this.activeDeviceCount = activeDeviceCount;
    }

    public int getInactiveDeviceCount() {
        return inactiveDeviceCount;
    }

    public void setInactiveDeviceCount(int inactiveDeviceCount) {
        this.inactiveDeviceCount = inactiveDeviceCount;
    }

    @Override
    public String toString() {
        return "DeviceStatistics{" +
                "yourDeviceCount=" + yourDeviceCount +
                ", privateRecipesCount=" + privateRecipesCount +
                ", publicRecipeCount=" + publicRecipeCount +
                ", jobSheetCount=" + jobSheetCount +
                ", activeDeviceCount=" + activeDeviceCount +
                ", inactiveDeviceCount=" + inactiveDeviceCount +
                '}';
    }
}