import java.util.*;
public class Log {
    private String type,category,note,currency;
    private double amount;
    private Date date;
    public Log(String type,Date date,double amount,String currency,String category,String note){
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.currency=currency;
        this.category = category;
        this.note = note;
    }
    public Log(String type,Date date,double amount,String currency,String category){
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
    }
    public Log(double amount, String category){
        this.amount = amount;
        this.category = category;
    }
    public String getType() { return type; }
    public Date getMyDate() { return date; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCategory() { return category; }
    public String getNote() { return note; }
    public void addAmount(double add) { this.amount+=add; }
}
