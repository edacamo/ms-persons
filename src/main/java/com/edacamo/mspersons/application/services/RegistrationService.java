package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;

public interface RegistrationService {

    public RegisterResponse registerUser(RegisterRequest request);
}
