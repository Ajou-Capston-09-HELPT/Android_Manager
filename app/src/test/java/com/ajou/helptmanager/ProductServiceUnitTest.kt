import com.ajou.helptmanager.home.model.ProductRequest
import com.ajou.helptmanager.network.model.AllProductsResponse
import com.ajou.helptmanager.home.model.ProductResponseData
import com.ajou.helptmanager.network.model.DeleteProductResponse
import com.ajou.helptmanager.network.model.ProductResponse
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class ProductServiceUnitTest {

    @Mock
    private lateinit var productService: ProductService
    private lateinit var testToken: String
    private var gymId: Int = 0
    private lateinit var productRequest: ProductRequest
    private var productId: Int = 0

    @Before
    fun setup() {
        testToken = "test_token"
        gymId = 1
        productRequest = ProductRequest(1, 1000)
        productId = 1
    }

    @Test
    fun should_return_product_list_when_called() = runBlockingTest {
        val expectedResponse = Response.success(AllProductsResponse("200",
            listOf(ProductResponseData(1, 1, 1000))
        ))
        `when`(productService.getProductList(testToken, gymId)).thenReturn(expectedResponse)

        val actualResponse = productService.getProductList(testToken, gymId)

        verify(productService).getProductList(testToken, gymId)
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun should_add_product_when_called() = runBlockingTest {
        val expectedResponse = Response.success(ProductResponse("200", ProductResponseData(1,1,1000)))
        `when`(productService.addProduct(testToken, gymId, productRequest)).thenReturn(expectedResponse)

        val actualResponse = productService.addProduct(testToken, gymId, productRequest)

        verify(productService).addProduct(testToken, gymId, productRequest)
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun should_modify_product_when_called() = runBlockingTest {
        val expectedResponse = Response.success(ProductResponse("200", ProductResponseData(1,1,1000)))
        `when`(productService.modifyProduct(testToken, gymId, productId, productRequest)).thenReturn(expectedResponse)

        val actualResponse = productService.modifyProduct(testToken, gymId, productId, productRequest)

        verify(productService).modifyProduct(testToken, gymId, productId, productRequest)
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun should_remove_product_when_called() = runBlockingTest {
        val expectedResponse = Response.success(DeleteProductResponse("200",true))
        `when`(productService.removeProduct(testToken, gymId, productId)).thenReturn(expectedResponse)

        val actualResponse = productService.removeProduct(testToken, gymId, productId)

        verify(productService).removeProduct(testToken, gymId, productId)
        Assert.assertEquals(expectedResponse, actualResponse)
    }

}