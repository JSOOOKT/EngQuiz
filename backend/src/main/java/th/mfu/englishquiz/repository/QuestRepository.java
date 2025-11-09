package th.mfu.englishquiz.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import th.mfu.englishquiz.entity.Quest;


public interface QuestRepository extends JpaRepository<Quest, Long> {
     
}