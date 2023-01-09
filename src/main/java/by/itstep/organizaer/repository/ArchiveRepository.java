package by.itstep.organizaer.repository;

import by.itstep.organizaer.model.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
