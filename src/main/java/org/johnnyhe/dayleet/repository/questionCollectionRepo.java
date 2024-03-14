package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.questionCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface questionCollectionRepo extends JpaRepository<questionCollection, Long> {

}
