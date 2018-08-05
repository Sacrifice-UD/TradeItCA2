package mapp.com.sg.myapplication;

public class Post {

    private String post_id;
    private String user_id;
    private String user_name;
    private String game;
    private String description;
    private String contact_email;

    public Post(String post_id, String user_id, String user_name, String game,
                String description, String contact_email) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.game = game;
        this.description = description;
        this.contact_email = contact_email;
    }

    public Post() {

    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "post_id='" + post_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", game='" + game + '\'' +
                ", description='" + description + '\'' +
                ", contact_email='" + contact_email + '\'' +
                '}';
    }
}
