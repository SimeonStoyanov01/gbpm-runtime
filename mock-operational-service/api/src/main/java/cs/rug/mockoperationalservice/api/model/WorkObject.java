package cs.rug.mockoperationalservice.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkObject {
    @NotBlank
    private String objectId;

    @NotBlank
    private String type;

    @NotBlank
    private String material;
}
