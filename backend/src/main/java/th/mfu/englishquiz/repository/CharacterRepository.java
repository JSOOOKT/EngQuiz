package th.mfu.englishquiz.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import th.mfu.englishquiz.entity.GameCharacter;


public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
      List< GameCharacter> findByUserId(Long userId);
}