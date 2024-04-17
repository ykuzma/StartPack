package com.game.util;

import com.game.entity.Player;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PlayerSpecification {
    public  static Specification<Player> getFilterPlayer(CriteriaPlayer criteria) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(criteria.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%"));
            }
            if(criteria.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + criteria.getTitle() + "%"));
            }
            if(criteria.getRace() != null) {
                predicates.add(criteriaBuilder.equal(root.get("race"), criteria.getRace()));
            }
            if(criteria.getProfession() != null) {
                predicates.add(criteriaBuilder.equal(root.get("profession"), criteria.getProfession()));
            }
            if(criteria.getBanned() != null && criteria.getBanned()) {
                predicates.add(criteriaBuilder.isTrue(root.get("banned")));
            }
            if(criteria.getBanned() != null && !criteria.getBanned()){
                predicates.add(criteriaBuilder.isFalse(root.get("banned")));
            }

            if(criteria.getMinExperience() != null || criteria.getMaxExperience() != null){
                int min = criteria.getMinExperience() != null ? criteria.getMinExperience() : 0;
                int max = criteria.getMaxExperience() != null ? criteria.getMaxExperience() : 10000000;
                predicates.add(criteriaBuilder.between(root.get("experience"), min,
                        max));
            }
            if(criteria.getMinLevel() != null || criteria.getMaxLevel() != null){
                int min = criteria.getMinLevel() != null ? criteria.getMinLevel() : 0;
                int max = criteria.getMaxLevel() != null ? criteria.getMaxLevel() : 60;
                predicates.add(criteriaBuilder.between(root.get("level"), min,
                        max));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
