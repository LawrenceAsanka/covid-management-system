package util;

public class UserTM {
    private String user_id;
    private String first_name;
    private String contact_number;
    private String email;
    private String role;
    private String userName;
    private String password;
    private String location;

    public UserTM() {
    }

    public UserTM(String user_id, String first_name, String contact_number, String email, String role, String userName, String password, String location) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.contact_number = contact_number;
        this.email = email;
        this.role = role;
        this.userName = userName;
        this.password = password;
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", contact_number='" + contact_number + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
