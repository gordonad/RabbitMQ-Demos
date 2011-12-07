package com.chariotsolutions.rabbitmq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.chariotsolutions.rabbitmq.domain.Mezzage;

@Repository
public interface MezzageRepository extends JpaSpecificationExecutor<Mezzage>,
		JpaRepository<Mezzage, Long> {
}
