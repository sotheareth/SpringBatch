package com.sotheareth;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.sotheareth.domain.UserRegistration;

public class UserFieldSet implements FieldSetMapper<UserRegistration> {
	public UserRegistration mapFieldSet(FieldSet fieldSet) {
		
		UserRegistration product = new UserRegistration();
		product.setFirstName(fieldSet.readString("firstName"));
		return product;
	}
}