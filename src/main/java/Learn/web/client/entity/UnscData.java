package Learn.web.client.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA entity mapped to the UNSC_DATA Oracle table.
 *
 * NOTE: The DB column LISTED_ON is VARCHAR2(20) in Oracle, but we map it
 * to LocalDate in Java. JPA will use the @Column definition for DDL generation;
 * at runtime Spring converts LocalDate <-> VARCHAR2 automatically via the
 * dialect converter. If you manage DDL manually (as shown), this works fine.
 */
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

    /**
     * Stored as VARCHAR2(20) in Oracle ("YYYY-MM-DD" format).
     * Hibernate converts LocalDate to/from String via the column definition below.
     * If you prefer a DATE column, change the Oracle DDL accordingly.
     */
    @Column(name = "LISTED_ON", length = 20)
    private LocalDate listedOn;

    @Column(name = "NAME_ORIGINAL_SCRIPT", length = 100)
    private String nameOriginalScript;

    @Column(name = "COMMENTS1", length = 4000)
    private String comments1;

    /** "I" = Individual, "E" = Entity (reserved for future use) */
    @Column(name = "TYPE", length = 1)
    private String type;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}