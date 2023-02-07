package com.example.prototipoapp.home.ui.funcionario;


public class User {
    private int pk;
    private String name;
    private String username;
    private String contato;
    private String cargo;
//    private String ImageUrl;

    public User() {}

    public User(int pk, String name, String username, String contato, String cargo) {
        this.pk = pk;
        this.name = name;
        this.username = username;
        this.contato = contato;
        this.cargo = cargo;

    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
