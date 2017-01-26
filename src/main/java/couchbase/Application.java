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


        // deleteAll, findAll etc uses a ViewQuery. It finds all doc ids from a view and then queries a bucket to get all docs.
        // So, it is mandatory to have a view for these queries.
        // In Development View, you need to create a view 'all' under design doc "_design/dev_userInfo" and publish it to Production View.
        // In Production View, this 'all' view will start appearing under design doc "_design/userInfo"
        // These queries by default expects a view 'all' under design doc "_design/userInfo"
        // You can see the code of findAll and deleteAll methods.
        //function (doc, meta) {
        //    if(doc._class = 'couchbase.domain.UserInfo') {
        //        emit(meta.id, null);
        //    }
        //}
        // countAll method needs a Reducer (_count) also for this view.
        MyService myService = applicationContext.getBean(MyService.class);
        {
            myService.deleteAll();
        }

        {
            UserInfo userInfo = myService.create();
            System.out.println(userInfo);
        }
        {
            List<UserInfo> userInfos = myService.findAll();
            System.out.println(userInfos);
        }
        {
            List<UserInfo> userInfos = myService.findByLastname();
            System.out.println(userInfos); // [UserInfo{type='userinfo', version=315093947187200, firstname='Tushar', lastname='Chokshi', age=32, addresses=[Address{city='Redmond', state='WA', country='USA', zipCode='98052'}, Address{city='Sacaramento', state='CA', country='USA', zipCode='95647'}, Address{city='Vadodara', state='Gujarat', country='India', zipCode='35276'}]}]
        }
    }

}

