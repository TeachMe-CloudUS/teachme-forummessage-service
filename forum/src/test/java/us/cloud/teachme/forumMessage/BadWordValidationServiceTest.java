package us.cloud.teachme.forumMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import us.cloud.teachme.forummessage.service.BadWordsService;

public class BadWordValidationServiceTest {

    @InjectMocks
    private BadWordsService badWordsService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateContent_noBadWords() {
        // Simulamos una respuesta de la API con un contenido sin malas palabras
        String content = "This is a clean message";
        String jsonResponse = "{\"result\":\"This is a clean message\"}";  // La respuesta indica que no tiene malas palabras
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Ejecutamos la validación
        boolean containsbadwords = badWordsService.containsBadWords(content);

        // Verificamos que el resultado es válido (sin malas palabras)
        assertFalse(containsbadwords);
    }

    @Test
    public void testValidateContent_withBadWords() {
        // Simulamos una respuesta de la API con un contenido que tiene malas palabras
        String content = "This message has badwords bitch";
        String jsonResponse = "{\"result\":\"This message has badwords *****\"}";  // La respuesta indica que tiene malas palabras
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);
        
        System.err.println(content);
        // Ejecutamos la validación
        boolean containsbadwords = badWordsService.containsBadWords(content);

        // Verificamos que el resultado es inválido (con malas palabras)
        assertTrue(containsbadwords);
    }

    @Test
    public void testValidateContent_apiError() {
        // Simulamos un error en la API (por ejemplo, un timeout o un error de red)
        String content = "This is a message";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new RuntimeException("API error"));

        // Ejecutamos la validación
        boolean containsbadwords = badWordsService.containsBadWords(content);

        // Verificamos que, en caso de error, el contenido sea considerado válido
        assertFalse(containsbadwords); 
    }

    @Test
    public void testValidateContent_emptyContent() {
        // Simulamos una respuesta de la API con un contenido vacío
        String content = "";
        String jsonResponse = "{\"result\":\"\"}";  // La respuesta indica que no tiene malas palabras
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Ejecutamos la validación
        boolean containsbadwords = badWordsService.containsBadWords(content);

        // Verificamos que el resultado es válido (sin malas palabras)
        assertFalse(containsbadwords);
    }

    

}
