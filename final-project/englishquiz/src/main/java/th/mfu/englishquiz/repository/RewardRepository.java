package th.mfu.englishquiz.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import th.mfu.englishquiz.entity.Reward;


public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByUserId(Long userId); 
}
