package com.crystalloids.alessandro.berardinelli.email;

import com.crystalloids.alessandro.berardinelli.api.dto.EmailDto;

public interface EmailNotificationsService {

    public void send(EmailDto emailDto);
}
