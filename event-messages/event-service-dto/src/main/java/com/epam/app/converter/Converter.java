package com.epam.app.converter;


import org.springframework.lang.NonNull;

public interface Converter <T,K> {
    @NonNull
    T convert (@NonNull K source);
}
