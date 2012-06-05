package de.htw;

import java.io.Serializable;

import de.uni_trier.jane.basetypes.Address;

public class ChatMessage implements Serializable{

	private static final long serialVersionUID = -1722709088070466350L;
	
	private String message;
	private Address sender;
	private Address destination;
	
	public ChatMessage(String message, Address sender, Address destination) {
		super();
		this.message = message;
		this.sender = sender;
		this.destination = destination;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Address getSender() {
		return sender;
	}
	public void setSender(Address sender) {
		this.sender = sender;
	}
	public Address getDestination() {
		return destination;
	}
	public void setDestination(Address destination) {
		this.destination = destination;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "ChatMessage [message=" + message + ", sender=" + sender
				+ ", destination=" + destination + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatMessage other = (ChatMessage) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}
	
	
}
