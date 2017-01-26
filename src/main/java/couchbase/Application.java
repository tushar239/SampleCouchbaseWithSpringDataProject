package couchbase;

import couchbase.domain.UserInfo;
import couchbase.service.MyService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */
// http://docs.spring.io/spring-data/couchbase/docs/current/reference/html/
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationConfiguration.class);
        applicationContext.refresh();

        /*String[] beanDefNames = applicationContext.getBeanDefinitionNames();
        for (int i = 0; i < beanDefNames.length; i++) {
            System.out.println(beanDefNames[i]);

        }*/

        MyService myService = applicationContext.getBean(MyService.class);
        {
            myService.deleteAllUserInfos();
            System.out.println("All UserInfos deleted");
        }

        {
            UserInfo userInfo = myService.createUserInfo();
            System.out.println("Created UserInfo: "+userInfo);
        }
        {
            List<UserInfo> userInfos = myService.findAll();
            System.out.println("findAll: "+userInfos);
        }
        {
            List<UserInfo> userInfos = myService.findByLastname();
            System.out.println("findByLastname: "+userInfos); // [UserInfo{type='userinfo', version=315093947187200, firstname='Tushar', lastname='Chokshi', age=32, addresses=[Address{city='Redmond', state='WA', country='USA', zipCode='98052'}, Address{city='Sacaramento', state='CA', country='USA', zipCode='95647'}, Address{city='Vadodara', state='Gujarat', country='India', zipCode='35276'}]}]
        }
        {
            List<UserInfo> allAdmins = myService.findAllAdmins();
            System.out.println("findAllAdmins: "+allAdmins);
        }

        {
            List<UserInfo> userInfos = myService.findByMiddlenameEquals("Jagdishchandra");
            System.out.println("findByMiddlenameEquals: "+ userInfos);
        }

        {
            List<UserInfo> userInfos = myService.findByMiddlenameStartingWith("Jag");
            System.out.println("findByMiddlenameStartingWith: " + userInfos);
            System.out.println(userInfos);
        }

        {
            int count = myService.countByMiddlename();
            System.out.println("countByMiddlename: " + count);
        }
    }

}

