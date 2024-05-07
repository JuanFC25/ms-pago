package isi.dan.mspago.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.mspago.dto.PagoDtoForDecision;

@RestController
@RequestMapping("api/pago")
public class PagoController {

    private final RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PagoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/realizarPago")
    public String realizarPago(@RequestParam String numeroPedido, @RequestParam Integer idUsuario, @RequestParam String decision) {
          // Crear un nuevo objeto PagoDtoForDecision
        PagoDtoForDecision pago = new PagoDtoForDecision(numeroPedido, idUsuario, decision);
        
        try {
            // Convertir el objeto a JSON
            String pagoJson = objectMapper.writeValueAsString(pago);
            
            // Enviar el JSON a través de RabbitMQ
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            
            return "Pago en proceso.";
        } catch (JsonProcessingException e) {
            // Manejar cualquier excepción de procesamiento JSON
            e.printStackTrace();
            return "Error al procesar el pago.";
        }
    }

    // Método para recibir la respuesta del microservicio de pedidos
    @RabbitListener(queues = "respuesta.pedidos")
    public void recibirRespuestaPedido(String mensaje) {
        System.out.println("Hola, mundo!");
    }
}
