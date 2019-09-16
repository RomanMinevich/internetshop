package mate.academy.internetshop;

import java.util.List;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Injector;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;

public class Main {
    @Inject
    private static ItemService itemService;
    @Inject
    private static UserService userService;
    @Inject
    private static BucketService bucketService;
    @Inject
    private static OrderService orderService;

    static {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Item item1 = new Item("Соль", 300D);
        itemService.create(item1);
        Item item2 = new Item("Приправа", 250D);
        itemService.create(item2);
        Item item3 = new Item("Грибы", 400D);
        itemService.create(item3);
        Item item4 = new Item("Трава", 200D);
        itemService.create(item4);

        User user1 = userService.create(new User());
        Bucket bucket1 = bucketService.create(new Bucket(user1.getId()));

        bucketService.addItem(bucket1.getId(), item1.getId());
        bucketService.addItem(bucket1.getId(), item2.getId());
        bucketService.addItem(bucket1.getId(), item3.getId());

        User user2 = userService.create(new User());
        Bucket bucket2 = bucketService.create(new Bucket(user2.getId()));

        bucketService.delete(bucket2.getId());
        Bucket bucket3 = bucketService.create(new Bucket(user2.getId()));
        //bucketService.addItem(bucket2.getId(), item3.getId()); – will throw an exception
        bucketService.addItem(bucket3.getId(), item3.getId());
        bucketService.addItem(bucket3.getId(), item4.getId());

        Order order1 = orderService.completeOrder(
                bucketService.getAllItems(bucket1.getId()), bucket1.getUserId());
        orderService.create(order1);
        /*Order order2 = orderService.completeOrder
        (bucketService.getAllItems(bucket2.getId()), bucket2.getUserId());
        – will throw an exception */
        Order order2 = orderService.completeOrder(
                bucketService.getAllItems(bucket3.getId()), bucket3.getUserId());
        orderService.create(order2);

        bucketService.addItem(bucket1.getId(), item4.getId());
        bucketService.addItem(bucket1.getId(), item4.getId());

        bucketService.addItem(bucket3.getId(), item3.getId());
        bucketService.addItem(bucket3.getId(), item4.getId());

        Order order3 = orderService.completeOrder(
                bucketService.getAllItems(bucket1.getId()), bucket1.getUserId());
        orderService.create(order3);

        List<Order> orders1 = userService.getOrders(user1.getId());
        orders1
                .stream()
                .peek(System.out::println)
                .map(Order::getItems)
                .forEach(System.out::println);

        System.out.println();

        List<Order> orders2 = userService.getOrders(user2.getId());
        orders2
                .stream()
                .peek(System.out::println)
                .map(Order::getItems)
                .forEach(System.out::println);
    }
}
