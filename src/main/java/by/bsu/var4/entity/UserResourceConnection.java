package by.bsu.var4.entity;


/**
 * Created by Asus on 08.10.2016.
 */
public class UserResourceConnection {
    private int userResourceConnectionId;
    private int userGroupId;
    private int resourceGroupId;
    private String userGroupName;
    private String resourceGroupName;
    private Boolean fullPermission;

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(int resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getResourceGroupName() {
        return resourceGroupName;
    }

    public void setResourceGroupName(String resourceGroupName) {
        this.resourceGroupName = resourceGroupName;
    }

    public Boolean getFullPermission() {
        return fullPermission;
    }

    public void setFullPermission(Boolean fullPermission) {
        this.fullPermission = fullPermission;
    }

    public int getUserResourceConnectionId() {
        return userResourceConnectionId;
    }

    public void setUserResourceConnectionId(int userResourceConnectionId) {
        this.userResourceConnectionId = userResourceConnectionId;
    }
}
