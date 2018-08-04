package mapp.com.sg.myapplication;

public class RegistrationDetails {
    public String RegistrationID;
    public String email;
    public String password;

    public RegistrationDetails() {

    }

    public RegistrationDetails(String RegistrationID, String email, String password) {
        this.RegistrationID = RegistrationID;
        this.email = email;
        this.password = password;
    }
}
