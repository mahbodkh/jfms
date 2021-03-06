package com.jfms.aaa.service;

import com.jfms.aaa.converter.UserConverter;
import com.jfms.aaa.dal.entity.ActivationEntity;
import com.jfms.aaa.dal.entity.UserEntity;
import com.jfms.aaa.dal.repository.ActivationRepository;
import com.jfms.aaa.dal.repository.UserRepository;
import com.jfms.aaa.model.UserActivationRequest;
import com.jfms.aaa.model.UserActivationResponse;
import com.jfms.aaa.model.UserInfo;
import com.jfms.aaa.service.biz.JWT;
import com.jfms.aaa.service.biz.RandomGenerator;
import com.jfms.aaa.service.biz.SMSService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ActivationService {
    @Autowired
    JWT jwt;
    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserConverter userConverter;
    @Autowired
    RandomGenerator randomGenerator;
    @Autowired
    SMSService smsService;

    private @Value("${token.ttl}") Long tokenTtl;

    public UserActivationResponse activateUser(UserActivationRequest userActivationRequest) {

        ActivationEntity activationEntity =
                activationRepository.findActivationEntityByActivationCodeAndIsUsedAndMobileNumber(
                userActivationRequest.getActivationCode(),
                false,
                userActivationRequest.getMobileNumber()
        );
        activationEntity.setUsed(true);
        activationRepository.save(activationEntity);
        UserEntity userEntity = userRepository.findByMobileNumber(userActivationRequest.getMobileNumber());
        UserInfo userInfo = userConverter.getInfo(userEntity);
        String jwtToken = jwt.createJWTToken(tokenTtl, userInfo);
        return new UserActivationResponse(jwtToken);
    }

    public UserActivationResponse getNewToken(String token) {
        try{
            jwt.parseJWT(token);
//            String jwtToken = jwt.createJWTToken(tokenTtl, null);
            return new UserActivationResponse(token);
        }catch (ExpiredJwtException e){
            String jwtToken = jwt.createJWTToken(tokenTtl, null);
            return new UserActivationResponse(jwtToken);
        }
    }

    public void generateActivationCode(Integer codeLength, String mobileNumber) throws TooRequestException {
        ActivationEntity activationEntity = null;
        activationEntity = activationRepository.findTopByMobileNumberOrderByCreationTimeDesc(mobileNumber);

        if (activationEntity != null && activationEntity.getUsed() == false && System.currentTimeMillis() - activationEntity.getCreationTime() < 60000 )
            throw new TooRequestException();
        String activationCode = randomGenerator.getRandomNumber(codeLength);
        activationRepository.save(new ActivationEntity(activationCode, mobileNumber));
        smsService.sendText(mobileNumber, activationCode);
    }
}
