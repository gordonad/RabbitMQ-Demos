package com.chariotsolutions.rabbitmq.repository;

import com.chariotsolutions.rabbitmq.domain.Mezzage;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Mezzage.class)
public interface MezzageRepository {
}
