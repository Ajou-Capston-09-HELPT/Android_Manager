import com.ajou.helptmanager.network.model.AllProductsResponse
import com.ajou.helptmanager.home.model.product.ProductRequest
import com.ajou.helptmanager.home.model.product.ProductResponseData
import com.ajou.helptmanager.network.model.DeleteProductResponse
import com.ajou.helptmanager.network.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    @GET("/products/{gymId}")
    suspend fun getProductList(
        @Header("Authorization") accessToken: String,
        @Path("gymId") gymId: Int?
    ): Response<AllProductsResponse>

    @POST("/products/{gymId}")
    suspend fun addProduct(
        @Header("Authorization") accessToken: String,
        @Path("gymId") gymId: Int?,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>

    @PUT("/products/{gymId}/{productId}")
    suspend fun modifyProduct(
        @Header("Authorization") accessToken: String,
        @Path("gymId") gymId: Int?,
        @Path("productId") productId: Int?,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>

    @DELETE("/products/{gymId}/{productId}")
    suspend fun removeProduct(
        @Header("Authorization") accessToken: String,
        @Path("gymId") gymId: Int?,
        @Path("productId") productId: Int?
    ): Response<DeleteProductResponse>
}