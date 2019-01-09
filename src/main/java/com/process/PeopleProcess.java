package com.process;

import org.springframework.batch.item.ItemProcessor;

import com.model.People;

public class PeopleProcess implements ItemProcessor<People, People>{

	@Override
	public People process(People item) throws Exception {
		String firstName = item.getFirstName().toUpperCase();
		String lastName = item.getLastName().toUpperCase();
		People transfromPeople = new People();
		transfromPeople.setFirstName(firstName);
		transfromPeople.setLastName(lastName);
		return transfromPeople;
	}

}
