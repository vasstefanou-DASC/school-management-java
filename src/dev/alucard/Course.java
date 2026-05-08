package dev.alucard;

public class Course {

    private int courseId;
    private String courseTitle;
    private int semester;
    private int hoursPerWeek;
    private int teacherId;

    public Course(int courseId,String courseTitle, int semester, int hoursPerWeek, int teacherId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.semester = semester;
        this.hoursPerWeek = hoursPerWeek;
        this.teacherId = teacherId;
    }
    public String getCourseTitle() {
        return courseTitle;
    }
    public int getSemester() {
        return semester;
    }
    public int getHoursPerWeek() {
        return hoursPerWeek;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public int getCourseId() {
        return courseId;
    }
    @Override
    public String toString() {
        return "Course named %s id:%d runs in semester number %d for %d hours per week and is taught by teacher with id:%d."
                .formatted(courseTitle,courseId,semester,hoursPerWeek,teacherId);
    }
}
