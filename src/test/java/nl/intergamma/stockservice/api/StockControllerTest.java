package nl.intergamma.stockservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.intergamma.stockservice.domain.ClaimRequest;
import nl.intergamma.stockservice.domain.StockInfo;
import nl.intergamma.stockservice.service.ClaimService;
import nl.intergamma.stockservice.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @MockBean
    private ClaimService claimService;

    @Test
    public void stockGetShouldReturnStockInfo() throws Exception {
        StockInfo stockInfo = new StockInfo("12345",5,"Leusden");

        Mockito.when(stockService.getStockInfo("12345", "Leusden")).thenReturn(stockInfo);

        mockMvc.perform(get("/api/v1/stock/12345/Leusden"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productCode").value("12345"));
    }

    @Test
    public void stockSaveShouldThrowValidationError() throws Exception {
        StockInfo stockInfo = new StockInfo("12345",-1,"Leusden");

        mockMvc.perform(post("/api/v1/stock").contentType(MediaType.APPLICATION_JSON).content(asJson(stockInfo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void stockSaveShouldPassStockInfoToService() throws Exception {
        StockInfo stockInfo = new StockInfo("12345",5,"Leusden");

        Mockito.when(stockService.saveStockInfo(stockInfo)).thenReturn(stockInfo);

        mockMvc.perform(post("/api/v1/stock").contentType(MediaType.APPLICATION_JSON).content(asJson(stockInfo)))
                .andExpect(status().isCreated());
    }

    @Test
    public void claimShouldBePassedToService() throws Exception {
        ClaimRequest claimRequest = new ClaimRequest("12345",5,"Leusden");

        mockMvc.perform(post("/api/v1/stock/claim").contentType(MediaType.APPLICATION_JSON).content(asJson(claimRequest)))
                .andExpect(status().isCreated());

        Mockito.verify(claimService).claimStock(claimRequest);
    }


    protected String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.registerModule(new JavaTimeModule());
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(object);
    }
}
