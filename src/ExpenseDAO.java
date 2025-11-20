import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final String URL = "jdbc:sqlite:database.db";


    public void addExpense(Expense expense){
        String sql = "INSERT INTO expenses (description, amount, date, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, expense.getDescription());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDate());
            pstmt.setString(4, expense.getCategory());
            pstmt.executeUpdate();
            System.out.println("Expense added successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public List<Expense> getAllExpenses() {
        String sql = "SELECT * FROM expenses";
        List<Expense> expenses=new ArrayList<>();
        try (Connection conn=DriverManager.getConnection(URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()){
            while(rs.next()){
                int id=rs.getInt("id");
                String description=rs.getString("description");
                double amount=rs.getDouble("amount");
                String date=rs.getString("date");
                String category=rs.getString("category");
                expenses.add(new Expense(id,description,amount,date,category));

            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return expenses;
    }
    public void deleteExpenseById(int id){
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM expenses WHERE id = ?");
        ){
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if(rows>0){
                System.out.println("Expense deleted successfully");
            }
            else {
                System.out.println("Expense not deleted successfully");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }


    }
    public double getTotalAmountSpent(){
        String sql = "SELECT SUM(amount) AS total FROM expenses";
        double sum=0.0;
        try(Connection conn= DriverManager.getConnection(URL);
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();){
            if(rs.next()){
                sum=rs.getDouble("total");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return sum;
    }
    public void updateExpense(Expense expense){
        String sql="UPDATE expenses SET description=?, amount=?, date=?, category=? WHERE id=?";
        try(Connection conn= DriverManager.getConnection(URL);
        PreparedStatement pstmt= conn.prepareStatement(sql);){
            pstmt.setString(1, expense.getDescription());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDate());
            pstmt.setString(4, expense.getCategory());
            pstmt.setInt(5, expense.getId());
            pstmt.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

