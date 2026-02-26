package Learn.web.client.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;



@Entity
@Table(name = "individuals")
public class Individual {

    @Id
    private Long dataId;

    @Setter
    private String firstName;
    private String secondName;
    private String referenceNumber;
    private String unListType;
    private String gender;
//    @Lob
//    private String comments;

    private LocalDate listedOn;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<Nationality> nationalities;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<IndividualAlias> aliases;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<IndividualAddress> addresses;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<IndividualDocument> documents;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<IndividualDateOfBirth> dateOfBirths;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<LastDayUpdated> lastDayUpdatedList;

    public Individual() {
    }

    public Individual(Long dataId, String firstName, String secondName, String referenceNumber, String unListType, String gender, String comments, LocalDate listedOn) {
        this.dataId = dataId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.referenceNumber = referenceNumber;
        this.unListType = unListType;
        this.gender = gender;
//        this.comments = comments;
        this.listedOn = listedOn;
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

    public String getUnListType() {
        return unListType;
    }

    public void setUnListType(String unListType) {
        this.unListType = unListType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    public String getComments() {
//        return comments;
//    }
//
//    public void setComments(String comments) {
//        this.comments = comments;
//    }

    public LocalDate getListedOn() {
        return listedOn;
    }

    public void setListedOn(LocalDate listedOn) {
        this.listedOn = listedOn;
    }

    public List<Nationality> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<Nationality> nationalities) {
        this.nationalities = nationalities;
    }

    public List<IndividualAlias> getAliases() {
        return aliases;
    }

    public void setAliases(List<IndividualAlias> aliases) {
        this.aliases = aliases;
    }

    public List<IndividualAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<IndividualAddress> addresses) {
        this.addresses = addresses;
    }

    public List<IndividualDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<IndividualDocument> documents) {
        this.documents = documents;
    }

    public List<IndividualDateOfBirth> getDateOfBirths() {
        return dateOfBirths;
    }

    public void setDateOfBirths(List<IndividualDateOfBirth> dateOfBirths) {
        this.dateOfBirths = dateOfBirths;
    }

    public List<LastDayUpdated> getLastDayUpdatedList() {
        return lastDayUpdatedList;
    }

    public void setLastDayUpdatedList(List<LastDayUpdated> lastDayUpdatedList) {
        this.lastDayUpdatedList = lastDayUpdatedList;
    }
}
