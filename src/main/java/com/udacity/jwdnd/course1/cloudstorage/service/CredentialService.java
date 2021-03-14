package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialsMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialsMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void saveCredential(Credentials credential) {
        credentialMapper.insert(credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getUserId());
    }

    public void updateCredential(Credentials credential) {
        credentialMapper.update(credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getCredentialId());
    }

    public List<Credentials> getCredentials(int userId) {
        List<Credentials> list = credentialMapper.getCredentials(userId);
        List<Credentials> encryptedList = new ArrayList<Credentials>();
        for (Credentials c : list) {
            encryptedList.add(c);
        }
        return encryptedList;
    }

    public void deleteCredential(int credentialId) {
        credentialMapper.delete(credentialId);
    }

}
