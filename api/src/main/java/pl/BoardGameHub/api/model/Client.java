package pl.BoardGameHub.api.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true) // wymagane przy dziedziczeniu i Lomboku
public class Client extends User{
    private int LoyaltyPoints;
}
