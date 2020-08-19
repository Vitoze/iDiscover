package com.example.vitib.idiscoverqrcodescanner;

public class User {
    private String Nome;
    private String Idade;
    private String Email;

    public User(String name, String age, String mail) {
        Nome = name;
        Idade = "Idade: " + age + " anos";
        Email = mail;

    }

    public String getUsername() {
        return Nome;
    }

    public void setUsername(String username) {
        this.Nome = username;
    }

    public String getUserage() {
        return Idade;
    }

    public void setUserage(String userage) {
        this.Idade = userage;
    }

    public String getUsermail() {
        return Email;
    }

    public void setUsermail(String usermail) {
        this.Email = usermail;
    }
}
