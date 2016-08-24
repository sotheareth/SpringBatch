package com.sotheareth.spring.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;


public class PersonValidationItemProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(Person item) throws Exception {
		Boolean isValidFirstName = StringUtils.isEmpty(item.getFirstName());
		Boolean isValidLastName  = StringUtils.isEmpty(item.getLastName());
		return (isValidFirstName && isValidLastName) ? item : null;
	}

}
