package mapp.com.sg.myapplication;

public class Post {

    private String post_id;
    private String user_id;
    private String image;
    private String title;
    private String description;
    private String trade;
    private String country;
    private String state_province;
    private String phone;
    private String contact_email;

    public Post(String post_id, String user_id, String image, String title, String description,
                String trade, String country, String state_province, String phone, String contact_email) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.trade = trade;
        this.country = country;
        this.state_province = state_province;
        this.phone = phone;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState_province() {
        return state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", trade='" + trade + '\'' +
                ", country='" + country + '\'' +
                ", state_province='" + state_province + '\'' +
                ", phone='" + phone + '\'' +
                ", contact_email='" + contact_email + '\'' +
                '}';
    }
}
