package com.fivem.alderalife.repository;

import com.fivem.alderalife.model.User;
import com.fivem.alderalife.model.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, Long> {

    Optional<Whitelist> findByUser(User user);

    List<Whitelist> findLastByUser(User user);

    long countByUser(User user);
}
