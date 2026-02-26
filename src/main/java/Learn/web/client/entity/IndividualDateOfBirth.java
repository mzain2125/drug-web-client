package Learn.web.client.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "date_of_births")
@Getter
@Setter
public class IndividualDateOfBirth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String typeOfDate;

    @ManyToOne
    @JoinColumn(name = "individual_id")
    private Individual individual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTypeOfDate() {
        return typeOfDate;
    }

    public void setTypeOfDate(String typeOfDate) {
        this.typeOfDate = typeOfDate;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
}

