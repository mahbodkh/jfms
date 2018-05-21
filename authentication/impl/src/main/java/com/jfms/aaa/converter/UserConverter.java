package com.jfms.aaa.converter;


import com.jfms.aaa.dal.entity.UserEntity;
import com.jfms.aaa.model.UserInfo;
import com.jfms.aaa.model.UserRegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserEntity getEntity(UserRegistrationRequest userRegistrationRequest){
        if (userRegistrationRequest == null)
            return null;
        return new UserEntity(
                userRegistrationRequest.getMobileNumber(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName()
        );
    }

    public UserInfo getInfo(UserEntity userEntity){
        return new UserInfo(
                userEntity.getMobileNumber(),
                userEntity.getFirstName(),
                userEntity.getLastName()
        );
    }

}
