package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/rest")
public class PlayerController {
    private PlayerService playerService;
    private PlayerValidator playerValidator;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerValidator playerValidator) {
        this.playerService = playerService;
        this.playerValidator = playerValidator;
    }

    @ExceptionHandler
    private ResponseEntity<PlayerErrorResponce> handleException(PlayerNotFoundException exception) {
        PlayerErrorResponce response = new PlayerErrorResponce("Player not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<PlayerErrorResponce> handleException(PlayerBadRequestException exception) {
        PlayerErrorResponce response = new PlayerErrorResponce("Bad request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/players")

    public List<Player> getAllPlayers (@RequestParam(defaultValue = "3") int pageSize,
                                       @RequestParam(defaultValue = "0") int pageNumber,
                                       @RequestParam(defaultValue = "ID") PlayerOrder order,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) Race race,
                                       @RequestParam(required = false)Profession profession,
                                       @RequestParam(required = false) Long after,
                                       @RequestParam(required = false) Long before,
                                       @RequestParam(required = false) Integer minExperience,
                                       @RequestParam(required = false) Integer maxExperience,
                                       @RequestParam(required = false) Integer minLevel,
                                       @RequestParam(required = false) Integer maxLevel,
                                       @RequestParam(required = false) Boolean banned) {

        CriteriaPlayer criteriaPlayer = new CriteriaPlayer(name, title, race, profession, after,
                                                           before, minExperience, maxExperience,
                                                           minLevel, maxLevel, banned);

        return playerService.findAll(pageNumber, pageSize, order, criteriaPlayer);
    }

    @GetMapping("/players/count")
    public int getCountPlayers (@RequestParam(required = false) String name,
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) Race race,
                                @RequestParam(required = false)Profession profession,
                                @RequestParam(required = false) Long after,
                                @RequestParam(required = false) Long before,
                                @RequestParam(required = false) Integer minExperience,
                                @RequestParam(required = false) Integer maxExperience,
                                @RequestParam(required = false) Integer minLevel,
                                @RequestParam(required = false) Integer maxLevel,
                                @RequestParam(required = false) Boolean banned) {

        CriteriaPlayer criteriaPlayer = new CriteriaPlayer(name, title, race, profession, after,
                before, minExperience, maxExperience,
                minLevel, maxLevel, banned);

        return playerService.count(criteriaPlayer);
    }

    @GetMapping("/players/{id}")
    public Player getPlayerById (@PathVariable("id") long id) {

        return playerService.findById(id);
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayerById (@PathVariable("id")  long id) {

        playerService.deleteById(id);

    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player, BindingResult bindingResult) {
        playerValidator.validate(player, bindingResult);

        playerService.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<HttpStatus> updatePlayer(@RequestBody Player player,
                                                   @PathVariable("id") long id) {
        playerService.save(player, id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
