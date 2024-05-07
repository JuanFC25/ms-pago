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
import isi.dan.mspago.dto.mensajeprocesado.MensajeProcesadoDto;

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
    public void recibirRespuestaPedido(String mensajeJson) {
        try {
            System.out.println("Respuesta recibida: " + mensajeJson);
            
            // Convertir el mensaje JSON a un objeto PagoDtoForDecision
            MensajeProcesadoDto mensaje = objectMapper.readValue(mensajeJson, MensajeProcesadoDto.class);
            
            // Extraer la información relevante del objeto PagoDtoForDecision
            String decision = mensaje.getDecision();
            String numeroPedido = mensaje.getNumeroPedido();
            String correoElectronico = mensaje.getEmail();

            // Enviar un correo electrónico con la decisión de pago
            
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante el procesamiento del mensaje
        }
    }
}
    

