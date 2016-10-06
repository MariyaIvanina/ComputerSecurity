package by.bsu.var4.entity;

public class Resource {
    private int resourceId;
    private String path;

    public Resource() {
        super();
    }

    public Resource(String path) {
        this.path = path;
    }

    public Resource(int resourceId, String path) {
        this.resourceId = resourceId;
        this.path = path;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (resourceId != resource.resourceId) return false;
        return path != null ? path.equals(resource.path) : resource.path == null;

    }

    @Override
    public int hashCode() {
        int result = resourceId;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", path='" + path + '\'' +
                '}';
    }
}
