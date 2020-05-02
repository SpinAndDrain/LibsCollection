package de.spinanddrain.net.connection.packet;

import java.io.Serializable;
import java.util.ArrayList;

import de.spinanddrain.util.arrays.ArrayUtils;

public class Packet extends ArrayList<Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5305919933485806971L;

	private String id;
	public String signature;
	
	/**
	 * Creates a new packet with the specified id
	 * and no content.
	 * 
	 * @param id packet id
	 */
	public Packet(String id) {
		this(id, new Object[0]);
	}
	
	/**
	 * Creates a new packet with the specified id
	 * and content.
	 * 
	 * @param id packet id
	 * @param objs content
	 */
	public Packet(String id, Object... objs) {
		this.id = id;
		addAll(ArrayUtils.modify(objs).toList());
	}
	
	/**
	 * 
	 * @return the packet's id
	 */
	public String getId() {
		return id;
	}
	
}
