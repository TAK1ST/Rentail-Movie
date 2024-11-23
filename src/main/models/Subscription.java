
package main.models;

import base.Model;

public class Subscription extends Model {
    private int subscriptionType;
    private String startDate;
    private String endDate;

    public Subscription(int subscriptionId, int subscriptionType, String startDate, String endDate) {
        super(subscriptionId);
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSubscriptionId() {
        return getId();
    }

    public void setSubscriptionId(int subscriptionId) {
        setId(subscriptionId);
    }

    public int getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getSubscriptionId(),
                        getSubscriptionType(),
                        getStartDate(),
                        getEndDate()
                };
    }
}
