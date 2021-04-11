package by.stepanov.navy.controller;

import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.service.SeamanService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/seamen", produces = "application/json")
@Api(value = "/seamen", tags = {"Seamen"})
public class SeamanController {

    private SeamanService seamanService;

    @Autowired
    public SeamanController(SeamanService seamanService) {
        this.seamanService = seamanService;
    }

    @GetMapping
    @ApiOperation(
            value = "Get existing seamen (filtration possible)",
            httpMethod = "GET",
            produces = "application/json",
            response = Seaman.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect seaman role or ship id"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<List<Seaman>> getAllSeamen(
            @ApiParam(
                    value = "Seaman role",
                    name = "role",
                    allowableValues = "CAPTAIN, SAILOR",
                    example = "CAPTAIN"
            )
            @RequestParam(value = "role", required = false) final  String seamanRole,
            @ApiParam(
                    value = "Ship id",
                    name = "shipId",
                    example = "1"
            )
            @RequestParam(value = "shipId", required = false) final Long shipId
    ) {
        return seamanService.reaAllSeamen(seamanRole, shipId);
    }

    @PostMapping
    @ApiOperation(
            value = "Create new seaman",
            httpMethod = "POST",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect seaman name or seaman role or seaman has ship id"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<String> postSeaman(
            @ApiParam(
                    value = "JSON-structure of seaman",
                    name = "seaman",
                    required = true
            )
            @RequestBody final Seaman seaman
    ){
        return seamanService.createSeaman(seaman);
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Read seaman by id",
            httpMethod = "GET",
            produces = "application/json",
            response = Seaman.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect seaman id"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<Seaman> getSeaman(
            @ApiParam(
                    value = "Seaman id",
                    name = "id",
                    example = "1",
                    required = true
            )
            @PathVariable(value = "id") final long seamanId
    ) {
        return seamanService.readSeaman(seamanId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Delete seaman by id",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Seaman not found or seaman's ship not found"),
            @ApiResponse(code = 422, message = "Seaman in the sea can't deleting"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<String> deleteSeaman(
            @ApiParam(
                    value = "Seaman id",
                    name = "id",
                    example = "5",
                    required = true
            )
            @PathVariable(value = "id") long seamanId
    ) {
        return seamanService.deleteSeaman(seamanId);
    }

    @PutMapping("/{id}/role")
    @ApiOperation(
            value = "Change seaman role",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect seaman role"),
            @ApiResponse(code = 404, message = "Seaman not found"),
            @ApiResponse(code = 422, message = "Seaman or ship in the sea or ship already has the  captain"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<String> putSeamanRole(
            @ApiParam(
                    value = "seaman id",
                    name = "id",
                    required = true,
                    example = "10"
            )
            @PathVariable(value = "id") final long seamanId,
            @ApiParam(
                    value = "seaman role",
                    name = "seaman_role",
                    required = true,
                    allowableValues = "CAPTAIN, SAILOR",
                    example = "CAPTAIN"
            )
            @RequestParam(value = "seaman_role") String seamanRole
    ){
        return seamanService.updateSeamanRole(seamanId, seamanRole);
    }

    @PutMapping("/{id}/ship")
    @ApiOperation(
            value = "Change seaman ship",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Seaman or ship not found"),
            @ApiResponse(code = 422, message = "Ship is full, captain already on board, ship in the sea, seaman in the sea"),
            @ApiResponse(code = 500, message = "Internal service error")})
    ResponseEntity<String> putSeamanShipId(
            @ApiParam(
                    value = "seaman id",
                    name = "id",
                    required = true,
                    example = "10"
            )
            @PathVariable(value = "id") final long seamanId,
            @ApiParam(
                    value = "seaman ship",
                    name = "ship_id",
                    example = "2"
            )
            @RequestParam(value = "ship_id", required = false) final Long shipId
    ){
        return seamanService.updateSeamanShip(seamanId, shipId);
    }
}
