package com.vision.testmicroservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity(name = "reset_password_token")
public class ResetPasswordToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Column(nullable = false, unique = true)
    private String token;



    public ResetPasswordToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public ResetPasswordToken(){

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}