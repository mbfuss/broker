package org.example.broker.model;

public class PrimeTask {

    private String taskGuid;
    private final int startNumber;
    private final int endNumber;
    private int primeCount;

    public PrimeTask(String taskGuid, int startNumber, int endNumber, int primeCount) {
        this.taskGuid = taskGuid;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.primeCount = primeCount;
    }

    public void setTaskGuid(String taskGuid) {
        this.taskGuid = taskGuid;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public int getPrimeCount() {
        return primeCount;
    }

    public void setPrimeCount(int primeCount) {
        this.primeCount = primeCount;
    }
}
