package br.gov.ce.esp.processoseletivo.listeners;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;


public class VerificarAssinaturaListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution)  {
		
		try {											
            
			PublicKey pubKey = (PublicKey)execution.getVariable("pubKey");
			String senhaAssinatura = (String)execution.getVariable("senhaAssinatura");
			byte[] assinatura = (byte[])execution.getVariable("assinatura");
			
			System.out.println("Public Key: " + pubKey.toString());
			System.out.println("Senha Assinatura: " + senhaAssinatura.toString());
			System.out.println("Assinatura: " + assinatura.toString());
			
			Signature clientSig = Signature.getInstance("DSA");
			clientSig.initVerify(pubKey);
			clientSig.update(senhaAssinatura.getBytes());

	       if (clientSig.verify(assinatura)) {
	           execution.setVariable("processoAssinadoCorretamente", true);
	       } else {
	           //Mensagem não pode ser validada
	    	   execution.setVariable("processoAssinadoCorretamente", false);
	       }
			
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
