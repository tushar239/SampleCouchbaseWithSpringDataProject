package couchbase.config;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import couchbase.repository.UserInfoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.support.IndexManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
In the case where the index creation cost isn’t considered too high and you are not in a production environment, it can be triggered automatically instead, in two steps. You will first need to annotate the repositories you want managed with the relevant annotation(s):

@ViewIndexed will create a view like the "all" view previously seen, to list all entities in the bucket.

@N1qlPrimaryIndexed can be used to ensure a general-purpose PRIMARY INDEX is available in N1QL.

@N1qlSecondaryIndexed will create a more specific N1QL index that does the same kind of filtering on entity type that the view does. It’ll allow for efficient listing of all documents that correspond to a Repository’s associated domain object.

Secondly, you’ll need to opt-in to this feature by customizing the indexManager() bean of your env-specific AbstractCouchbaseConfiguration to take certain types of annotations into account. This is done through the IndexManager(boolean processViews, boolean processN1qlPrimary, boolean processN1qlSecondary) constructor. Set the flags for the category of annotations you want processed to true, or false to deactivate the automatic creation feature.
*/

@Configuration
@EnableCouchbaseRepositories(basePackageClasses = {UserInfoRepository.class})
public class MyCouchbaseConfiguration extends AbstractCouchbaseConfiguration {


    // Using these information like hosts, bucketName and password, couchbaseCluster, couchbaseClient, couchbaseTemplate etc are created in AbstractCouchbaseConfiguration.
    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList("localhost");
    }

    @Override
    protected String getBucketName() {
        return "default";
    }

    @Override
    protected String getBucketPassword() {
        return "";
    }

    //this is for dev so it is ok to auto-create indexes
    @Override
    public IndexManager indexManager() {
        return new IndexManager(true, false, false);
    }

    // Auditing is not available in spring-data-couchbase 2.x.x
    @Bean
    public NaiveAuditorAware testAuditorAware() {
        return new NaiveAuditorAware();
    }

    // Configuration elements are closer to the Couchbase reality: Environment, Cluster, Bucket (potentially allowing you to create CouchbaseTemplates that each connect to a different bucket, or even cluster!)
    @Override
    protected CouchbaseEnvironment getEnvironment() {
        return DefaultCouchbaseEnvironment.builder()
                .connectTimeout(TimeUnit.SECONDS.toMillis(10))
                .computationPoolSize(6)
                .build();
    }


    // Setting Consistency in spring-data
    // Unlike to other NoSql DBs, in couchbase, write consistency is set during read time

    // Consistency Setting with Bucket Query(N1QL Query)
    // https://developer.couchbase.com/documentation/server/current/architecture/querying-data-with-n1ql.html
    // Consistency Setting with View Query
    // https://developer.couchbase.com/documentation/server/current/indexes/mapreduce-view-consistency.html

    // Spring-Data allows us to set both Bucket and View level consistencies together
    // http://docs.spring.io/spring-data/couchbase/docs/current/api/org/springframework/data/couchbase/core/query/Consistency.html
    // http://docs.spring.io/spring-data/couchbase/docs/current/reference/html/#couchbase.repository.consistency

    // Working with multiple buckets in spring-data
    // http://docs.spring.io/spring-data/couchbase/docs/current/reference/html/#couchbase.repository.multibucket

    /*@Bean
    public Bucket userBucket() {
        return couchbaseCluster().openBucket("users", "");
    }

    @Bean
    public CouchbaseTemplate userTemplate() {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), //reuse the default bean
                userBucket(), //the bucket is non-default
                mappingCouchbaseConverter(), translationService() //default beans here as well
        );
        template.setDefaultConsistency(getDefaultConsistency()); // setting consistency level
        return template;
    }

    //... then finally make sure all repositories of Users will use it
    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        baseMapping //this is already using couchbaseTemplate as default
                .mapEntity(User.class, userTemplate()); //every repository dealing with User will be backed by userTemplate()
    }*/
}

