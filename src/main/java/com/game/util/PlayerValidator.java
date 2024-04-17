package com.game.util;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PlayerValidator implements Validator {
    private final PlayerRepository repository;

    @Autowired
    public PlayerValidator(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Player.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Player player = (Player) o;

        if(player.getName() == null || player.getName().length() == 0) {
            throw new PlayerBadRequestException();
            //errors.rejectValue("name", "", "Too long name");
        }
        if(player.getTitle().length() > 30 || player.getName().length() > 12) {
            throw new PlayerBadRequestException();
        }

        if(player.getExperience() > 10000000 || player.getExperience() < 0) {
            throw new PlayerBadRequestException();
        }


    }
}
