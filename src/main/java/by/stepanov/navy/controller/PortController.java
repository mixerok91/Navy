package by.stepanov.navy.controller;

import by.stepanov.navy.model.entity.Port;
import by.stepanov.navy.service.PortService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ports", produces = "application/json")
@Api(value = "/ports", tags = {"Ports"})
public class PortController {

    private PortService portService;

    @Autowired
    public PortController(PortService portService) {
        this.portService = portService;
    }

    @GetMapping
    @ApiOperation(
            value = "Get info about ports",
            httpMethod = "GET",
            produces = "application/json",
            response = Port.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal service error")
    })
    public ResponseEntity<List<Port>> getAllPorts() {
        return portService.readAllPorts();
    }

    @GetMapping("/{id}/capacity")
    @ApiOperation(
            value = "Get current port capacity",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Port not found"),
            @ApiResponse(code = 500, message = "Internal service error")
    })
    public ResponseEntity<String> getPortCapacityInfo(
            @ApiParam(
                    value = "Port id",
                    name = "id",
                    required = true,
                    example = "3"
            )
            @PathVariable(value = "id") final long id
    ) {
        return portService.readPortCapacityInfo(id);
    }
}