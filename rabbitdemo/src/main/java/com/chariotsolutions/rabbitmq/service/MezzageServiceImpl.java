package com.chariotsolutions.rabbitmq.service;

import java.util.List;

import com.chariotsolutions.rabbitmq.rabbit.RabbitGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chariotsolutions.rabbitmq.domain.Mezzage;
import com.chariotsolutions.rabbitmq.repository.MezzageRepository;

@Service
@Transactional
public class MezzageServiceImpl implements MezzageService {

    @Autowired
    RabbitGateway rabbitGateway;
    
	@Autowired
	MezzageRepository mezzageRepository;

	@Override
	public long countAllMezzages() {
		return mezzageRepository.count();
	}

	@Override
	public void deleteMezzage(Mezzage mezzage) {
		mezzageRepository.delete(mezzage);
	}

	@Override
	public Mezzage findMezzage(java.lang.Long id) {
		return mezzageRepository.findOne(id);
	}

	@Override
	public List<Mezzage> findAllMezzages() {
		return mezzageRepository.findAll();
	}

	@Override
	public List<Mezzage> findMezzageEntries(int firstResult, int maxResults) {
		return mezzageRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	@Override
	public void saveMezzage(Mezzage mezzage) {
		mezzageRepository.save(mezzage);
        rabbitGateway.send(mezzage.getMessageBody());
	}

	@Override
	public Mezzage updateMezzage(Mezzage mezzage) {
		return mezzageRepository.save(mezzage);
	}
}
