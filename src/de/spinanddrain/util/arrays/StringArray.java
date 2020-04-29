package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class StringArray implements Array<String> {

	private String[] packet;
	
	public StringArray() {
		this.packet = new String[0];
	}
	
	public StringArray(String... strings) {
		this.packet = strings;
	}
	
	@Override
	public StringArray add(String object) {
		String[] copy = this.packet;
		this.packet = new String[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public StringArray remove(int index) {
		String[] copy = this.packet;
		this.packet = new String[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public StringArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public StringArray unshift(String object) {
		String[] copy = this.packet;
		this.packet = new String[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public StringArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public StringArray move(int s, int t) {
		String[] copy = this.packet;
		this.packet = new String[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public StringArray fill(String[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public StringArray override(String[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public StringArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public StringArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public StringArray keep(Filter<String> filter) {
		String[] copy = new String[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		String[] result = new String[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public StringArray eliminate(Filter<String> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public StringArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public StringArray clear(int newLength) {
		this.packet = new String[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(String object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(String object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(String[] other) {
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
	public StringArray modifyEach(NativeOperation<String> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public StringArray forEach(VoidOperation<String> operate) {
		for(String i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public StringArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public StringArray subarray(int beginIndex, int endIndex) {
		String[] copy = new String[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new StringArray(copy);
	}
	
	@Override
	public StringArray copy() {
		StringArray copy = new StringArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public StringArray insert(String object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public StringArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public StringArray shuffle() {
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
	public StringArray sort() {
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public String get(int index) {
		return this.packet[index];
	}
	
	@Override
	public String[] toArray() {
		return this.packet;
	}

	@Override
	public List<String> toList() {
		return Arrays.asList(this.packet);
	}

}
