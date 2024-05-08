package isi.dan.mspago.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import isi.dan.mspago.dto.PagoDtoForDecision;
import isi.dan.mspago.dto.mensajeprocesado.MensajeProcesadoDto;

@RestController
@RequestMapping("api/pago")
@CrossOrigin(origins = "http://localhost:3001")
public class PagoController {

    private final RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PagoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/realizarPago")
    public HashMap<String, String>  realizarPago(@RequestParam String idPedido, @RequestParam Integer idUsuario, @RequestParam String decision) {
          // Crear un nuevo objeto PagoDtoForDecision
        PagoDtoForDecision pago = new PagoDtoForDecision(idPedido, idUsuario, decision);
        
        try {
            // Convertir el objeto a JSON
            String pagoJson = objectMapper.writeValueAsString(pago);
            
            // Enviar el JSON a través de RabbitMQ
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Pago en proceso.");
            return response;
            // return  new JSONObject()
            // "Pago en proceso."JSONParser();
        } catch (JsonProcessingException e) {
            // Manejar cualquier excepción de procesamiento JSON
            e.printStackTrace();
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Pago en proceso.");
            return response;
        }
    }

    // Método para recibir la respuesta del microservicio de pedidos
    @RabbitListener(queues = "respuesta.pedidos")
    public void recibirRespuestaPedido(String mensajeJson) {
        try {
            System.out.println("Respuesta recibida: " + mensajeJson);
            
            // Convertir el mensaje JSON a un objeto PagoDtoForDecision
            MensajeProcesadoDto mensaje = objectMapper.readValue(mensajeJson, MensajeProcesadoDto.class);
            
            // Extraer la información relevante del objeto PagoDtoForDecision
            String decision = mensaje.getDecision();
            String idPedido = mensaje.getIdPedido();
            String correoElectronico = mensaje.getEmail();

            // Enviar un correo electrónico con la decisión de pago
            
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante el procesamiento del mensaje
        }
    }
}
    

