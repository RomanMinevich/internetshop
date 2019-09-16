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
            if (checkDependency(field, ItemServiceImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getItemService());
            } else if (checkDependency(field, BucketServiceImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getBucketService());
            } else if (checkDependency(field, OrderServiceImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getOrderService());
            } else if (checkDependency(field, UserServiceImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getUserService());
            }
        }
    }

    private static void injectToService(Class service) throws IllegalAccessException {
        for (Field field : getAnnotatedFields(service)) {
            if (checkDependency(field, ItemDaoImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getItemDao());
            } else if (checkDependency(field, BucketDaoImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getBucketDao());
            } else if (checkDependency(field, OrderDaoImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getOrderDao());
            } else if (checkDependency(field, UserDaoImpl.class)) {
                field.setAccessible(true);
                field.set(null, Factory.getUserDao());
            }
        }
    }

    private static List<Field> getAnnotatedFields(Class dependant) {
        return of(dependant.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    private static boolean checkDependency(Field annotatedField, Class dependency) {
        return of(dependency.getInterfaces())
                .anyMatch(dependencyInt -> dependencyInt.equals(annotatedField.getType()))
                && (dependency.isAnnotationPresent(Service.class)
                || dependency.isAnnotationPresent(Dao.class));
    }
}
