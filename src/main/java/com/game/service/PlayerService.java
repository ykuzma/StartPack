package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.util.CriteriaPlayer;
import com.game.util.PlayerNotFoundException;
import com.game.util.PlayerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public List<Player> findAll(int page, int size, PlayerOrder order, CriteriaPlayer criteria) {
        List<Player> players;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(order.getFieldName()));
        Specification<Player> spec = PlayerSpecification.getFilterPlayer(criteria);
        players = playerRepository.findAll(spec, pageRequest).getContent();
        for (Player p: players) {
            System.out.println(p.getName());
        }
        return players;
    }

    public int count(CriteriaPlayer criteria) {
        Specification<Player> spec = PlayerSpecification.getFilterPlayer(criteria);
        return playerRepository.findAll(spec).size();
    }

    public Player findById(long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
       return optionalPlayer.orElseThrow(PlayerNotFoundException::new);
    }
    @Transactional
    public void deleteById(long id) {

        try {
            playerRepository.deleteById(id);
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }

    }

    @Transactional
    public void update(Player player, long id) {

            player.setId(id);
            playerRepository.save(player);


    }
    @Transactional
    public void save(Player player) {

        playerRepository.save(setExpForNextLevel(setLevel(player)));
    }

    private Player setLevel(Player player) {
        int exp = player.getExperience();
        player.setLevel(((int)Math.sqrt(2500 + 200 * exp) - 50) / 100);
        return player;
    }

    private Player setExpForNextLevel(Player player) {
        int level = player.getLevel();
        int exp = player.getExperience();
        player.setUntilNextLevel(50 * (level + 1) * (level + 2) - exp);
        return player;
    }

}