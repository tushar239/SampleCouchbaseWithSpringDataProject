package couchbase.config;

import org.springframework.data.domain.AuditorAware;

/**
 * @author Tushar Chokshi @ 1/25/17.
 */
// Auditing is not supported by spring-data-couch 2.x.x
public class NaiveAuditorAware implements AuditorAware<String> {

    private String auditor = "auditor";

    @Override
    public String getCurrentAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
}
