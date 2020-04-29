package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class BooleanArray implements Array<Boolean> {

	private Boolean[] packet;
	
	public BooleanArray() {
		this.packet = new Boolean[0];
	}
	
	public BooleanArray(boolean... bools) {
		this.packet = new Boolean[bools.length];
		for(int i = 0; i < bools.length; i++) {
			this.packet[i] = bools[i];
		}
	}
	
	public BooleanArray(Boolean... bools) {
		this.packet = bools;
	}
	
	@Override
	public BooleanArray add(Boolean object) {
		Boolean[] copy = this.packet;
		this.packet = new Boolean[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public BooleanArray remove(int index) {
		Boolean[] copy = this.packet;
		this.packet = new Boolean[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public BooleanArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public BooleanArray unshift(Boolean object) {
		Boolean[] copy = this.packet;
		this.packet = new Boolean[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public BooleanArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public BooleanArray move(int s, int t) {
		Boolean[] copy = this.packet;
		this.packet = new Boolean[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public BooleanArray fill(Boolean[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public BooleanArray override(Boolean[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public BooleanArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public BooleanArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public BooleanArray keep(Filter<Boolean> filter) {
		boolean[] copy = new boolean[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Boolean[] result = new Boolean[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public BooleanArray eliminate(Filter<Boolean> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public BooleanArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public BooleanArray clear(int newLength) {
		this.packet = new Boolean[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Boolean object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Boolean object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Boolean[] other) {
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
	public BooleanArray modifyEach(NativeOperation<Boolean> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public BooleanArray forEach(VoidOperation<Boolean> operate) {
		for(Boolean i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public BooleanArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public BooleanArray subarray(int beginIndex, int endIndex) {
		Boolean[] copy = new Boolean[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new BooleanArray(copy);
	}
	
	@Override
	public BooleanArray copy() {
		BooleanArray copy = new BooleanArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public BooleanArray insert(Boolean object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public BooleanArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++)
			this.move(i, this.packet.length - i - 1);
		return this;
	}
	
	@Override
	public BooleanArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public BooleanArray sort() {
		ByteArray sorted = this.toBytes().sort();
		this.clear();
		sorted.forEach(element -> this.add(element == 1 ? true : false));
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Boolean get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Boolean[] toArray() {
		return this.packet;
	}

	/**
	 * 
	 * @return the values of this array as a <code>ByteArray</code>
	 */
	public ByteArray toBytes() {
		ByteArray res = new ByteArray();
		this.forEach(element -> res.add((byte) (element ? 1 : 0)));
		return res;
	}
	
	public boolean[] toNativeArray() {
		boolean[] nat = new boolean[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}
	
	@Override
	public List<Boolean> toList() {
		return Arrays.asList(this.packet);
	}

}
