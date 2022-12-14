package work.gaigeshen.flowable.demo.simple;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author gaigeshen
 */
public class HolidayRequestBpmnModelCreator {


    static BpmnModel create() {
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        process.setExecutable(true);
        process.setId("holidayRequest");
        process.setName("Holiday Request");
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");

        FormProperty employeeProperty = new FormProperty();
        employeeProperty.setId("employee");
        employeeProperty.setVariable("employee");
        employeeProperty.setType("string");
        employeeProperty.setReadable(true);
        employeeProperty.setWriteable(true);
        employeeProperty.setRequired(true);

        FormProperty typeProperty = new FormProperty();
        typeProperty.setId("type");
        typeProperty.setVariable("type");
        typeProperty.setType("enum");
        typeProperty.setReadable(true);
        typeProperty.setWriteable(true);
        typeProperty.setRequired(true);
        FormValue event = new FormValue();
        event.setId("event");
        event.setName("event");
        FormValue sick = new FormValue();
        sick.setId("sick");
        sick.setName("sick");
        typeProperty.setFormValues(Arrays.asList(event, sick));

        FormProperty nrOfHolidaysProperty = new FormProperty();
        nrOfHolidaysProperty.setId("nrOfHolidays");
        nrOfHolidaysProperty.setVariable("nrOfHolidays");
        nrOfHolidaysProperty.setType("long");
        nrOfHolidaysProperty.setReadable(true);
        nrOfHolidaysProperty.setWriteable(true);
        nrOfHolidaysProperty.setRequired(true);

        FormProperty dateDurationProperty = new FormProperty();
        dateDurationProperty.setId("dateDuration");
        dateDurationProperty.setVariable("dateDuration");
        dateDurationProperty.setType("dateDuration");
        dateDurationProperty.setReadable(true);
        dateDurationProperty.setWriteable(true);
        dateDurationProperty.setRequired(true);

        FormProperty multiSelectionProperty = new FormProperty();
        multiSelectionProperty.setId("multiSelection");
        multiSelectionProperty.setVariable("multiSelection");
        multiSelectionProperty.setType("multiSelection");
        multiSelectionProperty.setReadable(true);
        multiSelectionProperty.setWriteable(true);
        multiSelectionProperty.setRequired(true);
        FormValue multiSelection1 = new FormValue();
        multiSelection1.setId("multiSelection1");
        multiSelection1.setName("multiSelection1");
        FormValue multiSelection2 = new FormValue();
        multiSelection2.setId("multiSelection2");
        multiSelection2.setName("multiSelection2");
        multiSelectionProperty.setFormValues(Arrays.asList(multiSelection1, multiSelection2));

        FormProperty singleSelectionProperty = new FormProperty();
        singleSelectionProperty.setId("singleSelection");
        singleSelectionProperty.setVariable("singleSelection");
        singleSelectionProperty.setType("singleSelection");
        singleSelectionProperty.setReadable(true);
        singleSelectionProperty.setWriteable(true);
        singleSelectionProperty.setRequired(true);
        FormValue singleSelection1 = new FormValue();
        singleSelection1.setId("singleSelection1");
        singleSelection1.setName("singleSelection1");
        FormValue singleSelection2 = new FormValue();
        singleSelection2.setId("singleSelection2");
        singleSelection2.setName("singleSelection2");
        singleSelectionProperty.setFormValues(Arrays.asList(singleSelection1, singleSelection2));

        FormProperty descriptionProperty = new FormProperty();
        descriptionProperty.setId("description");
        descriptionProperty.setVariable("description");
        descriptionProperty.setType("string");
        descriptionProperty.setReadable(true);
        descriptionProperty.setWriteable(true);
        descriptionProperty.setRequired(false);

        startEvent.setFormProperties(Arrays.asList(employeeProperty, typeProperty, nrOfHolidaysProperty,
                dateDurationProperty, multiSelectionProperty, singleSelectionProperty,
                descriptionProperty));
        process.addFlowElement(startEvent);

        UserTask approveTask = new UserTask();
        approveTask.setId("approveTask");
        approveTask.setName("Approve or reject request");
        approveTask.setCandidateGroups(Collections.singletonList("managers"));

        FormProperty employeePropertyClone1 = employeeProperty.clone();
        employeePropertyClone1.setWriteable(false);

        FormProperty typePropertyClone1 = typeProperty.clone();
        typePropertyClone1.setWriteable(false);

        FormProperty nrOfHolidaysPropertyClone1 = nrOfHolidaysProperty.clone();
        nrOfHolidaysPropertyClone1.setWriteable(false);

        FormProperty dateDurationProperty1 = dateDurationProperty.clone();
        dateDurationProperty1.setWriteable(false);

        FormProperty multiSelectionProperty1 = multiSelectionProperty.clone();
        multiSelectionProperty1.setWriteable(false);

        FormProperty singleSelectionProperty1 = singleSelectionProperty.clone();
        singleSelectionProperty1.setWriteable(false);

        FormProperty descriptionPropertyClone1 = descriptionProperty.clone();
        descriptionPropertyClone1.setWriteable(false);

        approveTask.setFormProperties(Arrays.asList(employeePropertyClone1, typePropertyClone1, nrOfHolidaysPropertyClone1,
                dateDurationProperty1, multiSelectionProperty1, singleSelectionProperty1,
                descriptionPropertyClone1));
        process.addFlowElement(approveTask);

        ExclusiveGateway decision = new ExclusiveGateway();
        decision.setId("decision");
        process.addFlowElement(decision);

        ServiceTask externalSystemCall = new ServiceTask();
        externalSystemCall.setId("externalSystemCall");
        externalSystemCall.setName("Enter holidays in external system");
        externalSystemCall.setImplementation("work.gaigeshen.flowable.demo.simple.CallExternalSystemDelegate");
        externalSystemCall.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);

