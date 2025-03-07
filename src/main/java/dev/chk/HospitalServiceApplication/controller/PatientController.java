package dev.chk.HospitalServiceApplication.controller;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.model.Patient;
import dev.chk.HospitalServiceApplication.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allowing CORS
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientDtoById(id));
    }

    @GetMapping("/phn/{phn}")
    public ResponseEntity<PatientDto> getPatientByPhn(@PathVariable String phn) {
        return ResponseEntity.ok(patientService.getPatientDtoByPhn(phn));
    }

    @PostMapping
    public ResponseEntity<PatientDto> addPatient(@RequestBody PatientDto patientDto) {
        PatientDto newPatient = patientService.addPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable String id, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDto));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PatientDto>> getPatientsByStatus(@PathVariable String status) {
        List<PatientDto> patients = patientService.getPatientsByStatus(status);
        return ResponseEntity.ok(patients);
    }
}
