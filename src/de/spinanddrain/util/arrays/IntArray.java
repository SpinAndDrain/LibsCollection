package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class IntArray implements Array<Integer> {

	private Integer[] packet;
	
	public IntArray() {
		this.packet = new Integer[0];
	}
	
	public IntArray(int... integers) {
		this.packet = new Integer[integers.length];
		for(int i = 0; i < integers.length; i++) {
			this.packet[i] = (Integer) integers[i];
		}
	}
	
	public IntArray(Integer... integers) {
		this.packet = integers;
	}
	
	@Override
	public IntArray add(Integer object) {
		Integer[] copy = this.packet;
		this.packet = new Integer[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public IntArray remove(int index) {
		Integer[] copy = this.packet;
		this.packet = new Integer[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public IntArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public IntArray unshift(Integer object) {
		Integer[] copy = this.packet;
		this.packet = new Integer[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public IntArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public IntArray move(int s, int t) {
		Integer[] copy = this.packet;
		this.packet = new Integer[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public IntArray fill(Integer[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public IntArray override(Integer[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public IntArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public IntArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public IntArray keep(Filter<Integer> filter) {
		int[] copy = new int[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Integer[] result = new Integer[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public IntArray eliminate(Filter<Integer> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public IntArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public IntArray clear(int newLength) {
		this.packet = new Integer[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Integer object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Integer object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Integer[] other) {
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
	public IntArray modifyEach(NativeOperation<Integer> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public IntArray forEach(VoidOperation<Integer> operate) {
		for(Integer i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public IntArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public IntArray subarray(int beginIndex, int endIndex) {
		Integer[] copy = new Integer[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new IntArray(copy);
	}
	
	@Override
	public IntArray copy() {
		IntArray copy = new IntArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public IntArray insert(Integer object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public IntArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public IntArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public IntArray sort() {
		IntArray copy = this.copy();
		Integer[] sorted = new Integer[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			int lowest = (int) MathUtils.getSmallestOf(ArrayCaster.convertToLong(ArrayCaster.cast(copy.packet)));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Integer get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Integer[] toArray() {
		return this.packet;
	}

	public int[] toNativeArray() {
		int[] nat = new int[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}
	
	@Override
	public List<Integer> toList() {
		return Arrays.asList(this.packet);
	}

}
