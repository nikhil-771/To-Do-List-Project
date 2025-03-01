import java.sql.*;

import java.util.List;
import java.util.ArrayList;

public class ToDoListDAO
{
    
    int totalTasksCreated = 0;
    int tasksDeleted = 0;
    int pendingTasks = 0;
    int completedTasks = 0;
    
    public String addTask(String tb_name, String st, String dueDate)
    {
        String query ;
        dueDate = "20" + dueDate;

        query = "INSERT INTO " +tb_name + "  (task, dueDate) VALUES (?, ?)";
        try(Connection con = DB_Connection.getConnection(); PreparedStatement pstm = con.prepareStatement(query) )
            {
                pstm.setString(1, st);
                pstm.setDate(2, java.sql.Date.valueOf(dueDate));
                int changes = pstm.executeUpdate();
                if(changes>0)
                {
                    totalTasksCreated++;
                    pendingTasks++;
                    return "Success";
                }
                else{
                    return "Failed to Insert due to unkown reason";
                }
            }
            catch(SQLException e){
                return "SQL Error: " + e.getMessage();
            }
    }

    public String deleteTask(String tb_name, int id)
    {
        String deleteQuery = "DELETE FROM "+ tb_name + " WHERE taskId = " + id;

        try(Connection con = DB_Connection.getConnection(); Statement stm = con.createStatement())
        {
            int changes = stm.executeUpdate(deleteQuery);
            if(changes>0){
                tasksDeleted++;
                return "Success";
            }
            else{
                return "❌ Deletion failed! Task ID not present.";
            }
        }
        catch(SQLException e)
        {
            return "❌ Deletion failed!" + e.getMessage();
        }
    }
    public List<String[]> showTasks(String tb_name)
    {
        String countQuery = "SELECT COUNT(*) FROM " + tb_name;
        String query = "SELECT * FROM " + tb_name;
        List<String[]> tasks= new ArrayList<String[]>();

        try(Connection con = DB_Connection.getConnection(); Statement stm = con.createStatement())
        {
            try(ResultSet rs = stm.executeQuery(countQuery))
            {
                if(rs.next() && rs.getInt(1) == 0)
                {
                    System.out.println("No tasks to display");
                    String[] message = {"No tasks to display"};
                    tasks.add(message);
                    return tasks;
                }
            }
            catch (SQLException e)
            {
                System.out.println("Could Not Display Tasks" + e.getMessage());
                String[] message = {"Could Not Display Tasks" + e.getMessage()};
                tasks.add(message);
                return tasks;
            }

            try(ResultSet rs = stm.executeQuery(query)){
                while(rs.next())
                {
                    String taskId = String.valueOf(rs.getInt("taskID"));
                    String taskName = String.valueOf(rs.getString("task"));
                    String dueDate = String.valueOf(rs.getDate("dueDate"));
                    String addedDate = String.valueOf(rs.getDate("addedDate"));
                    String addedTime = String.valueOf(rs.getTime("addedTime"));
                    String completionDate = (rs.getDate("completionDate") != null) ? rs.getDate("completionDate").toString() : "N/A";
                    String status = rs.getBoolean("status") ? "Completed":"Not Completed";

                    String[] row = {
                            taskId,
                            taskName,
                            dueDate,
                            addedDate,
                            addedTime,
                            completionDate,
                            status
                    };
                    tasks.add(row);

                    System.out.println("----------------------------------------------");
                    System.out.println("🆔 Task ID: " + taskId);
                    System.out.println("📝 Task: " + taskName);

                    if (status.equals("Completed"))
                    {
                        System.out.println("✅ Status: Completed");
                        System.out.println("\uD83D\uDCC6 Completion Date: " + (completionDate != null ? completionDate : "N/A"));
                    }
                    else {
                        System.out.println("❌ Status: Not Completed");
                        System.out.println("\uD83D\uDCC5 Due Date: " + dueDate);
                    }

                    System.out.println("\uD83D\uDCC5 Added Date: " + addedDate);
                    System.out.println("\uD83D\uDD67 Added Time: " + addedTime);
                }
                return tasks;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Unable to display tasks\n" + e.getMessage());
            String[] message = {"Could Not Display Tasks" + e.getMessage()};
            tasks.add(message);
            return tasks;
        }
    }


    public String markComplete(String tb_name, int id)
    {
        String query = "UPDATE " + tb_name + " SET STATUS = 1 WHERE taskID = " + id;

        try (Connection con = DB_Connection.getConnection(); Statement stm = con.createStatement())
        {
            int change = stm.executeUpdate(query);
            if (change == 0) {
                return "⚠️ Task ID not found.";
            } else {
                completedTasks++;
                return "Success";
            }
        } catch (SQLException e) {
            return ("❌ Failed to mark task as complete.\n" + e.getMessage());
        }
    }

    public String clearCompletedTasks(String tb_name)
    {
        String deleteQuery = "DELETE FROM " + tb_name + " WHERE status = 1";
        try(Connection con = DB_Connection.getConnection(); Statement stm = con.createStatement())
        {
            int changes = stm.executeUpdate(deleteQuery);
            if (changes!=0) {
                System.out.println("Deleted " + changes + " completed tasks");
                return "Success";
            }
            else {
                System.out.println("⛔Failed to delete any completed task ");
                return "⛔Failed to delete any completed task ";
            }
        }
        catch(SQLException e)
        {
            System.out.println("⛔Failed to delete\n" +e.getMessage());
            return "⛔Failed to delete\n" +e.getMessage();
        }  
    }

}
