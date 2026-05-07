package dev.alucard.gui;

import dev.alucard.DatabaseCRUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InsertWindow extends JFrame implements ActionListener {

    private JPanel panel;
    private JTextField firstNameField;
    private JLabel firstNameLabel;
    private JTextField lastNameField;
    private JLabel lastNameLabel;
    private JTextField availabilityField;
    private JLabel availabilityLabel;
    private JButton insertButton;
    private DatabaseCRUD crud;

    public InsertWindow(MainMenuWindow mainMenu,DatabaseCRUD crud) {

        this.crud = crud;
        this.setTitle("Insert Form");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(300,220));
        panel.setLayout(null);

        firstNameLabel = new JLabel("First Name: ");
        firstNameLabel.setBounds(40, 20, 80, 30);
        firstNameField = new JTextField();
        firstNameField.setBounds(120, 20, 140, 30);
        lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(40, 70, 80, 30);
        lastNameField = new JTextField();
        lastNameField.setBounds(120, 70, 140, 30);
        availabilityLabel = new JLabel("Availability: ");
        availabilityLabel.setBounds(40, 120, 80, 30);
        availabilityField = new JTextField();
        availabilityField.setBounds(120, 120, 140, 30);
        insertButton = new JButton("Insert");
        insertButton.setBounds(100, 170, 100, 30);
        insertButton.addActionListener(this);

        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(availabilityLabel);
        panel.add(availabilityField);
        panel.add(insertButton);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainMenu.reappearMainWindow();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertButton) {
            System.out.println("Button Clicked");
            int id = crud.addTeacher(firstNameField.getText(),
                    lastNameField.getText(),
                    Integer.parseInt(availabilityField.getText()));
            if (id == -1) {
                JOptionPane.showMessageDialog(this,"Insert failed!");
            } else {
                JOptionPane.showMessageDialog(this,"Teacher inserted with ID: " + id);
                dispose();
            }
        }
    }
}
