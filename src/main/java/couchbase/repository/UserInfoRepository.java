package couchbase.repository;

import couchbase.domain.UserInfo;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.N1qlSecondaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
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
public interface UserInfoRepository extends CouchbasePagingAndSortingRepository<UserInfo, String> {

    //List<UserInfo> findByLastnameAndAgeBetween(String lastName, int minAge, int maxAge);

    // This will query entire bucket

    // Internally, it executes below N1QL query:
    // {
    //  "statement":"SELECT META(`default`).id AS _ID, META(`default`).cas AS _CAS, `default`.* FROM `default` WHERE `lastname` = \"Chokshi\" AND `_class` = \"couchbase.domain.UserInfo\"",
    //  "scan_consistency":"not_bounded"
    // }

    List<UserInfo> findByLastname(String lastName);


    // To use a View, you need to have @View.
    // If you don't specify a viewName attribute, then spring-data will expects you to have a view name 'allAdmins' under Production View's design doc '_design/userInfo'
    // If you don't have a view then you will get an exception 'com.couchbase.client.java.error.ViewDoesNotExistException: View userInfo/allAdmins does not exist.'

    // viewName Attr - The name of the View to use. If omitted, defaults to one derived from the method name (stripped of prefix "find" or "count").
    // This is mandatory to trigger a query derivation from the method name (ie. a View query with parameters like limit, startkey, etc...).

    // designDocument Attr - The name of the Design Document to use. If omitted, defaults to one derived from the entity class name (_design/userInfo).

    // reduce Attr - (default false) If you have a Reducer in your view, but if you don't want reduced data, then you need reduce=false. If you want reduced data, then you need reduce=true.

    // function (doc, meta) {
    //    if (doc._class == "couchbase.domain.UserInfo" && doc.isAdmin) {
    //        emit(null, null); // or emit(meta.id, null). for 'findAllAdmins', you are not specifying search by key, so it will just search doc ids. So, you don't need to specify index key for this view.
    //    }
    // }

    // Limitation :
    // At present in spring-data-couchbase, View based query derivation is limited to a few keywords and only works on simple keys (not compound keys like [ age, fname ]).

    // Internally, it creates a View query: ViewQuery(userInfo/allAdmins){params="stale=update_after"}
    @View
    List<UserInfo> findAllAdmins(); // same as find from allAdmins view under design document '_design/userInfo'.

    /*

    You need a view with below map function.

    function (doc, meta) {
      if(doc._class='couchbase.domain.UserInfo' && doc.middlename) {
        emit(doc.middlename, null); // index key is middlename
      }
    }
     */
    // Internally, it creates a View query: ViewQuery(userInfo/middleNames){params="stale=update_after&key=%22Jagdishchandra%22"}
    @View(viewName="middleNames") // default designDocument is same as name of the entity (userInfo). Default value of reduce=false. It means that even if you have a reducer specified in a View, it won't be used.
    List<UserInfo> findByMiddlenameEquals(String middleNamesViewKeyEqualsTo);

    // Internally, it creates a View query:  ViewQuery(userInfo/middleNames){params="stale=update_after&inclusive_end=false&startkey=%22Jag%22&endkey=%22Jag%EE%BF%BF%22"}
    @View(viewName="middleNames", designDocument = "userInfo")
    List<UserInfo> findByMiddlenameStartingWith(String middleNamesViewKeyStartsWith);

    @View(viewName="middleNames", designDocument = "userInfo")
    List<UserInfo> findByMiddlenameIn(String[] middleNamesViewKeyIn);

    // you need a reducer '_count' in a view for count*** methods
    // if you have a reducer '_stats', then it returns a json object. So, you need return type of your count*** method accordingly.

    // Internally, it creates a View Reduced query: ViewQuery(userInfo/middleNames){params="reduce=true&stale=update_after"}
    @View(viewName="middleNames", designDocument = "userInfo"/*, reduce = true*/) // even though default value of reduce=false, for count*** methods, it is set to true by spring-data. It means that it will use '_count' reducer.
    int countByMiddlename();

    // Internally, it creates an N1QL query: {"statement":"select d.*, meta(d).id as _ID, meta(d).cas as _CAS from default as d","scan_consistency":"not_bounded"}
    // Not sure why, but 'meta(d).id as _ID, meta(d).cas' in select query are mandatory. If you see for other findBy methods also, spring adds these fields internally.
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    // is same as
    //@Query("SELECT d.*, meta(d).id AS _ID, meta(d).cas AS _CAS FROM default AS d WHERE d._class='couchbase.domain.UserInfo'")

    /*
    #n1ql.selectEntity allows to easily make sure the statement will select all the fields necessary to build the full entity (including document ID and CAS value).

    #n1ql.filter in the WHERE clause adds a criteria matching the entity type with the field that Spring Data uses to store type information.

    #n1ql.bucket will be replaced by the name of the bucket the entity is stored in, escaped in backticks.

    #n1ql.fields will be replaced by the list of fields (eg. for a SELECT clause) necessary to reconstruct the entity.
    */

    List<UserInfo> findAllByCustomQuery();


    /*
    Limiting the result size of a query with Top and First

    User findFirstByOrderByLastnameAsc();

    User findTopByOrderByAgeDesc();

    Page<User> queryFirst10ByLastname(String lastname, Pageable pageable);

    Slice<User> findTop3ByLastname(String lastname, Pageable pageable);

    List<User> findFirst10ByLastname(String lastname, Sort sort);

    List<User> findTop10ByLastname(String lastname, Pageable pageable);
     */
}
