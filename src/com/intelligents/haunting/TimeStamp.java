package com.intelligents.haunting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class TimeStamp implements java.io.Serializable {
    String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy @ HH:mm"));
}

