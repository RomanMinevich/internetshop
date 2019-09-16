package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Item;

public interface ItemDao {

    Item create(Item item);

    Item read(Long id);

    Item update(Item item);

    Item delete(Long id);
}
