package com.example.prototipoapp.home.ui.veiculo;

public class VeiculoItem {
    private int pk;
    private String name;
    private String placa;
    private int tipo;
    private String imageUrl;

    public VeiculoItem() {
    }

    public VeiculoItem(int pk, String name, String placa, int tipo, String imageUrl) {
        this.pk = pk;
        this.name = name;
        this.placa = placa;
        this.tipo = tipo;
        this.imageUrl = imageUrl;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
