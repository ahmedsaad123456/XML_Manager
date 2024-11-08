public class Student {

    private final String ID;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final double gpa;
    private final int level;
    private final String address;

    // =================================================================================================================

    // Constructor to initialize Student fields
    public Student(String ID, String firstName, String lastName, String gender, double gpa, int level, String address) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.gpa = gpa;
        this.level = level;
        this.address = address;
    }


    // =================================================================================================================
    public String getID() { return ID; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public double getGpa() { return gpa; }
    public int getLevel() { return level; }
    public String getAddress() { return address; }

    // =================================================================================================================

    @Override
    public String toString() {
        return "Student [ID=" + ID + ", FirstName=" + firstName + ", LastName=" + lastName +
                ", Gender=" + gender + ", GPA=" + gpa + ", Level=" + level + ", Address=" + address + "]";
    }
}
