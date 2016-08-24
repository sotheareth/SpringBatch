package com.sotheareth;

import java.util.Collection;
import java.util.Date;

import com.sotheareth.domain.UserRegistration;

public interface UserRegistrationService {
	Collection<UserRegistration> getOutstandingUserRegistrationBatchForDate(
			int quantity, Date date);

	UserRegistration registerUser(UserRegistration userRegistrationRegistration);
}
