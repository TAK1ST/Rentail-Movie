
package main.models;

import base.Model;

public class Subscription extends Model {
    private int subscriptionType;
    private String startDate;
    private String endDate;


    public Subscription(String id, int subscriptionType, String startDate, String endDate) {
        super(id);
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
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
                        getSubscriptionType(),
                        getStartDate(),
                        getEndDate()
                };
    }
}
