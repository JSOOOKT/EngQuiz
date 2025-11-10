package th.mfu.englishquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.mfu.englishquiz.entity.Tier; // Uses the new Tier entity

@Repository
public interface TierRepository extends JpaRepository<Tier, Long> {
    // Basic CRUD methods are inherited
}