
import java.util.*;

class VirtualClassroomManager {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClassroomManager classroomManager = new ClassroomManager();

        System.out.println("Welcome to the Virtual Classroom Manager!");

        while (true) {
            System.out.println("Enter a command:");
            String input = scanner.nextLine();
            String[] command = input.split(" ", 2);

            if (command.length == 0) continue;
            String action = command[0];
            String details = command.length > 1 ? command[1] : "";

            switch (action) {
                case "add_classroom":
                    classroomManager.addClassroom(details);
                    break;
                case "add_student":
                    classroomManager.addStudent(details);
                    break;
                case "schedule_assignment":
                    classroomManager.scheduleAssignment(details);
                    break;
                case "submit_assignment":
                    classroomManager.submitAssignment(details);
                    break;
                case "list_classrooms":
                    classroomManager.listClassrooms();
                    break;
                case "list_students":
                    classroomManager.listStudents(details);
                    break;
                case "exit":
                    System.out.println("Exiting Virtual Classroom Manager...");
                    return;
                default:
                    System.out.println("Unknown command. Try again.");
            }
        }
    }
}

class Classroom {
    private String name;
    private List<Student> students;
    private List<Assignment> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void scheduleAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}

class Student {
    private String id;

    public Student(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class Assignment {
    private String details;
    private boolean submitted;

    public Assignment(String details) {
        this.details = details;
        this.submitted = false;
    }

    public String getDetails() {
        return details;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void submit() {
        this.submitted = true;
    }
}

class ClassroomManager {
    private Map<String, Classroom> classrooms;

    public ClassroomManager() {
        classrooms = new HashMap<>();
    }

    public void addClassroom(String className) {
        if (classrooms.containsKey(className)) {
            System.out.println("Classroom " + className + " already exists.");
        } else {
            classrooms.put(className, new Classroom(className));
            System.out.println("Classroom " + className + " has been created.");
        }
    }

    public void addStudent(String details) {
        String[] parts = details.split(" ");
        if (parts.length != 2) {
            System.out.println("Invalid command format. Use: add_student <studentID> <className>");
            return;
        }
        String studentId = parts[0];
        String className = parts[1];

        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            Student student = new Student(studentId);
            classroom.addStudent(student);
            System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void scheduleAssignment(String details) {
        String[] parts = details.split(" ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid command format. Use: schedule_assignment <className> <assignmentDetails>");
            return;
        }
        String className = parts[0];
        String assignmentDetails = parts[1];

        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            Assignment assignment = new Assignment(assignmentDetails);
            classroom.scheduleAssignment(assignment);
            System.out.println("Assignment for " + className + " has been scheduled.");
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void submitAssignment(String details) {
        String[] parts = details.split(" ", 3);
        if (parts.length != 3) {
            System.out.println("Invalid command format. Use: submit_assignment <studentID> <className> <assignmentDetails>");
            return;
        }
        String studentId = parts[0];
        String className = parts[1];
        String assignmentDetails = parts[2];

        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            Optional<Student> studentOpt = classroom.getStudents().stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst();
            if (studentOpt.isPresent()) {
                Optional<Assignment> assignmentOpt = classroom.getAssignments().stream()
                        .filter(a -> a.getDetails().equals(assignmentDetails))
                        .findFirst();
                if (assignmentOpt.isPresent()) {
                    assignmentOpt.get().submit();
                    System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
                } else {
                    System.out.println("Assignment not found in " + className + ".");
                }
            } else {
                System.out.println("Student " + studentId + " not enrolled in " + className + ".");
            }
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
        } else {
            System.out.println("Available classrooms:");
            classrooms.keySet().forEach(System.out::println);
        }
    }

    public void listStudents(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            List<Student> students = classroom.getStudents();
            if (students.isEmpty()) {
                System.out.println("No students enrolled in " + className + ".");
            } else {
                System.out.println("Students in " + className + ":");
                students.forEach(student -> System.out.println(student.getId()));
            }
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }
}
