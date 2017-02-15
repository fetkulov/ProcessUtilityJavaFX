package org.demofx;


public class OsProcess {
    private String imageName;
    private String pid;
    private Double memoryUsedKB;

    public OsProcess(String imageName, String pid, Double memoryUsedKB) {
        this.imageName = imageName;
        this.pid = pid;
        this.memoryUsedKB = memoryUsedKB;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Double getMemoryUsedKB() {
        return memoryUsedKB;
    }

    public void setMemoryUsedKB(Double memoryUsedKB) {
        this.memoryUsedKB = memoryUsedKB;
    }
}
