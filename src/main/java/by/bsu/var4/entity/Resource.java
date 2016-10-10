package by.bsu.var4.entity;

public class Resource {
    private int resourceId;
    private String projectName;
    private String projectInfo;

    public Resource() {
        super();
    }

    public Resource(String projectName, String projectInfo) {
        this.setProjectInfo(projectInfo);
        this.setProjectName(projectName);
    }

    public Resource(int resourceId, String projectName, String projectInfo) {
        this.resourceId = resourceId;
        this.setProjectName(projectName);
        this.setProjectInfo(projectInfo);
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (resourceId != resource.resourceId) return false;
        if(projectName.equals(resource.projectName)) return false;
        return projectInfo != null ? projectInfo.equals(resource.projectInfo) : resource.projectInfo == null;

    }

    @Override
    public int hashCode() {
        int result = resourceId;
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0) + (projectInfo != null ? projectInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", project name='" + projectName + '\'' +
                '}';
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }
}
