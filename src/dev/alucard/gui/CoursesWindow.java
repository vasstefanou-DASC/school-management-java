package dev.alucard.gui;

import dev.alucard.Course;
import dev.alucard.DatabaseCRUD;
import dev.alucard.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class CoursesWindow extends JFrame implements ActionListener {

    private DatabaseCRUD crud;
    private Teacher teacher;
    private DefaultTableModel assignedModel;
    private JTable assignedTable;
    private JScrollPane assignedScroll;
    private JPanel panel = new JPanel();
    private JTextField teacherFullNameField;
    private JLabel teacherInfoLabel;
    private JButton removeButton;
    private JButton addButton;
    private JDialog dialog;
    private JPanel dialogPanel;
    private JLabel courseTitleLabel;
    private JTextField courseTitleField;
    private JButton confirmButton;
    private List<Course> assignedCourses;



    private static class NotEditableTableModel extends DefaultTableModel {

        public NotEditableTableModel(String[] columns, int rows) {
            super(columns,rows);
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }


    private static final String[] COLUMNS = {"Title","Semester","Hours/Week"};

    public CoursesWindow(DatabaseCRUD crud, Teacher teacher) {
        this.crud = crud;
        this.teacher = teacher;
        this.setTitle("Courses Form");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        panel.setPreferredSize(new Dimension(400,360));
        panel.setLayout(null);

        teacherInfoLabel = new JLabel("Teacher Name: ");
        teacherInfoLabel.setBounds(40,20,120,30);
        teacherFullNameField = new JTextField();
        teacherFullNameField.setText(teacher.getFirstName()+" "+teacher.getLastName());
        teacherFullNameField.setEditable(false);
        teacherFullNameField.setFocusable(false);
        teacherFullNameField.setBounds(160,20,200,30);

        assignedModel = new NotEditableTableModel(COLUMNS,0);
        assignedTable = new JTable(assignedModel);
        assignedScroll = new JScrollPane(assignedTable);
        assignedScroll.setBounds(0,70,400,220);

        addButton = new JButton("Add");
        addButton.setBounds(40, 310, 100, 30);
        removeButton = new JButton("Remove");
        removeButton.setBounds(260, 310, 100, 30);
        addButton.addActionListener(this);
        removeButton.addActionListener(this);

        panel.add(teacherInfoLabel);
        panel.add(teacherFullNameField);
        panel.add(assignedScroll);
        panel.add(addButton);
        panel.add(removeButton);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);

        assignedModel.setRowCount(0);
        assignedCourses = crud.showCourses(teacher.getTeacherId());
        for (Course c: assignedCourses) {
            assignedModel.addRow(new Object[]{c.getCourseTitle(),c.getSemester(),c.getHoursPerWeek()});
        }
        this.setVisible(true);

    }

    private void optionAddCourseWindow() {
        dialog = new JDialog(this,"Add Course",true);
        dialogPanel = new JPanel();
        dialogPanel.setPreferredSize(new Dimension(300,120));
        dialogPanel.setLayout(null);
        dialog.setResizable(false);

        courseTitleLabel = new JLabel("Course Title: ");
        courseTitleLabel.setBounds(40, 20, 80, 30);
        courseTitleField = new JTextField();
        courseTitleField.setBounds(120, 20, 140, 30);
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(100, 70, 100, 30);
        confirmButton.addActionListener(event -> confirmButtonClicked());

        dialogPanel.add(courseTitleLabel);
        dialogPanel.add(courseTitleField);
        dialogPanel.add(confirmButton);
        dialog.add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void confirmButtonClicked() {
        String courseTitle = courseTitleField.getText().trim();
        if (courseTitle.isBlank()) {
            JOptionPane.showMessageDialog(dialog,"Please enter a course title","Empty field",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Course course = crud.findCourseByTitle(courseTitle);
        if (course == null) {
            JOptionPane.showMessageDialog(dialog,"No course found with this title","Not found",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (teacher.getHoursAvailablePerWeek() - course.getHoursPerWeek() < 0) {
            JOptionPane.showMessageDialog(dialog,"Not enough hours","Cannot assign course",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (course.getTeacherId() != 0) {
            int confirm = JOptionPane.showConfirmDialog(dialog,"Course already has a tutor, do you want to replace him?",
                    "Course has a tutor!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.NO_OPTION) {
                return;
            }
        }

        try {
            crud.addTeacherToCourse(teacher.getFirstName(),teacher.getLastName(), teacher.getHoursAvailablePerWeek(),
                    teacher.getTeacherId(),course.getHoursPerWeek(),course.getCourseId());
            teacher.setHoursAvailablePerWeek(teacher.getHoursAvailablePerWeek()-course.getHoursPerWeek());
            JOptionPane.showMessageDialog(dialog,"Successfully added course.");
            dialog.dispose();
            assignedModel.setRowCount(0);
            assignedCourses = crud.showCourses(teacher.getTeacherId());
            for (Course c: assignedCourses) {
                assignedModel.addRow(new Object[]{c.getCourseTitle(),c.getSemester(),c.getHoursPerWeek()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dialog,"Error adding Course" + e.getMessage(),
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeButtonClicked() {
        int row = assignedTable.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this,"Select a course to remove",
                    "A course has not been selected",JOptionPane.WARNING_MESSAGE);
            return;
        }
        Course course = assignedCourses.get(row);
        crud.removeTeacherFromCourse(teacher.getFirstName(),teacher.getLastName(),
                teacher.getHoursAvailablePerWeek(),teacher.getTeacherId(),
                course.getHoursPerWeek(), course.getCourseId());
        teacher.setHoursAvailablePerWeek(teacher.getHoursAvailablePerWeek()+course.getHoursPerWeek());
        JOptionPane.showMessageDialog(this,"Successfully removed course");
        assignedModel.setRowCount(0);
        assignedCourses = crud.showCourses(teacher.getTeacherId());
        for (Course c: assignedCourses) {
            assignedModel.addRow(new Object[]{c.getCourseTitle(),c.getSemester(),c.getHoursPerWeek()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            optionAddCourseWindow();
        }
        if (e.getSource() == removeButton) {
            removeButtonClicked();
        }
    }
}
