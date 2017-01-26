package couchbase.service;

import couchbase.domain.Address;
import couchbase.domain.UserInfo;
import couchbase.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tushar Chokshi @ 1/25/17.
 */
@Service
public class MyService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    public MyService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfoRepository getUserInfoRepository() {
        return userInfoRepository;
    }

    public void setUserInfoRepository(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public List<UserInfo> findAll() {
        Iterable<UserInfo> all = userInfoRepository.findAll();

        List<UserInfo> userInfos = new ArrayList<>();
        for (UserInfo userInfo : all) {
            userInfos.add(userInfo);
        }

        return userInfos;
    }

    public void deleteAll() {
        userInfoRepository.deleteAll();
    }

    public UserInfo create() {
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstname("Tushar");
        userInfo.setLastname("Chokshi");
        userInfo.setAge(32);

        Address add1 = new Address();
        add1.setCity("Redmond");
        add1.setState("WA");
        add1.setCountry("USA");
        add1.setZipCode("98052");

        Address add2 = new Address();
        add2.setCity("Sacaramento");
        add2.setState("CA");
        add2.setCountry("USA");
        add2.setZipCode("95647");

        Address add3 = new Address();
        add3.setCity("Vadodara");
        add3.setState("Gujarat");
        add3.setCountry("India");
        add3.setZipCode("35276");


        List<Address> addresses = new ArrayList<>();
        addresses.add(add1);
        addresses.add(add2);
        addresses.add(add3);

        userInfo.setAddresses(addresses);

        return userInfoRepository.save(userInfo);
    }


    public List<UserInfo> findByLastname() {
        return userInfoRepository.findByLastname("Chokshi");
    }

}