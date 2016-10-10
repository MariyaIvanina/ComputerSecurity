package by.bsu.var4.entity;

/**
 * Created by Asus on 09.10.2016.
 */
public class ResourceModel {
    private int resourceId;
    private String resourceName;
    private boolean fullPermission;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public boolean isFullPermission() {
        return fullPermission;
    }

    public void setFullPermission(boolean fullPermission) {
        this.fullPermission = fullPermission;
    }
}
