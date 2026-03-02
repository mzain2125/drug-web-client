package Learn.web.client.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UNSC_DATA")
public class UnscData {

    @Id
    @Column(name = "DATAID", nullable = false)
    private Long dataId;

    @Column(name = "VERSIONNUM")
    private Integer versionNum;

    @Column(name = "FIRST_NAME", length = 200)
    private String firstName;

    @Column(name = "SECOND_NAME", length = 100)
    private String secondName;

    @Column(name = "THIRD_NAME", length = 100)
    private String thirdName;

    @Column(name = "UN_LIST_TYPE", length = 20)
    private String unListType;

    @Column(name = "REFERENCE_NUMBER", length = 20)
    private String referenceNumber;

    /** This will use the converter automatically */
    @Column(name = "LISTED_ON", length = 20)
    @Convert(converter = LocalDateStringConverter.class)  // Apply converter
    private LocalDate listedOn;

    @Column(name = "NAME_ORIGINAL_SCRIPT", length = 100)
    private String nameOriginalScript;

    @Column(name = "COMMENTS1", length = 4000)
    private String comments1;

    /** "I" = Individual, "E" = Entity */
    @Column(name = "TYPE", length = 1)
    private String type;

    @Column(name = "FILE_UPLOADED_DATE", updatable = false)
    private LocalDateTime FILE_UPLOADED_DATE;

    @PrePersist
    public void prePersist() {
        this.FILE_UPLOADED_DATE = LocalDateTime.now();
    }
}