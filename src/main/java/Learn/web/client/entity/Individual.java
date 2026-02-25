package Learn.web.client.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "individuals")
public class Individual {

    @Id
    private Long dataId;

    private String firstName;
    private String secondName;
    private String referenceNumber;
    private LocalDate listedOn;
    private String nationality;

    public Individual() {
    }

    public Individual(Long dataId, String firstName, String secondName, String referenceNumber, LocalDate listedOn, String nationality) {
        this.dataId = dataId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.referenceNumber = referenceNumber;
        this.listedOn = listedOn;
        this.nationality = nationality;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getListedOn() {
        return listedOn;
    }

    public void setListedOn(LocalDate listedOn) {
        this.listedOn = listedOn;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}