        process.addFlowElement(externalSystemCall);

        ServiceTask sendRejectionMail = new ServiceTask();
        sendRejectionMail.setId("sendRejectionMail");
        sendRejectionMail.setName("Send out rejection email");
        sendRejectionMail.setImplementation("work.gaigeshen.flowable.demo.simple.SendRejectionMail");
        sendRejectionMail.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);

        process.addFlowElement(sendRejectionMail);

        UserTask holidayApprovedTask = new UserTask();
        holidayApprovedTask.setId("holidayApprovedTask");
        holidayApprovedTask.setName("Holiday approved");
        holidayApprovedTask.setAssignee("${employee}");

        holidayApprovedTask.setFormProperties(Arrays.asList(employeePropertyClone1, typePropertyClone1, nrOfHolidaysPropertyClone1,
                dateDurationProperty1, multiSelectionProperty1, singleSelectionProperty1,
                descriptionPropertyClone1));
        process.addFlowElement(holidayApprovedTask);

        EndEvent approveEnd = new EndEvent();
        approveEnd.setId("approveEnd");
        process.addFlowElement(approveEnd);

        EndEvent rejectEnd = new EndEvent();
        rejectEnd.setId("rejectEnd");
        process.addFlowElement(rejectEnd);

        SequenceFlow startEventToApproveTask = new SequenceFlow();
        startEventToApproveTask.setSourceRef("startEvent");
        startEventToApproveTask.setTargetRef("approveTask");
        process.addFlowElement(startEventToApproveTask);

        SequenceFlow approveTaskToDecision = new SequenceFlow();
        approveTaskToDecision.setSourceRef("approveTask");
        approveTaskToDecision.setTargetRef("decision");
        process.addFlowElement(approveTaskToDecision);

        SequenceFlow decisionToSendRejectionMail = new SequenceFlow();
        decisionToSendRejectionMail.setSourceRef("decision");
        decisionToSendRejectionMail.setTargetRef("sendRejectionMail");
        decisionToSendRejectionMail.setConditionExpression("${nrOfHolidays > 14}");
        process.addFlowElement(decisionToSendRejectionMail);

        SequenceFlow decisionToExternalSystemCall = new SequenceFlow();
        decisionToExternalSystemCall.setSourceRef("decision");
        decisionToExternalSystemCall.setTargetRef("externalSystemCall");
        decisionToExternalSystemCall.setConditionExpression("${nrOfHolidays > 1}");
        process.addFlowElement(decisionToExternalSystemCall);

        SequenceFlow externalSystemCallToHolidayApprovedTask = new SequenceFlow();
        externalSystemCallToHolidayApprovedTask.setSourceRef("externalSystemCall");
        externalSystemCallToHolidayApprovedTask.setTargetRef("holidayApprovedTask");
        process.addFlowElement(externalSystemCallToHolidayApprovedTask);

        SequenceFlow holidayApprovedTaskToApproveEnd = new SequenceFlow();
        holidayApprovedTaskToApproveEnd.setSourceRef("holidayApprovedTask");
        holidayApprovedTaskToApproveEnd.setTargetRef("approveEnd");
        process.addFlowElement(holidayApprovedTaskToApproveEnd);

        SequenceFlow sendRejectionMailToRejectEnd = new SequenceFlow();
        sendRejectionMailToRejectEnd.setSourceRef("sendRejectionMail");
        sendRejectionMailToRejectEnd.setTargetRef("rejectEnd");
        process.addFlowElement(sendRejectionMailToRejectEnd);

        return bpmnModel;
    }
}
