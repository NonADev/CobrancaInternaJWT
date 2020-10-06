package br.com.sevencomm.cobranca.domain.models;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Cobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    Integer pagadorAreaId;

    @NotNull
    Integer beneficiarioAreaId;

    @NotNull
    Integer statusId;
    String datahora; // ! colocar como datetime
    String descricao;
    Double valor;
}
