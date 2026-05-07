package dev.alucard.gui;

import dev.alucard.DatabaseCRUD;
import dev.alucard.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ResultsWindow extends JFrame implements ActionListener {

    private final JTextField teacherIDField;
    private final JLabel teacherIDLabel;
    private final JTextField firstNameField;
    private final JLabel firstNameLabel;
    private final JTextField lastNameField;
    private final JLabel lastNameLabel;
    private final JTextField availabilityField;
    private final JLabel availabilityLabel;
    private final JButton updateButton;
    private final JButton deleteButton;
    private final JButton coursesButton;
    private final JButton firstButton;
    private final JButton nextButton;
    private final JButton previousButton;
    private final JButton lastButton;
    private DatabaseCRUD crud;
    private int i = 0;
    private List<Teacher> teachers;

    public ResultsWindow(MainMenuWindow mainMenu, DatabaseCRUD crud, List<Teacher> teachers) {

        this.crud = crud;
        this.teachers = teachers;
        this.setTitle("Results Form");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(420,270));
        panel.setLayout(null);

        teacherIDLabel = new JLabel("Teacher ID ");
        teacherIDLabel.setBounds(40, 20, 80, 30);
        teacherIDField = new JTextField();
        teacherIDField.setBounds(120, 20, 140, 30);
        teacherIDField.setEditable(false);
        firstNameLabel = new JLabel("First Name: ");
        firstNameLabel.setBounds(40, 70, 80, 30);
        firstNameField = new JTextField();
        firstNameField.setBounds(120, 70, 140, 30);
        lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(40, 120, 80, 30);
        lastNameField = new JTextField();
        lastNameField.setBounds(120, 120, 140, 30);
        availabilityLabel = new JLabel("Availability: ");
        availabilityLabel.setBounds(40, 170, 80, 30);
        availabilityField = new JTextField();
        availabilityField.setBounds(120, 170, 140, 30);
        updateButton = new JButton("Update");
        updateButton.setBounds(40, 220, 100, 30);
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(160, 220, 100, 30);
        coursesButton = new JButton("Courses");
        coursesButton.setBounds(280, 220, 100, 30);
        firstButton = new JButton("First");
        firstButton.setBounds(280, 20, 100, 30);
        nextButton = new JButton("Next");
        nextButton.setBounds(280, 70, 100, 30);
        previousButton = new JButton("Previous");
        previousButton.setBounds(280, 120, 100, 30);
        lastButton = new JButton("Last");
        lastButton.setBounds(280, 170, 100, 30);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        coursesButton.addActionListener(this);
        firstButton.addActionListener(this);
        nextButton.addActionListener(this);
        previousButton.addActionListener(this);
        lastButton.addActionListener(this);

        if (teachers.size() > 0) {
            teacherIDField.setText(String.valueOf(teachers.get(0).getTeacherId()));
            firstNameField.setText(teachers.get(0).getFirstName());
            lastNameField.setText(teachers.get(0).getLastName());
            availabilityField.setText(String.valueOf(teachers.get(0).getHoursAvailablePerWeek()));
        }

        panel.add(teacherIDLabel);
        panel.add(teacherIDField);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(availabilityLabel);
        panel.add(availabilityField);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(coursesButton);
        panel.add(firstButton);
        panel.add(nextButton);
        panel.add(previousButton);
        panel.add(lastButton);

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
        if(e.getSource() == updateButton) {
            crud.updateTeacherData(firstNameField.getText(),lastNameField.getText(),
                    Integer.parseInt(availabilityField.getText()),Integer.parseInt(teacherIDField.getText()));
            dispose();
        }
        if(e.getSource() == deleteButton) {
            crud.deleteTeacher(Integer.parseInt(teacherIDField.getText()));
            dispose();
        }
        if(e.getSource() == coursesButton) {

        }
        if(e.getSource() == firstButton) {
            i = 0;
            navigateTeacher();
        }
        if(e.getSource() == nextButton) {
            if (i < teachers.size()-1) {
                i = i + 1;
                navigateTeacher();
            }
        }
        if(e.getSource() == previousButton) {
            if (i > 0){
                i = i - 1;
                navigateTeacher();
            }
        }
        if(e.getSource() == lastButton) {
            i = teachers.size() - 1;
            navigateTeacher();
        }
    }

    public void navigateTeacher () {
        teacherIDField.setText(String.valueOf(teachers.get(i).getTeacherId()));
        firstNameField.setText(teachers.get(i).getFirstName());
        lastNameField.setText(teachers.get(i).getLastName());
        availabilityField.setText(String.valueOf(teachers.get(i).getHoursAvailablePerWeek()));
    }
}

