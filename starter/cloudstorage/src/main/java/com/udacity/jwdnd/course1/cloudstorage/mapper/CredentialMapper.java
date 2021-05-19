package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userId}")
    Credential[] getCredentialListings(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key},#{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void addCredential(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid=#{credentialId}")
    Credential getCredentialByCredentialId(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{encodedKey}, password = #{encryptedPassword}  WHERE credentialid = #{credentialId}")
    void updateCredential(Integer credentialId, String url, String username, String encryptedPassword, String encodedKey);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer credentialId);
}
