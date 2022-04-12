package com.project3.revtech.security;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class SecurityEncryptionConfig {
	
	@Bean
    @ConfigurationProperties("jasypt.encryptor")
    public SimpleStringPBEConfig jasypConfig() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        return config;
    }

    @Bean
    public StringEncryptor jasyptEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(jasypConfig());
        return encryptor;
    }

}
