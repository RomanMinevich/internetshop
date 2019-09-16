package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Bucket;

public interface BucketDao {

    Bucket create(Bucket bucket);

    Bucket read(Long id);

    Bucket update(Bucket bucket);

    Bucket delete(Long id);
}
