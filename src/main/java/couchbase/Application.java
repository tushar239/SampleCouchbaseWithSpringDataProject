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
            System.out.println();
            System.out.println(".....All UserInfos deleted");
        }
        System.out.println();

        {
            Iterable<UserInfo> userInfos = myService.createUserInfos();
            System.out.println();
            userInfos.forEach(userInfo -> System.out.println(".....Created UserInfo: " + userInfo));
        }
        System.out.println();

        {
            List<UserInfo> userInfos = myService.findAll();
            System.out.println();
            System.out.println(".....findAll: " + userInfos);
        }
        System.out.println();

        {
            List<UserInfo> userInfos = myService.findByLastname("Chokshi");
            System.out.println();
            System.out.println(".....findByLastname: " + userInfos); // [UserInfo{type='userinfo', version=315093947187200, firstname='Tushar', lastname='Chokshi', age=32, addresses=[Address{city='Redmond', state='WA', country='USA', zipCode='98052'}, Address{city='Sacaramento', state='CA', country='USA', zipCode='95647'}, Address{city='Vadodara', state='Gujarat', country='India', zipCode='35276'}]}]
        }
        System.out.println();

        {
            List<UserInfo> allAdmins = myService.findAllAdmins();
            System.out.println();
            System.out.println(".....findAllAdmins: " + allAdmins);
        }
        System.out.println();

        {
            List<UserInfo> userInfos = myService.findByMiddlenameEquals("Jagdishchandra");
            System.out.println();
            System.out.println(".....findByMiddlenameEquals: " + userInfos);
        }
        System.out.println();

        {
            List<UserInfo> userInfos = myService.findByMiddlenameStartingWith("Jag");
            System.out.println();
            System.out.println(".....findByMiddlenameStartingWith: " + userInfos);
        }
        System.out.println();

        {
            int count = myService.countByMiddlename();
            System.out.println();
            System.out.println(".....countByMiddlename: " + count);
        }
        System.out.println();

        {
            List<UserInfo> userInfosByCustomQuery = myService.findAllByCustomQuery();
            System.out.println();
            System.out.println(".....findAllByCustomQuery: "+userInfosByCustomQuery);
        }
    }

}

