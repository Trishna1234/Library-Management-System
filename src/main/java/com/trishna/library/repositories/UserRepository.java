package com.trishna.library.repositories;

import com.trishna.library.models.SecuredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SecuredUser, Integer> {
    SecuredUser findByUsername(String username);
}
