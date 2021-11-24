package br.gov.ce.esp.processoseletivo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.FileValueImpl;
import org.springframework.stereotype.Component;

import br.gov.ce.esp.processoseletivo.assinatura.DestinatarioAssinaturaDigital;
import br.gov.ce.esp.processoseletivo.assinatura.RemetenteAssinaturaDigital;

/**
 * This is an easy adapter implementation
 * illustrating how a Java Delegate can be used
 * from within a BPMN 2.0 Service Task.
 */
@Component("assinarProcesso")
public class AssinarProcessoDelegate implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(AssinarProcessoDelegate.class.getName());
  
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
    
    
    assinarAnexos(execution);
    
    
  }
  
  private void assinarAnexos(DelegateExecution execution) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
	  
	  try {
			  
		//Remetente Gera Assinatura Digital para uma Mensagem
		 RemetenteAssinaturaDigital remetenteAssiDig = new RemetenteAssinaturaDigital();
	     String senhaAssinatura = (String)execution.getVariable("senhaAssinatura");
	     
	     byte[] assinatura = remetenteAssiDig.geraAssinatura(senhaAssinatura);
	     //Guarda Chave Pública para ser Enviada ao Destinatário
	     PublicKey pubKey = remetenteAssiDig.getPubKey();

	     //Destinatário recebe dados correto
	     DestinatarioAssinaturaDigital destinatarioAssiDig = new DestinatarioAssinaturaDigital();
	     destinatarioAssiDig.recebeMensagem(pubKey, senhaAssinatura, assinatura);
	     
	     execution.setVariable("pubKey", pubKey);
	     execution.setVariable("senhaAssinatura", senhaAssinatura);
	     execution.setVariable("assinatura", assinatura);
	     
	     /*

	     //Destinatário recebe mensagem alterada
	     String msgAlterada = senhaAssinatura.concat("alterada.");
	     destinatarioAssiDig.recebeMensagem(pubKey, msgAlterada, assinatura);

	     //Criando outra Assinatura
	       String mensagem2 = "Exemplo de outra mensagem.";
	     byte[] assinatura2 = remetenteAssiDig.geraAssinatura(mensagem2);
	     //Guarda Chave Pública para ser Enviada ao Destinatário
	     PublicKey pubKey2 = remetenteAssiDig.getPubKey();

	     //Destinatário recebe outra assinatura
	     destinatarioAssiDig.recebeMensagem(pubKey, senhaAssinatura, assinatura2);

	     //Destinatário recebe outra chave pública
	     destinatarioAssiDig.recebeMensagem(pubKey2, senhaAssinatura, assinatura);
	     
	     */
	     
	  } catch (NoSuchAlgorithmException e) {
		// TODO Tratar NoSuchAlgorithmException
		  e.printStackTrace();
	  } catch (InvalidKeyException e) {
		// TODO Tratar InvalidKeyException
		e.printStackTrace();
	  } catch (SignatureException e) {
		// TODO Tratar SignatureException
		e.printStackTrace();
	  }

  }

}
