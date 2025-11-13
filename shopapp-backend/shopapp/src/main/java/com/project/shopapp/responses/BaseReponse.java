package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class BaseReponse {
    @JsonProperty( "create_at" )
    private LocalDateTime createAt;

    @JsonProperty("update_at" )
    private LocalDateTime updateAt;
}
