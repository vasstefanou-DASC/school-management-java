package dev.alucard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCRUD {

    private final Connection connection;

    private static final String SEARCH_COURSES_BY_TEACHERID = """
            SELECT * FROM school.courses WHERE teacher_id = ?""";

    private static final String REMOVE_TEACHER_FROM_COURSE = """
            UPDATE school.courses SET teacher_id = NULL WHERE course_id = ?""";

    private static final String ADD_TEACHER_TO_COURSE = """
            UPDATE school.courses SET teacher_id = ? WHERE course_id = ?""";

    private static final String DELETE_TEACHER = """
            DELETE FROM school.teachers WHERE teacher_id = ?""";

    private static final String SEARCH_TEACHER_BY_LASTNAME = """
            SELECT * FROM school.teachers WHERE lastname LIKE ?""";

    private static final String UPDATE_TEACHER = """
            UPDATE school.teachers SET firstname = ?, lastname = ?,
            available = ? WHERE teacher_id = ?""";
    private static final String UPDATE_AVAILABILITY = """
            UPDATE school.teachers SET available = ? WHERE teacher_id = ?""";

    private static final String INSERT_TEACHER = """
            INSERT INTO school.teachers (firstname,lastname,available)
            VALUES (?,?,?)""";

    private static final String INSERT_COURSE = """
            INSERT INTO school.courses (coursetitle,semester,hours)
            VALUES (?,?,?)""";

    private static final String[] COURSES = {"Introduction to Programming,1,4","Systems Analysis,1,3",
            "Mathematics I,1,3","Object-Oriented Programming,2,4","Databases I,2,4","Human-Computer Interaction,2,3",
            "Application Development,3,4","Introduction to Web development,3,4","Digital Systems and Computer Architecture,3,4",
            "Computer Networks and Security,4,4","Databases II,4,4","Data Structures and Algorithms,4,4","Software Technology,5,4",
            "Operating Systems and Network Programming,5,4","Number Theory and Cryptography,5,4","Discrete Mathematics,5,3",
            "Advanced Programming,6,4","Advanced Databases,6,4"};

    public DatabaseCRUD(Connection connection) {
        this.connection = connection;
    }

    public void addCourses() throws SQLException {
        int rows = 0;
        try (PreparedStatement psCourse = connection.prepareStatement(INSERT_COURSE);){
            for (String course : COURSES){
                String[] parts = course.split(",");
                psCourse.setString(1,parts[0]);
                psCourse.setInt(2,Integer.parseInt(parts[1]));
                psCourse.setInt(3,Integer.parseInt(parts[2]));
                rows += psCourse.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong while inserting data");
            e.printStackTrace();
        }
        System.out.println("Rows updated: " + rows);
    }

    public int addTeacher(String firstName, String lastName , int availability) {
        int teacherId = -1;
        try (PreparedStatement psTeacher = connection.prepareStatement(INSERT_TEACHER,
                Statement.RETURN_GENERATED_KEYS)) {
            if (firstName != null && !firstName.isBlank()) {
                psTeacher.setString(1,
                        firstName.substring(0,1).toUpperCase()+firstName.substring(1).toLowerCase());
            } else {
                System.out.println("Null values or blank names are not accepted.");
                return teacherId;
            }
            if (lastName != null && !lastName.isBlank()) {
                psTeacher.setString(2,
                        lastName.substring(0,1).toUpperCase()+lastName.substring(1).toLowerCase());
            } else {
                System.out.println("Null values or blank names are not accepted.");
                return teacherId;
            }
            if (availability > 0 && availability <= 16) {
                psTeacher.setInt(3,availability);
            } else {
                System.out.println("Work hours must be between 1-16 hours.");
                return teacherId;
            }
            psTeacher.executeUpdate();
            ResultSet rs = psTeacher.getGeneratedKeys();
            if (rs.next()) {
                teacherId = rs.getInt(1);
                System.out.println("Teacher ID: " + teacherId);
            }
        } catch (SQLException e) {
            System.out.println("Unsuccessful insertion");
            e.printStackTrace();
        }
        return teacherId;
    }

    public List<Teacher> showTeachers(String lastName) {
        List<Teacher> teachers = new ArrayList<>();
        try (PreparedStatement psSearch = connection.prepareStatement(SEARCH_TEACHER_BY_LASTNAME)) {
            psSearch.setString(1,lastName +"%");
            ResultSet rs = psSearch.executeQuery();
            while (rs.next()) {
                teachers.add(new Teacher(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }
        } catch (SQLException e) {
            System.out.println("Unsuccessful Search");
            e.printStackTrace();
        }
        return teachers;
    }

    public void updateTeacherData (String firstName,String lastName,int availability,int teacherId) {
        try (PreparedStatement psUpdate = connection.prepareStatement(UPDATE_TEACHER)) {
            psUpdate.setString(1,firstName);
            psUpdate.setString(2,lastName);
            psUpdate.setInt(3,availability);
            psUpdate.setInt(4,teacherId);
            int rows = psUpdate.executeUpdate();
            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            System.out.println("Unsuccessful update");
            e.printStackTrace();
        }
    }

    public void updateTeacherData (int availability,int teacherId) {
        try (PreparedStatement psUpdate = connection.prepareStatement(UPDATE_TEACHER)) {
            psUpdate.setInt(1,availability);
            psUpdate.setInt(2,teacherId);
            int rows = psUpdate.executeUpdate();
            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            System.out.println("Unsuccessful update");
            e.printStackTrace();
        }
    }

    public void deleteTeacher (int teacherId) {
        try (PreparedStatement psDelete = connection.prepareStatement(DELETE_TEACHER)) {
            psDelete.setInt(1,teacherId);
            int rows = psDelete.executeUpdate();
            System.out.println("Deleted rows: " + rows);
        } catch (SQLException e) {
            System.out.println("Unsuccessful deletion");
            e.printStackTrace();
        }
    }

    public List<Course> showCourses (int teacherId) {
        List<Course> courses = new ArrayList<>();
        try (PreparedStatement psCourses = connection.prepareStatement(SEARCH_COURSES_BY_TEACHERID)) {
            psCourses.setInt(1,teacherId);
            ResultSet rs = psCourses.executeQuery();
            while (rs.next()) {
                courses.add(new Course(rs.getInt(1),rs.getString(2),
                        rs.getInt(3),rs.getInt(4),rs.getInt(5)));
            }
        } catch (SQLException e) {
            System.out.println("Unsuccessful course search");
            e.printStackTrace();
        }
        return courses;
    }

    public void removeTeacherFromCourse (String firstName,String lastName,int availability,int teacherId,int hoursPerWeek,int courseId) {
        try (PreparedStatement psRemove = connection.prepareStatement(REMOVE_TEACHER_FROM_COURSE)) {
            updateTeacherData(firstName,lastName,availability+hoursPerWeek,teacherId);
            psRemove.setInt(1,courseId);
            int rows = psRemove.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            System.out.println("Unsuccessful removal");
            e.printStackTrace();
        }
    }

    public void addTeacherToCourse (String firstName,String lastName,int availability,int teacherId,int hoursPerWeek,int courseId)
    throws SQLException{
        if (availability-hoursPerWeek < 0){
            System.out.println("Teacher does not have enough available hours");
            return;
        }
        connection.setAutoCommit(false);
        try (PreparedStatement psAdd = connection.prepareStatement(ADD_TEACHER_TO_COURSE)) {
            updateTeacherData(availability-hoursPerWeek,teacherId);
            psAdd.setInt(1,teacherId);
            psAdd.setInt(2,courseId);
            psAdd.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Unsuccessful addition");
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }






}
