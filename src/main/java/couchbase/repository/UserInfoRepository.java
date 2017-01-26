package couchbase.repository;

import couchbase.domain.UserInfo;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.N1qlSecondaryIndexed;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tushar Chokshi @ 1/25/17.
 */
@Repository

// Use these annotations only for dev purpose
@ViewIndexed(designDoc="userInfo", viewName = "all") // will create a view like the "all" view, to list all entities in the bucket.
@N1qlPrimaryIndexed // can be used to ensure a general-purpose PRIMARY INDEX is available in N1QL.
@N1qlSecondaryIndexed(indexName="userInfo_class") // This will create a GSI index on _class field of the document. _class filed is added by spring-data. It's value is same ase couchbase.domain.UserInfo  (CREATE INDEX `userInfo_class` ON `default`(`_class`) WHERE (`_class` = "couchbase.domain.UserInfo"))
public interface UserInfoRepository extends CouchbaseRepository<UserInfo, String> {

    /**
     * Additional custom finder method, backed by an auto-generated
     * N1QL query.
     */
    //List<UserInfo> findByLastnameAndAgeBetween(String lastName, int minAge, int maxAge);

    // This will query a bucket
    List<UserInfo> findByLastname(String lastName);


    @View
    List<UserInfo> findAllAdmins();
}
