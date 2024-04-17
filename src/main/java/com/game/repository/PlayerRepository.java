package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    public List<Player> findByName(String name);
    public List<Player> findByRace(Race race);

}
