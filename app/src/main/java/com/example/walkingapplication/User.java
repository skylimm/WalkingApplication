package com.example.walkingapplication;

public class User {
    private String email, name, image;
    private Double height, weight;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String image, Double height, Double weight) {
        this.email = email;

        //extract substring before @ symbol in email. "name@domain.com" -> "name"
        int atIndex = email.indexOf("@");
        this.name = (atIndex >= 0) ? email.substring(0, atIndex) : "";

        this.image = image;
        this.height = height;
        this.weight = weight;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public Double getHeight() { return height; }
    public Double getWeight() { return weight; }

}