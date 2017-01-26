package couchbase.domain;

import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

/**
 * @author Tushar Chokshi @ 1/25/17.
 */
@Document(expiry = 0)
public class UserInfo {
    @Id
    private String firstname;
    private String lastname;
    private String middlename;
    private int age;
    private List<Address> addresses;
    private String type = "userinfo";

    private boolean isAdmin;

    /*
    Couchbase Server does not support multi-document transactions or rollback. To implement optimistic locking, Couchbase uses a CAS (compare and swap) approach. When a document is mutated, the CAS value also changes. The CAS is opaque to the client, the only thing you need to know is that it changes when the content or a meta information changes too.

    In other datastores, similar behavior can be achieved through an arbitrary version field with a incrementing counter. Since Couchbase supports this in a much better fashion, it is easy to implement. If you want automatic optimistic locking support, all you need to do is add a @Version annotation on a long field like this:

    If you load a document through the template or repository, the version field will be automatically populated with the current CAS value. It is important to note that you shouldn’t access the field or even change it on your own. Once you save the document back, it will either succeed or fail with a OptimisticLockingFailureException. If you get such an exception, the further approach depends on what you want to achieve application wise. You should either retry the complete load-update-write cycle or propagate the error to the upper layers for proper handling.
    */
    @Version
    private long version;

/*    @CreatedBy
    private String creator;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModification;

    @CreatedDate
    private Date creationDate;*/

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (age != userInfo.age) return false;
        if (isAdmin != userInfo.isAdmin) return false;
        if (version != userInfo.version) return false;
        if (firstname != null ? !firstname.equals(userInfo.firstname) : userInfo.firstname != null) return false;
        if (lastname != null ? !lastname.equals(userInfo.lastname) : userInfo.lastname != null) return false;
        if (middlename != null ? !middlename.equals(userInfo.middlename) : userInfo.middlename != null) return false;
        if (addresses != null ? !addresses.equals(userInfo.addresses) : userInfo.addresses != null) return false;
        return type != null ? type.equals(userInfo.type) : userInfo.type == null;

    }

    @Override
    public int hashCode() {
        int result = firstname != null ? firstname.hashCode() : 0;
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (isAdmin ? 1 : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", age=" + age +
                ", addresses=" + addresses +
                ", type='" + type + '\'' +
                ", isAdmin=" + isAdmin +
                ", version=" + version +
                '}';
    }
}
