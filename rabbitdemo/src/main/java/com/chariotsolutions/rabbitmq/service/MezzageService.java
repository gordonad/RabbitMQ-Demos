package com.chariotsolutions.rabbitmq.service;

import java.util.List;

import com.chariotsolutions.rabbitmq.domain.Mezzage;

public interface MezzageService {

	public abstract long countAllMezzages();

	public abstract void deleteMezzage(Mezzage mezzage);

	public abstract Mezzage findMezzage(java.lang.Long id);

	public abstract List<Mezzage> findAllMezzages();

	public abstract List<Mezzage> findMezzageEntries(int firstResult,
			int maxResults);

	public abstract void saveMezzage(Mezzage mezzage);

	public abstract Mezzage updateMezzage(Mezzage mezzage);

}
