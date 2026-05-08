package dev.alucard.gui;

import dev.alucard.DatabaseCRUD;
import dev.alucard.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class MainMenuWindow extends JFrame implements ActionListener {

    private final JPanel panel = new JPanel();
    private final JTextField lastNameField;
    private JLabel lastNameLabel;
    private JButton searchButton;
    private JButton insertButton;
    private Connection connection;
    private DatabaseCRUD crud;

    public MainMenuWindow() {

        addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent event) {
                String url = "jdbc:mysql://localhost:3306/school";
                String username = System.getenv("SQLUSERNAME");
                String password = System.getenv("SQLPASSWORD");
                try {
                    connection = DriverManager.getConnection(url, username, password);
                    crud = new DatabaseCRUD(connection);
                    System.out.println("Successful Connection.");
                } catch (SQLException e) {
                    System.out.println("Unsuccessful Connection.");
                    e.printStackTrace();
                }
            }
        });
        this.setTitle("Main Menu Form");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        panel.setPreferredSize(new Dimension(300, 120));
        panel.setLayout(null);

        lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(40, 20, 80, 30);
        lastNameField = new JTextField();
        lastNameField.setBounds(120, 20, 140, 30);
        searchButton = new JButton("Search");
        searchButton.setBounds(40, 70, 100, 30);
        insertButton = new JButton("Insert");
        insertButton.setBounds(160, 70, 100, 30);
        searchButton.addActionListener(this);
        insertButton.addActionListener(this);

        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(searchButton);
        panel.add(insertButton);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == insertButton) {
            this.setVisible(false);
            new InsertWindow(this,crud);
        }
        if (event.getSource() == searchButton) {
            this.setVisible(false);
            List<Teacher> teachers = crud.showTeachers(lastNameField.getText());
            new ResultsWindow(this,crud,teachers);
        }
    }

    public void reappearMainWindow() {
        this.setVisible(true);
    }
}