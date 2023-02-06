package com.example.prototipoapp.home.ui.ordem;

import java.sql.Time;
import java.util.Date;

public class Ordem {
    private int pk;
    private String solicitante;
    private String descricao;
    private String origem;
    private String destino;
    private String data_solicitacao;
//    private Date data_solicitado;
//    private Time horario_requirido;
//    private String instituicao;
    private String status;
//    private Date data_marcada;
//    private Time horario_marcado;
//    private String motorista;
//    private String veiculo;
    private int carga;

    public Ordem(){}

    public Ordem(int pk, String solicitante, String descricao, String origem,
                 String destino, String data_solicitacao, String status, int carga) {
        this.pk = pk;
        this.solicitante = solicitante;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
        this.data_solicitacao = data_solicitacao;
        this.status = status;
        this.carga = carga;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData_solicitacao() {
        return data_solicitacao;
    }

    public void setData_solicitacao(String data_solicitacao) {
        this.data_solicitacao = data_solicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }
}

