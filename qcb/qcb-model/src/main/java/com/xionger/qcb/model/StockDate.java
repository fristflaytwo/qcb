package com.xionger.qcb.model;

public class StockDate extends BaseEntity {

	private static final long serialVersionUID = -4790717668754004100L;

    private String stockDate;

    private String isTradeDate;

    private String isStartWeekDay;

    private String isEndWeekDay;

    public String getStockDate() {
        return stockDate;
    }

    public void setStockDate(String stockDate) {
        this.stockDate = stockDate == null ? null : stockDate.trim();
    }

    public String getIsTradeDate() {
        return isTradeDate;
    }

    public void setIsTradeDate(String isTradeDate) {
        this.isTradeDate = isTradeDate == null ? null : isTradeDate.trim();
    }

    public String getIsStartWeekDay() {
        return isStartWeekDay;
    }

    public void setIsStartWeekDay(String isStartWeekDay) {
        this.isStartWeekDay = isStartWeekDay == null ? null : isStartWeekDay.trim();
    }

    public String getIsEndWeekDay() {
        return isEndWeekDay;
    }

    public void setIsEndWeekDay(String isEndWeekDay) {
        this.isEndWeekDay = isEndWeekDay == null ? null : isEndWeekDay.trim();
    }
}