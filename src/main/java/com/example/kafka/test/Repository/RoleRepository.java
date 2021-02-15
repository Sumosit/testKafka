package com.example.kafka.test.Repository;

import java.util.Optional;

import com.example.kafka.test.Enum.ERole;
import com.example.kafka.test.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
