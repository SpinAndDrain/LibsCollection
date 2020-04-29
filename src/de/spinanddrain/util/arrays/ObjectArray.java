package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class ObjectArray implements Array<Object> {

	/*
	 * Created by SpinAndDrain on 11.03.2020
	 */

	private Object[] packet;
	
	public ObjectArray() {
		this.packet = new Object[0];
	}
	
	public ObjectArray(Object[] priorContent) {
		this.packet = priorContent;
	}
	
	@Override
	public ObjectArray add(Object line) {
		Object[] copy = this.packet;
		this.packet = new Object[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = line;
		return this;
	}

	@Override
	public ObjectArray unshift(Object line) {
		Object[] copy = this.packet;
		this.packet = new Object[copy.length + 1];
		this.packet[0] = line;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}
	
	@Override
	public ObjectArray remove(int index) {
		Object[] copy = this.packet;
		this.packet = new Object[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}
	
	@Override
	public ObjectArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}
	
	@Override
	public ObjectArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}
	
	@Override
	public ObjectArray move(int stable, int target) {
		Object[] copy = this.packet;
		this.packet = new Object[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == stable ? copy[target] : i == target ? copy[stable] : copy[i]);
		}
		return this;
	}
	
	@Override
	public ObjectArray fill(Object[] lines) {
		for(int i = 0; i < lines.length; i++) {
			this.add(lines[i]);
		}
		return this;
	}
	
	
	@Override
	public ObjectArray override(Object[] lines) {
		this.packet = lines;
		return this;
	}

	@Override
	public ObjectArray keep(Filter<Object> filter) {
		Object[] copy = new Object[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Object[] result = new Object[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public ObjectArray eliminate(Filter<Object> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public ObjectArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public ObjectArray clear(int newLength) {
		this.packet = new Object[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Object object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Object object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Object[] other) {
		for(int i = 0; i < this.packet.length; i++) {
			if(!this.packet[i].equals(other[i])) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return this.packet.length < 1;
	}
	
	@Override
	public ObjectArray modifyEach(NativeOperation<Object> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public ObjectArray forEach(VoidOperation<Object> operate) {
		for(Object i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public ObjectArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public ObjectArray subarray(int beginIndex, int endIndex) {
		Object[] copy = new Object[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new ObjectArray(copy);
	}
	
	@Override
	public ObjectArray copy() {
		ObjectArray copy = new ObjectArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public ObjectArray insert(Object object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public ObjectArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public ObjectArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	/**
	 * 
	 * @deprecated cannot sort non-primitive types
	 */
	@Deprecated
	@Override
	public ObjectArray sort() {
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Object get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Object[] toArray() {
		return packet;
	}
	
	@Override
	public List<Object> toList() {
		return Arrays.asList(this.packet);
	}
	
	@Override
	public ObjectArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}
	
	@Override
	public ObjectArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < packet.length; i++) {
				this.move(packet.length - 1, packet.length - 1 - i);
			}
		}
		return this;
	}
	
}
