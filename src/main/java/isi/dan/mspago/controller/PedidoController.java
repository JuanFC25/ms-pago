package isi.dan.mspago.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/pedido")
public class PedidoController {


    @GetMapping
    public ResponseEntity <String> buscar(){
        // TODO : Busquedas
        return ResponseEntity.ok().body("sasasa");
        //return ResponseEntity.ok().body(repo.findAll());
    }
    

}
