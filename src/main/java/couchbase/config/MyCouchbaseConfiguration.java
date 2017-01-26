package couchbase.config;

import couchbase.repository.UserInfoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.support.IndexManager;

import java.util.Arrays;
import java.util.List;

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
}
