package org.springframework.samples.petclinic.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {

    private Integer steal;
    private Integer gold;
    private Integer iron;
    private Integer medal;

    public PlayerDTO() {
    }

    public PlayerDTO(Player p) {
        this.steal = p.getSteal();
        this.gold = p.getGold();
        this.iron = p.getIron();
        this.medal = p.getMedal();
    }
}
