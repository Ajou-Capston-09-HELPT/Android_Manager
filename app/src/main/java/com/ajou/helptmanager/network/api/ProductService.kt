import com.ajou.helptmanager.membership.model.ProductRequest
import com.ajou.helptmanager.membership.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    @GET("/products/{gymId}")
    suspend fun getProductList(
        @Header("Authorization") accessToken: String,
        @Path("gymId") gymId: Long
    ): Response<List<ProductResponse>>

    @POST("/products/{gymId}")
    suspend fun addProduct(
        @Path("gymId") gymId: Long,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>

    @PUT("/products/{gymId}/{productId}")
    suspend fun modifyProduct(
        @Path("gymId") gymId: Long,
        @Path("productId") productId: Long,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>

    @DELETE("/products/{gymId}/{productId}")
    suspend fun removeProduct(
        @Path("productId") productId: Long
    ): Response<Boolean>
}