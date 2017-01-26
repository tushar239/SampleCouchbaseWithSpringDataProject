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

/*
- Couchbase expects to have PRIMARY INDEX on all documents. Without it, document(s) cannot be searched.

'CREATE PRIMARY INDEX `default_pk_gsi_index` ON `default`'

You can use '@N1qlPrimaryIndexed' on UserInfoRepository  along with 'new IndexManager(true, false, false)', but this should be done only for dev purpose.
For prod, you should create indexes manually using couchbase admin console (http://localhost:8091/ui/index.html)
You can install use 'Couchbase Query Bench' for queries (http://localhost:8095/). Once you install it, it will be integrated automatically with Admin Console also.

- It is also advisable to create an index on 'type' field of your document. Spring-Data adds '_class' field. You can use that or have your own 'type' field.
spring-data will add _class=couchbase.domain.UserInfo to every document you persist in couchbase.

CREATE INDEX `userInfo_class` ON `default`(`_class`) WHERE (`_class` = "couchbase.domain.UserInfo")

- 'all' view under Production View's design doc "_design/userInfo" is required

deleteAll, findAll etc uses a ViewQuery. It finds all doc ids from a view and then queries a bucket to get all docs.
So, it is mandatory to have a view for these queries.
In Development View, you need to create a view 'all' under design doc "_design/dev_userInfo" and publish it to Production View.
In Production View, this 'all' view will start appearing under design doc "_design/userInfo"
These queries by default expects a view 'all' under design doc "_design/userInfo"
You can see the code of findAll and deleteAll methods.

function (doc, meta) {
    if(doc._class = 'couchbase.domain.UserInfo') {
        emit(meta.id, null);
    }
}

countAll method needs a Reducer (_count) also for this view.



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

    public void deleteAllUserInfos() {
        userInfoRepository.deleteAll();
    }

    public UserInfo createUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstname("Tushar");
        userInfo.setMiddlename("Jagdishchandra");
        userInfo.setLastname("Chokshi");
        userInfo.setAge(32);
        userInfo.setAdmin(true);

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

    public List<UserInfo> findAllAdmins() {
        return userInfoRepository.findAllAdmins();
    }

    public List<UserInfo> findByMiddlenameEquals(String middleName) {
        return userInfoRepository.findByMiddlenameEquals(middleName);
    }

    public List<UserInfo> findByMiddlenameStartingWith(String middleNamePrefix) {
        return userInfoRepository.findByMiddlenameStartingWith(middleNamePrefix);
    }

    public int countByMiddlename() {
        return userInfoRepository.countByMiddlename();
    }
}