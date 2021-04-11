package by.stepanov.navy.controller;

import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.entity.ShipStatus;
import by.stepanov.navy.service.ShipService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ships", produces = "application/json")
@Api(value = "/ships", tags = {"Ships"})
public class ShipController {

    private ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    @ApiOperation(
            value = "Get existing ships (filtration possible)",
            httpMethod = "GET",
            produces = "application/json",
            response = Ship.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect ship status"),
            @ApiResponse(code = 500, message = "Internal service error")})
    public ResponseEntity<List<Ship>> getAllShips(
            @ApiParam(
                    value = "Ship status (location)",
                    name = "status",
                    allowableValues = "SEA, PORT",
                    example = "PORT"
            )
            @RequestParam(value = "status", required = false) final String status
    ){
        return shipService.readAllShips(status);
    }

    @PostMapping
    @ApiOperation(
            value = "Create new ship",
            httpMethod = "POST",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect ship name or port id"),
            @ApiResponse(code = 404, message = "Port not found"),
            @ApiResponse(code = 422, message = "Port is full"),
            @ApiResponse(code = 500, message = "Internal service error")})
    public ResponseEntity<String> postShip(
            @ApiParam(
                    value = "JSON-structure of ship",
                    name = "ship",
                    required = true
            )
            @RequestBody final Ship ship
    ){
        return shipService.createShip(ship);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Deleting the ship",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class

    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Ship not found"),
            @ApiResponse(code = 422, message = "Ship in the sea can't deleting"),
            @ApiResponse(code = 500, message = "Internal service error")})
    public ResponseEntity<String> deleteShip(
            @ApiParam(
                    value = "ship id",
                    name = "id",
                    required = true,
                    example = "12"
            )
            @PathVariable(value = "id") final long id
    ){
        return shipService.deleteShip(id);
    }

    @GetMapping("/{id}/status")
    @ApiOperation(
            value = "Get ship location",
            httpMethod = "GET",
            produces = "application/json",
            response = ShipStatus.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Ship not found"),
            @ApiResponse(code = 500, message = "Internal service error")})
    public ResponseEntity<ShipStatus> getShipStatus(
            @PathVariable(value = "id") final long id
    ){
        return shipService.readShipStatus(id);
    }

    @PutMapping("/{id}/status")
    @ApiOperation(
            value = "Change ship location",
            httpMethod = "PUT",
            produces = "application/json",
            response = ShipStatus.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect ship status"),
            @ApiResponse(code = 404, message = "Port or ship not found"),
            @ApiResponse(code = 422, message = "Port is full or Ship has not enough team or Ship has no captain"),
            @ApiResponse(code = 500, message = "Internal service error")})
    public ResponseEntity<ShipStatus> putShipStatus(
            @ApiParam(
                    value = "ship id",
                    name = "id",
                    required = true,
                    example = "12"
            )
            @PathVariable(value = "id") final long id,
            @ApiParam(
                    value = "port id (if ship goes to the port)",
                    name = "port_id",
                    example = "3"
            )
            @RequestParam(value = "port_id", required = false) final Long portId,
            @ApiParam(
                    value = "JSON-structure of ship location",
                    name = "status",
                    required = true
            )
            @RequestBody final ShipStatus status
    ){
        return shipService.updateShipStatus(id, portId, status);
    }
}