package isi.dan.mspago.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isi.dan.mspago.dto.PagoDtoForDecision;

@RestController
@RequestMapping("api/pago")
public class PagoController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PagoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/realizarPago")
    public String realizarPago(@RequestParam String numeroPedido, @RequestParam Integer idUsuario, @RequestParam String decision) {
        PagoDtoForDecision pago = new PagoDtoForDecision(numeroPedido, idUsuario, decision);
        rabbitTemplate.convertAndSend("exchange.pedidos", "routingKey.pedidos", pago);
        
        return "Pago en proceso.";
    }

    // Método para recibir la respuesta del microservicio de pedidos
    @RabbitListener(queues = "respuesta.pedidos")
    public void recibirRespuestaPedido(String mensaje) {
        // Aquí puedes implementar la lógica para manejar la respuesta del microservicio de pedidos
        // Por ejemplo, verificar si el pedido se procesó correctamente y actuar en consecuencia
    }
}
