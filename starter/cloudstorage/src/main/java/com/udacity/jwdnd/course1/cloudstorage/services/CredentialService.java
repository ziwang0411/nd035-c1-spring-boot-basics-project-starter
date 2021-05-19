package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }


    public Credential[] getCredentialListings(Integer userId) {
        return credentialMapper.getCredentialListings(userId);
    }

    public void addCredential(String url, String username, String password, String encodedKey, Integer userId) {
        Credential credential = new Credential(null, url, username, encodedKey,password,userId);
        credentialMapper.addCredential(credential);
    }

    public Credential getCredentialByCredentialId(Integer credentialId) {
        return credentialMapper.getCredentialByCredentialId(credentialId);
    }

    public void updateCredential(Integer credentialId, String url, String username, String encryptedPassword, String encodedKey) {
        credentialMapper.updateCredential(credentialId, url, username, encryptedPassword, encodedKey);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }
}
