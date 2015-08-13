package dash.tran;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.Hour;
import dash.pojo.Message;
import dash.pojo.Task;
import dash.service.HourService;
import dash.service.MessageService;

public class MessageTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public Long createMessage1(Message message, Task task, int ds, MessageService messageService) throws AppException {
		return messageService.createMessage(message, task, ds);
	}

	@Transactional("transactionManagerVMA")
	public Long createMessage2(Message message, Task task, int ds,  MessageService messageService) throws AppException {
		return messageService.createMessage(message, task, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteMessage1(Message message, int ds, MessageService messageService) {
		messageService.deleteMessage(message, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void deleteMessage2(Message message, int ds, MessageService messageService) {
		messageService.deleteMessage(message, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updatePartiallyMessage1(Message message, Task task, int ds, MessageService messageService) throws AppException {
		messageService.updatePartiallyMessage(message, task, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void updatePartiallyMessage2(Message message, Task task, int ds, MessageService messageService) throws AppException {
		messageService.updatePartiallyMessage(message, task, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updateFullyMessage1(Message message, int ds, MessageService messageService) throws AppException {
		messageService.updateFullyMessage(message, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void updateFullyMessage2(Message message, int ds, MessageService messageService) throws AppException {
		messageService.updateFullyMessage(message, ds);
	}
}
