package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.constant.CredentialMessageEnum;
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

    private void validateValue(Credentials credential) throws Exception {
        String message = "";
        Boolean isError = false;
        if (credential.getUrl().length() > 100) {
            isError = true;
            message += CredentialMessageEnum.NOT_VALID_URL_LENGTH.message;
        }
        if (credential.getUsername().length() > 30) {
            isError = true;
            message += CredentialMessageEnum.NOT_VALID_USERNAME_LENGTH.message;
        }
        if (credential.getPassword().length() > 30) {
            isError = true;
            message += CredentialMessageEnum.NOT_VALID_PASSWORD_LENGTH.message;
        }

        if (isError) {
            throw new Exception(message);
        }
    }

    public void saveCredential(Credentials credential) throws Exception {
        validateValue(credential);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credentialMapper.insert(credential.getUrl(), credential.getUsername(), credential.getKey(), encryptedPassword, credential.getUserId());
    }

    public void updateCredential(Credentials credential) throws Exception {
        validateValue(credential);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credentialMapper.update(credential.getUrl(), credential.getUsername(), credential.getKey(), encryptedPassword, credential.getCredentialId());
    }

    public List<Credentials> getCredentials(int userId) {
        List<Credentials> list = credentialMapper.getCredentials(userId);
        List<Credentials> encryptedList = new ArrayList<Credentials>();
        for (Credentials c : list) {
            encryptedList.add(c);
        }
        return encryptedList;
    }

    public List<Credentials> getCredentialsByUsername(String username) {
        return credentialMapper.getCredentialsByUsername(username);
    }

    public void deleteCredential(int credentialId) {
        credentialMapper.delete(credentialId);
    }

}
