package org.demofx;

/**
 * Data structure for comparing two reduced "process name-consumed memory" lists
 * now enough just one leftRed or rightRed
 * Leaving it for future updates
 */
public class ComparedInfo {

    private String currentProcessName;
    private Double currentProcessMemory;
    private boolean leftRed;

    private String importedProcessName;
    private Double importedProcessMemory;
    private boolean rightRed;

    public ComparedInfo() {
    }

    public String getCurrentProcessName() {
        return currentProcessName;
    }

    public void setCurrentProcessName(String currentProcessName) {
        this.currentProcessName = currentProcessName;
    }

    public Double getCurrentProcessMemory() {
        return currentProcessMemory;
    }

    public void setCurrentProcessMemory(Double currentProcessMemory) {
        this.currentProcessMemory = currentProcessMemory;
    }

    public boolean isLeftRed() {
        return leftRed;
    }

    public void setLeftRed(boolean leftRed) {
        this.leftRed = leftRed;
    }

    public String getImportedProcessName() {
        return importedProcessName;
    }

    public void setImportedProcessName(String importedProcessName) {
        this.importedProcessName = importedProcessName;
    }

    public Double getImportedProcessMemory() {
        return importedProcessMemory;
    }

    public void setImportedProcessMemory(Double importedProcessMemory) {
        this.importedProcessMemory = importedProcessMemory;
    }

    public boolean isRightRed() {
        return rightRed;
    }

    public void setRightRed(boolean rightRed) {
        this.rightRed = rightRed;
    }
}

