package student;

public record Student(String ID, String firstName, String lastName, String gender, double gpa, int level,
                      String address) {

    @Override
    public String toString() {
        return "student.Student [ID=" + ID + ", FirstName=" + firstName + ", LastName=" + lastName +
                ", Gender=" + gender + ", GPA=" + gpa + ", Level=" + level + ", Address=" + address + "]";
    }
}
