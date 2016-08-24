package com.sotheareth;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.sotheareth.domain.UserRegistration;

public class UserRegistrationServiceItemWriter implements
		ItemWriter<UserRegistration> {
	private static final Logger logger = LoggerFactory
			.getLogger(UserRegistrationServiceItemWriter.class);
	// this is the client interface to an HTTP Invoker service.
	@Autowired
	private UserRegistrationService userRegistrationService;

	/**
	 * takes aggregated input from the reader and 'writes' them using a custom
	 * implementation.
	 */
	public void write(List<? extends UserRegistration> items) throws Exception {
		for (final UserRegistration userRegistration : items) {
			UserRegistration registeredUser = userRegistrationService
					.registerUser(userRegistration);
			
		}
	}
}