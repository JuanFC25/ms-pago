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

import isi.dan.mspago.dto.PagoDtoForDecision.PagoDtoForDecision;
import isi.dan.mspago.dto.mensajeprocesado.MensajeProcesadoDto;
import isi.dan.mspago.service.MailSenderService;

@RestController
@RequestMapping("api/pago")
public class PagoController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MailSenderService mailService;

    @Autowired
    public PagoController(RabbitTemplate rabbitTemplate, MailSenderService mailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.mailService = mailService;
    }

    @PostMapping("/realizarPago")
    public String realizarPago(@RequestParam String idPedido, @RequestParam Integer idUsuario, @RequestParam String decision) {

        PagoDtoForDecision pago = new PagoDtoForDecision(idPedido, idUsuario, decision);
        
        try {
            String pagoJson = objectMapper.writeValueAsString(pago);
            
            rabbitTemplate.convertAndSend("pedidos", "pedidos", pagoJson);
            
            return "Pago en proceso.";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error al procesar el pago.";
        }
    }

    // Método para recibir la respuesta del microservicio de pedidos
    @RabbitListener(queues = "respuesta.pedidos")
    public void recibirRespuestaPedido(String mensajeJson) {
        try {
            System.out.println("Respuesta recibida: " + mensajeJson);
            
            MensajeProcesadoDto mensaje = objectMapper.readValue(mensajeJson, MensajeProcesadoDto.class);
        
            String decision = mensaje.getDecision();
            String idPedido = mensaje.getIdPedido();
            String correoElectronico = mensaje.getEmail();

            mailService.sendNewMail(correoElectronico, "Tenemos noticias sobre el pedido "+idPedido, "El resultado fue: "+decision);
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
  
}
    

