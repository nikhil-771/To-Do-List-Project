import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import javax.swing.*;


public class ToDoListGUI
{
    JFrame frame;
    JPanel mainPanel, taskDispalyPanel;
    JTextField textField;
    JButton showAllButton, completeButton, deleteButton, clearCompletedButton, addTaskButton, submit;
    ToDoListDAO dao_obj = new ToDoListDAO();
    ToDoListGUI(){
        initialUI();
    }


    private void initialUI()
    {
        // Frame
        frame = new JFrame("\uD83D\uDCDDTo Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 500));
        frame.setLayout(new BorderLayout());

        // Remove Icon
        frame.setIconImage(null);

        // Button Panel
        mainPanel = new JPanel(new GridLayout(1,5));
        completeButton = new JButton("Complete Task");
        deleteButton = new JButton("Delete Task");
        clearCompletedButton = new JButton("Clear Completed Task");
        addTaskButton = new JButton("Add task");
        showAllButton = new JButton("Show All Tasks");

        // Add Action Listner
        addTaskButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> completeTask());
        clearCompletedButton.addActionListener(e -> clearAllCompleted());
        showAllButton.addActionListener(e -> showAllTasks());

        // Remove focusable border
        completeButton.setFocusable(false);
        deleteButton.setFocusable(false);
        clearCompletedButton.setFocusable(false);
        addTaskButton.setFocusable(false);
        showAllButton.setFocusable(false);

        // Add buttons to the panel
        mainPanel.add(addTaskButton);
        mainPanel.add(completeButton);
        mainPanel.add(deleteButton);
        mainPanel.add(clearCompletedButton);
        mainPanel.add(showAllButton);


        // TextField
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(500, 300));
        submit = new JButton("Submit");

        frame.add(mainPanel, BorderLayout.NORTH);

        frame.setVisible(true);

    }

    private void addTask()
    {
        // New Frame
        JFrame frame = new JFrame("Enter Task");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // To close only this frame and not other
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(4,2,10,10));

        // Text Field for Task
        frame.add(new JLabel("Enter Task"));
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(250, 100));
        txt.setFont(new Font("Consolas", Font.PLAIN, 16));
        txt.setBackground(Color.WHITE);
        txt.setCaretColor(Color.BLACK);
        frame.add(txt);

        // Text Field for Due Date
        frame.add(new JLabel("Enter Date in format YY-MM-DD"));
        JTextField txtDate = new JTextField();
        txtDate.setBackground(Color.WHITE);
        txtDate.setFont(new Font("Consolas", Font.PLAIN, 20));
        txtDate.setCaretColor(Color.BLACK);
        frame.add(txtDate);

        // Button
        JButton button = new JButton("Submit");
        button.setFont(new Font("Consolas", Font.PLAIN, 15));
        ToDoListDAO dao_obj = new ToDoListDAO();

        button.addActionListener(e -> {
                    String task = txt.getText().trim();
                    String dueDate = txtDate.getText().trim();
                    if (!task.isEmpty() && !dueDate.isEmpty()){
                        String st = dao_obj.addTask("tasks", task, dueDate);
                        if (st.equals("Success"))
                        {
                            JOptionPane.showMessageDialog(frame, "Task Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, st, "Database Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "⚠\uFE0F Please Enter both task and date!", "Missing task or Due Date", JOptionPane.WARNING_MESSAGE);
                    }
                });

        // Enter for submit
        frame.getRootPane().setDefaultButton(button);

        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }

    private void showAllTasks()
    {
        JFrame frame = new JFrame("All tasks");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(600,600));
        frame.setLayout(new BorderLayout());
        ImageIcon icon = new ImageIcon("icon.png");
        frame.setIconImage(icon.getImage());

        String[] columnNames = {"\uD83C\uDD94ID", "\uD83D\uDCDDTask", "\uD83D\uDCC5Due Date", "\uD83D\uDCC5Added Date", "\uD83D\uDD67Added Time", "\uD83D\uDCC5Completion Date", "❓Status"};

        List<String[]> taskList = dao_obj.showTasks("tasks");
        String[][] data = taskList.toArray(new String[0][]);
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);



    }

    private void deleteTask()
    {
        ToDoListDAO dao_obj = new ToDoListDAO();

        JFrame frame = new JFrame("Delete Task");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(400, 500));
        frame.setLayout(new GridLayout(3,1,10,10));
        frame.add(new JLabel("Enter Task ID to Delete:"));

        JTextField txt = new JTextField();
        txt.setFont(new Font("Consolas", Font.PLAIN, 16));
        txt.setBackground(Color.WHITE);
        txt.setCaretColor(Color.BLACK);
        frame.add(txt);

        JButton button = new JButton("Submit");

        button.addActionListener(e ->{
            String st = txt.getText().trim();
            if(!st.isEmpty())
            {
                String result = dao_obj.deleteTask("tasks", Integer.parseInt(st));
                if(result.equals("Success"))
                {
                    JOptionPane.showMessageDialog(frame, "Task Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Not deleted! ID did not match any record!", "Id Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(frame, "⚠\uFE0F Please Enter Valid Task ID", "Missing task or Due Date", JOptionPane.WARNING_MESSAGE);
                return;
            }
        });
        // Enter for submit
        frame.getRootPane().setDefaultButton(button);

        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }

    private void completeTask()
    {
        JFrame frame = new JFrame("Enter Task ID");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(new Dimension(300, 200));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setFont(new Font("Consolas", Font.PLAIN, 16));
        JButton button = new JButton("Submit");
        button.setFocusable(false);
        button.addActionListener(e -> {
            String st = textField.getText();
            String result;
            if(st.isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Enter ID to mark Complete", "Invalid ID", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                try {
                    int taskId = Integer.parseInt(st);
                    result = dao_obj.markComplete("tasks", taskId);

                    if ("Success".equals(result)) {
                        JOptionPane.showMessageDialog(frame, "✅ Task marked as complete.", "Successful", JOptionPane.PLAIN_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, result, "Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid numeric ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(textField);
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(panel);

        // Enter for submit
        frame.getRootPane().setDefaultButton(button);

        frame.pack();
        frame.setVisible(true);

    }

    private void clearAllCompleted()
    {
        String res = dao_obj.clearCompletedTasks("tasks");
        if(res.equals("Success"))
        {
            JOptionPane.showMessageDialog(null, "ALL completed task deleted", "Success", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, res, "Failed", JOptionPane.WARNING_MESSAGE);
        }
    }



}
