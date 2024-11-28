import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private List<Student> students;
    private List<Course> courses;
    private List<Grade> grades;

    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("学生信息管理系统");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        students = loadStudents();
        courses = loadCourses();
        grades = loadGrades();

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("学生信息", createStudentPanel());
        tabbedPane.addTab("课程信息", createCoursePanel());
        tabbedPane.addTab("成绩信息", createGradePanel());

        add(tabbedPane);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton deleteButton = new JButton("删除");
        JButton editButton = new JButton("编辑");
        JButton searchButton = new JButton("查询");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(searchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                if (students.stream().anyMatch(student -> student.getStudentId().equals(studentId))) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号已存在");
                    return;
                }

                String name = JOptionPane.showInputDialog("请输入姓名:");
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "姓名不能为空");
                    return;
                }

                String gender = JOptionPane.showInputDialog("请输入性别:");
                if (gender == null || gender.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "性别不能为空");
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(JOptionPane.showInputDialog("请输入年龄:"));
                    if (age <= 0) {
                        JOptionPane.showMessageDialog(MainFrame.this, "年龄必须为正整数");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "年龄必须是整数");
                    return;
                }

                String className = JOptionPane.showInputDialog("请输入班级:");
                if (className == null || className.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "班级不能为空");
                    return;
                }

                students.add(new Student(studentId, name, gender, age, className));
                saveStudents();
                updateStudentTextArea(textArea);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要删除的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                students.removeIf(student -> student.getStudentId().equals(studentId));
                saveStudents();
                updateStudentTextArea(textArea);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要编辑的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                for (Student student : students) {
                    if (student.getStudentId().equals(studentId)) {
                        String name = JOptionPane.showInputDialog("请输入新的姓名:", student.getName());
                        if (name == null || name.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(MainFrame.this, "姓名不能为空");
                            return;
                        }

                        String gender = JOptionPane.showInputDialog("请输入新的性别:", student.getGender());
                        if (gender == null || gender.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(MainFrame.this, "性别不能为空");
                            return;
                        }

                        int age;
                        try {
                            age = Integer.parseInt(JOptionPane.showInputDialog("请输入新的年龄:", student.getAge()));
                            if (age <= 0) {
                                JOptionPane.showMessageDialog(MainFrame.this, "年龄必须为正整数");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MainFrame.this, "年龄必须是整数");
                            return;
                        }

                        String className = JOptionPane.showInputDialog("请输入新的班级:", student.getClassName());
                        if (className == null || className.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(MainFrame.this, "班级不能为空");
                            return;
                        }

                        student.setName(name);
                        student.setGender(gender);
                        student.setAge(age);
                        student.setClassName(className);
                        saveStudents();
                        updateStudentTextArea(textArea);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该学生");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要查询的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                for (Student student : students) {
                    if (student.getStudentId().equals(studentId)) {
                        JOptionPane.showMessageDialog(MainFrame.this, student.toString());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该学生");
            }
        });

        updateStudentTextArea(textArea);
        return panel;
    }
    private JPanel createCoursePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton deleteButton = new JButton("删除");
        JButton editButton = new JButton("编辑");
        JButton searchButton = new JButton("查询");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(searchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = JOptionPane.showInputDialog("请输入课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                if (courses.stream().anyMatch(course -> course.getCourseId().equals(courseId))) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号已存在");
                    return;
                }

                String courseName = JOptionPane.showInputDialog("请输入课程名:");
                if (courseName == null || courseName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程名不能为空");
                    return;
                }

                int credits;
                try {
                    credits = Integer.parseInt(JOptionPane.showInputDialog("请输入学分:"));
                    if (credits <= 0) {
                        JOptionPane.showMessageDialog(MainFrame.this, "学分必须为正整数");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学分必须是整数");
                    return;
                }

                courses.add(new Course(courseId, courseName, credits));
                saveCourses();
                updateCourseTextArea(textArea);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = JOptionPane.showInputDialog("请输入要删除的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                courses.removeIf(course -> course.getCourseId().equals(courseId));
                saveCourses();
                updateCourseTextArea(textArea);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = JOptionPane.showInputDialog("请输入要编辑的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                for (Course course : courses) {
                    if (course.getCourseId().equals(courseId)) {
                        String courseName = JOptionPane.showInputDialog("请输入新的课程名:", course.getCourseName());
                        if (courseName == null || courseName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(MainFrame.this, "课程名不能为空");
                            return;
                        }

                        int credits;
                        try {
                            credits = Integer.parseInt(JOptionPane.showInputDialog("请输入新的学分:", course.getCredits()));
                            if (credits <= 0) {
                                JOptionPane.showMessageDialog(MainFrame.this, "学分必须为正整数");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MainFrame.this, "学分必须是整数");
                            return;
                        }

                        course.setCourseName(courseName);
                        course.setCredits(credits);
                        saveCourses();
                        updateCourseTextArea(textArea);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该课程");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = JOptionPane.showInputDialog("请输入要查询的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                for (Course course : courses) {
                    if (course.getCourseId().equals(courseId)) {
                        JOptionPane.showMessageDialog(MainFrame.this, course.toString());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该课程");
            }
        });

        updateCourseTextArea(textArea);
        return panel;
    }

    private JPanel createGradePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton deleteButton = new JButton("删除");
        JButton editButton = new JButton("编辑");
        JButton searchButton = new JButton("查询");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(searchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                Student student = findStudentById(studentId);
                if (student == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不存在");
                    return;
                }

                String courseId = JOptionPane.showInputDialog("请输入课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                Course course = findCourseById(courseId);
                if (course == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不存在");
                    return;
                }

                double score;
                try {
                    score = Double.parseDouble(JOptionPane.showInputDialog("请输入分数:"));
                    if (score < 0 || score > 100) {
                        JOptionPane.showMessageDialog(MainFrame.this, "分数必须在0到100之间");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "分数必须是浮点数");
                    return;
                }

                grades.add(new Grade(studentId, student.getName(), courseId, course.getCourseName(), score));
                saveGrades();
                updateGradeTextArea(textArea);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要删除的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                String courseId = JOptionPane.showInputDialog("请输入要删除的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                grades.removeIf(grade -> grade.getStudentId().equals(studentId) && grade.getCourseId().equals(courseId));
                saveGrades();
                updateGradeTextArea(textArea);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要编辑的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                String courseId = JOptionPane.showInputDialog("请输入要编辑的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                for (Grade grade : grades) {
                    if (grade.getStudentId().equals(studentId) && grade.getCourseId().equals(courseId)) {
                        double score;
                        try {
                            score = Double.parseDouble(JOptionPane.showInputDialog("请输入新的分数:", grade.getScore()));
                            if (score < 0 || score > 100) {
                                JOptionPane.showMessageDialog(MainFrame.this, "分数必须在0到100之间");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MainFrame.this, "分数必须是浮点数");
                            return;
                        }

                        grade.setScore(score);
                        saveGrades();
                        updateGradeTextArea(textArea);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该成绩");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("请输入要查询的学号:");
                if (studentId == null || studentId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "学号不能为空");
                    return;
                }
                String courseId = JOptionPane.showInputDialog("请输入要查询的课程号:");
                if (courseId == null || courseId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "课程号不能为空");
                    return;
                }
                for (Grade grade : grades) {
                    if (grade.getStudentId().equals(studentId) && grade.getCourseId().equals(courseId)) {
                        JOptionPane.showMessageDialog(MainFrame.this, grade.toString());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(MainFrame.this, "未找到该成绩");
            }
        });

        updateGradeTextArea(textArea);
        return panel;
    }

    private Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private Course findCourseById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    private void updateStudentTextArea(JTextArea textArea) {
        textArea.setText("");
        for (Student student : students) {
            textArea.append(student.toString() + "\n");
        }
    }

    private void updateCourseTextArea(JTextArea textArea) {
        textArea.setText("");
        for (Course course : courses) {
            textArea.append(course.toString() + "\n");
        }
    }

    private void updateGradeTextArea(JTextArea textArea) {
        textArea.setText("");
        for (Grade grade : grades) {
            textArea.append(grade.toString() + "\n");
        }
    }

    private List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }

    private List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("courses.dat"))) {
            courses = (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courses;
    }

    private List<Grade> loadGrades() {
        List<Grade> grades = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("grades.dat"))) {
            grades = (List<Grade>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return grades;
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCourses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courses.dat"))) {
            oos.writeObject(courses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGrades() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("grades.dat"))) {
            oos.writeObject(grades);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}