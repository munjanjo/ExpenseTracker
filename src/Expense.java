import java.sql.Date;

public class Expense {
    private int  id;
    private String description;
    private double amount;
    private String date;
    private String category;
    Expense(String description, double amount, String date, String category) {
        if(description != null){
            this.description = description;
        }
        this.amount = amount;
        this.date = date.toString();
        this.category = category;
    }
    Expense(int id, String description, double amount, String date, String category){
        this.id = id;
        if(description != null){
            this.description = description;
        }
        this.amount = amount;
        this.date = date.toString();
        this.category = category;

    }
    @Override
    public String toString(){
        return id + " | " + date + " | " + category + " | " + description + " | " + amount;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}




}
