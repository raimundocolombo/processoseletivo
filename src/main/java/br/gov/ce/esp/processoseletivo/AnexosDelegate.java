package br.gov.ce.esp.processoseletivo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.impl.value.FileValueImpl;
import org.springframework.stereotype.Component;

/**
 * This is an easy adapter implementation
 * illustrating how a Java Delegate can be used
 * from within a BPMN 2.0 Service Task.
 */
@Component("anexos")
public class AnexosDelegate implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(AnexosDelegate.class.getName());
  
  public void execute(DelegateExecution execution) throws Exception {
    
    LOGGER.info("\n\n  ... LoggerDelegate invoked by "
            + "activityName='" + execution.getCurrentActivityName() + "'"
            + ", activityId=" + execution.getCurrentActivityId()
            + ", processDefinitionId=" + execution.getProcessDefinitionId()
            + ", processInstanceId=" + execution.getProcessInstanceId()
            + ", businessKey=" + execution.getProcessBusinessKey()
            + ", executionId=" + execution.getId()
            + ", variables=" + execution.getVariables()
            + " \n\n");
    
    
    ByteArrayInputStream memorando = (ByteArrayInputStream)execution.getVariable("memorando_anexo");
    
    List<Object> listFiles = new ArrayList<Object>();
    listFiles.add(memorando);
    
    //execution.setVariable("listFiles", listFiles);
    
    
  }
  
  private File criarFile(InputStream inputStream) {
	  File file = new File("/tmp/output.txt");
      try (FileOutputStream outputStream = new FileOutputStream(file)) {
          int read;
          byte[] bytes = new byte[1024];

          while ((read = inputStream.read(bytes)) != -1) {
              outputStream.write(bytes, 0, read);
          }
      } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return file;
  }

}
