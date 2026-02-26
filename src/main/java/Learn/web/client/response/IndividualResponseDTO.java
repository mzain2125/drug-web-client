package Learn.web.client.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class IndividualResponseDTO {

    private Long dataId;
    private String firstName;
    private String secondName;
    private String referenceNumber;
    private String gender;
    private LocalDate listedOn;

    private List<String> nationalities;
}
