package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import com.nerdysoft.competencymatrix.entity.dto.EvaluatorProgressAccessDto;
import com.nerdysoft.competencymatrix.entity.dto.ManagerCompetencyAccessDto;
import com.nerdysoft.competencymatrix.service.access.EvaluatorProgressService;
import com.nerdysoft.competencymatrix.service.access.ManagerCompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/access")
public class AccessController {

    private final EvaluatorProgressService evaluatorProgressService;
    private final ManagerCompetencyService managerCompetencyService;

    @Autowired
    public AccessController(EvaluatorProgressService evaluatorProgressService, ManagerCompetencyService managerCompetencyService) {
        this.evaluatorProgressService = evaluatorProgressService;
        this.managerCompetencyService = managerCompetencyService;
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @PostMapping("/evaluator")
    public ResponseEntity<EvaluatorProgressAccessDto> addAccessForEvaluator(EvaluatorProgressAccess access) {
        EvaluatorProgressAccess evaluatorProgressAccess = evaluatorProgressService.addAccess(access);
        return new ResponseEntity<>(EvaluatorProgressAccessDto.from(evaluatorProgressAccess), OK);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @DeleteMapping("/evaluator/{id}")
    public ResponseEntity<Boolean> deleteAccessForEvaluator(@PathVariable Long id){
        boolean isDeleted = evaluatorProgressService.deleteAccess(id);

        if (isDeleted){
            return new ResponseEntity<>(true, OK);
        }else
            return new ResponseEntity<>(false, NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_CHECK_ACCESS')")
    @GetMapping("/evaluator")
    public ResponseEntity<List<EvaluatorProgressAccessDto>> getAccessesForEvaluator(@AuthenticationPrincipal UserDetails user) {
        List<EvaluatorProgressAccess> accessForEvaluator = evaluatorProgressService.getAccessForEvaluator(user);

        List<EvaluatorProgressAccessDto> dtos = accessForEvaluator.stream().map(EvaluatorProgressAccessDto::from).collect(Collectors.toList());
        if (dtos.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(dtos, OK);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @PostMapping("/manager")
    public ResponseEntity<ManagerCompetencyAccessDto> addAccessForManager(ManagerCompetencyAccess access) {
        ManagerCompetencyAccess managerCompetencyAccess = managerCompetencyService.addAccess(access);
        return new ResponseEntity<>(ManagerCompetencyAccessDto.from(managerCompetencyAccess), OK);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @DeleteMapping("/manager/{id}")
    public ResponseEntity<Boolean> deleteAccessForManager(@PathVariable Long id){
        boolean isDeleted = managerCompetencyService.deleteAccess(id);

        if (isDeleted){
            return new ResponseEntity<>(true, OK);
        }else
            return new ResponseEntity<>(false, NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_CHECK_ACCESS')")
    @GetMapping("/manager")
    public ResponseEntity<List<ManagerCompetencyAccessDto>> getAccessesForManager(@AuthenticationPrincipal UserDetails user) {
        List<ManagerCompetencyAccess> accessForManager = managerCompetencyService.getAccessForManager(user);

        List<ManagerCompetencyAccessDto> dtos = accessForManager.stream().map(ManagerCompetencyAccessDto::from).collect(Collectors.toList());
        if (dtos.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(dtos, OK);
    }
}
