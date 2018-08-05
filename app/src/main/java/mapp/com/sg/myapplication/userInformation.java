package mapp.com.sg.myapplication;

public class userInformation {
    public String name;
    public String age;

    public userInformation() {

    }

    public userInformation(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
