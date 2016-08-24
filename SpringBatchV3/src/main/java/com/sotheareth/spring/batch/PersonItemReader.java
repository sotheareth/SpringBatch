package com.sotheareth.spring.batch;

import java.util.Collection;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class PersonItemReader implements ItemReader<Person> {

	@Override
	public Person read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		Collection<Person> persons = null;
		if (persons != null && persons.size() >= 1)
			return persons.iterator().next();
		return null;
	}

}
