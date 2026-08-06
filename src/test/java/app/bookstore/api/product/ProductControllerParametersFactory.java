package app.bookstore.api.product;

import app.bookstore.api.utils.DataTestParameters;
import app.bookstore.db.models.PostRecord;
import app.bookstore.db.models.ProductRecord;

import java.time.LocalDateTime;

public class ProductControllerParametersFactory {

    public DataTestParameters<ProductResponse, ProductRecord, Integer> productId() {
        return new DataTestParameters<>(
                "product id",
                ProductResponse::getId,
                ProductRecord::getProductId
        );
    }

    public DataTestParameters<ProductResponse, ProductRecord, Double> minPrice() {
        return new DataTestParameters<>(
                "minimal price",
                ProductResponse::getPrice,
                ProductRecord::getMinPrice
        );
    }

    public DataTestParameters<ProductResponse, ProductRecord, Double> maxPrice() {
        return new DataTestParameters<>(
                "maximal price",
                ProductResponse::getPrice,
                ProductRecord::getMaxPrice
        );
    }

    public DataTestParameters<ProductResponse, PostRecord, String> name() {
        return new DataTestParameters<>(
                "name",
                ProductResponse::getName,
                PostRecord::getName
        );
    }

    public DataTestParameters<ProductResponse, PostRecord, String> postStatus() {
        return new DataTestParameters<>(
                "post status",
                ProductResponse::getStatus,
                PostRecord::getStatus
        );
    }

    public DataTestParameters<ProductResponse, PostRecord, String> slug() {
        return new DataTestParameters<>(
                "slug",
                ProductResponse::getSlug,
                PostRecord::getSlug
        );
    }

    public DataTestParameters<ProductResponse, PostRecord, LocalDateTime> postDate() {
        return new DataTestParameters<>(
                "post date",
                ProductResponse::getDateCreated,
                PostRecord::getPostDate
        );
    }
}
