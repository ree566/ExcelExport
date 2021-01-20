/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.RequisitionListContainer;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Requisition;
import com.advantech.model.db1.RequisitionEvent;
import com.advantech.model.db1.RequisitionEvent_;
import com.advantech.model.db1.RequisitionReason;
import com.advantech.model.db1.RequisitionState;
import com.advantech.model.db1.RequisitionState_;
import com.advantech.model.db1.RequisitionType;
import com.advantech.model.db1.Requisition_;
import com.advantech.model.db1.User;
import com.advantech.model.db1.User_;
import com.advantech.service.db1.RequisitionEventService;
import com.advantech.service.db1.RequisitionReasonService;
import com.advantech.service.db1.RequisitionService;
import com.advantech.service.db1.RequisitionStateService;
import com.advantech.service.db1.RequisitionTypeService;
import com.fasterxml.jackson.annotation.JsonView;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.advantech.sap.SapQueryPort;
import com.advantech.webservice.port.QryWipAttQueryPort;
import com.google.common.base.CharMatcher;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

/**
 *
 * @author Wei.Cheng
 */
@RestController
@RequestMapping("/RequisitionController")
public class RequisitionController {

    @Autowired
    private RequisitionService service;

    @Autowired
    private RequisitionEventService eventService;

    @Autowired
    private RequisitionReasonService requisitionReasonService;

    @Autowired
    private RequisitionTypeService requisitionTypeService;

    @Autowired
    private RequisitionStateService requisitionStateService;

    @Autowired
    private SapQueryPort port;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.POST})
    protected DataTablesOutput<Requisition> findAll(
            HttpServletRequest request,
            @Valid DataTablesInput input,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        Floor floor = user.getFloor();

        if (startDate != null && endDate != null) {
            final Date sD = startDate.toDate();
            final Date eD = endDate.withHourOfDay(23).toDate();

            return service.findAll(input, (Root<Requisition> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
                Path<Date> dateEntryPath = root.get(Requisition_.createDate);
                if (request.isUserInRole("ROLE_ADMIN")) {
                    return cb.between(dateEntryPath, sD, eD);
                } else {
                    Join<Requisition, User> userJoin = root.join(Requisition_.user, JoinType.INNER);
                    return cq.where(cb.and(cb.between(dateEntryPath, sD, eD), cb.equal(userJoin.get(User_.FLOOR), floor))).getRestriction();
                }
            });
        } else {
            if (request.isUserInRole("ROLE_ADMIN")) {
                return service.findAll(input);
            } else {
                return service.findAll(input, (Root<Requisition> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
                    Join<Requisition, User> userJoin = root.join(Requisition_.user, JoinType.INNER);
                    return cb.equal(userJoin.get(User_.FLOOR), floor);
                });
            }
        }

    }

    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    protected String save(@ModelAttribute Requisition requisition, @RequestParam(required = false) String remark, BindingResult bindingResult) throws Exception {

        bindingResult.getAllErrors().stream().map((object) -> {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;

                System.out.println(fieldError.getCode());
            }
            return object;
        }).filter((object) -> (object instanceof ObjectError)).map((object) -> (ObjectError) object).forEachOrdered((objectError) -> {
            System.out.println(objectError.getCode());
        });

        checkModelMaterial(newArrayList(requisition));

        service.save(requisition, remark);
        return "success";

    }

    private void checkModelMaterial(List<Requisition> requisitions) throws Exception {
        if (!requisitions.isEmpty()) {
            String po = requisitions.get(0).getPo();
            JCoFunction function = port.getMaterialInfo(po);
            JCoTable table = function.getTableParameterList().getTable("ZWODETAIL");//调用接口返回结果

            requisitions.forEach((r) -> {
                boolean checkFlag = false;
                String checkMt = r.getMaterialNumber();
                for (int i = 0; i < table.getNumRows(); i++) {
                    table.setRow(i);
                    String material = table.getString("MATNR");
                    material = CharMatcher.is('0').trimLeadingFrom(material);
                    if (material.equals(checkMt)) {
                        checkFlag = true;
                        break;
                    }
                }

                checkArgument(checkFlag == true, "Can't find material info " + checkMt + " in po " + po);
            });
        }
    }

    @ResponseBody
    @RequestMapping(value = "/batchSave", method = {RequestMethod.POST})
    protected String batchSave(@ModelAttribute RequisitionListContainer container) throws Exception {
        checkModelMaterial(container.getMyList());
        service.batchInsert(container.getMyList());
        return "success";

    }

    @ResponseBody
    @RequestMapping(value = "/updateState", method = {RequestMethod.POST})
    protected String updateState(@RequestParam int requisition_id, @RequestParam int state_id,
            @RequestParam(required = false) String remark) {

        service.changeState(requisition_id, state_id);
        return "success";

    }

    @ResponseBody
    @RequestMapping(value = "/findEvent", method = {RequestMethod.POST})
    protected DataTablesOutput<RequisitionEvent> findEvent(@Valid DataTablesInput input, @RequestParam int requisition_id) {
        Requisition re = service.findById(requisition_id).get();
        return eventService.findAll(input, (Root<RequisitionEvent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Path<Integer> idEntryPath = root.get(RequisitionEvent_.REQUISITION);
            return cb.equal(idEntryPath, re);
        });
    }

    @ResponseBody
    @RequestMapping(value = "/findRequisitionReasonOptions", method = {RequestMethod.GET})
    protected List<RequisitionReason> findRequisitionReasonOptions() {
        return requisitionReasonService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/findRequisitionStateOptions", method = {RequestMethod.GET})
    protected List<RequisitionState> findRequisitionStateOptions() {
        return requisitionStateService.findAll((Root<RequisitionState> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Path<Integer> idEntryPath = root.get(RequisitionState_.ID);
            return cb.not(idEntryPath.in(newArrayList(1, 3, 4)));
        });
    }

    @ResponseBody
    @RequestMapping(value = "/findRequisitionTypeOptions", method = {RequestMethod.GET})
    protected List<RequisitionType> findRequisitionTypeOptions() {
        return requisitionTypeService.findAll();
    }
}
