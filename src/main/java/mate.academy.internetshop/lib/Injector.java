package mate.academy.internetshop.lib;

import static java.util.stream.Stream.of;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.internetshop.Factory;
import mate.academy.internetshop.Main;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.dao.impl.BucketDaoImpl;
import mate.academy.internetshop.dao.impl.ItemDaoImpl;
import mate.academy.internetshop.dao.impl.OrderDaoImpl;
import mate.academy.internetshop.dao.impl.UserDaoImpl;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;

public class Injector {

    public static void injectDependency() throws IllegalAccessException {
        injectToMain();
        injectToService(ItemServiceImpl.class);
        injectToService(BucketServiceImpl.class);
        injectToService(OrderServiceImpl.class);
        injectToService(UserServiceImpl.class);
    }

    private static void injectToMain() throws IllegalAccessException {
        for (Field field : getAnnotatedFields(Main.class)) {
            if (field.getType().equals(ItemService.class)
                    && ItemServiceImpl.class.isAnnotationPresent(Service.class)) {
                field.set(null, Factory.getItemService());
            } else if (field.getType().equals(BucketService.class)
                    && BucketServiceImpl.class.isAnnotationPresent(Service.class)) {
                field.set(null, Factory.getBucketService());
            } else if (field.getType().equals(OrderService.class)
                    && OrderServiceImpl.class.isAnnotationPresent(Service.class)) {
                field.set(null, Factory.getOrderService());
            } else if (field.getType().equals(UserService.class)
                    && UserServiceImpl.class.isAnnotationPresent(Service.class)) {
                field.set(null, Factory.getUserService());
            }
        }
    }

    private static void injectToService(Class service) throws IllegalAccessException {
        for (Field field : getAnnotatedFields(service)) {
            if (field.getType().equals(ItemDao.class)
                    && ItemDaoImpl.class.isAnnotationPresent(Dao.class)) {
                field.set(null, Factory.getItemDao());
            } else if (field.getType().equals(BucketDao.class)
                    && BucketDaoImpl.class.isAnnotationPresent(Dao.class)) {
                field.set(null, Factory.getBucketDao());
            } else if (field.getType().equals(OrderDao.class)
                    && OrderDaoImpl.class.isAnnotationPresent(Dao.class)) {
                field.set(null, Factory.getOrderDao());
            } else if (field.getType().equals(UserDao.class)
                    && UserDaoImpl.class.isAnnotationPresent(Dao.class)) {
                field.set(null, Factory.getUserDao());
            }
        }
    }

    private static List<Field> getAnnotatedFields(Class dependant) {
        return of(dependant.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }
}
