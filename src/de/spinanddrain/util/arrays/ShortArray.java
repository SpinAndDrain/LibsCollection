package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class ShortArray implements Array<Short> {

	private Short[] packet;
	
	public ShortArray() {
		this.packet = new Short[0];
	}
	
	public ShortArray(short... shorts) {
		this.packet = new Short[shorts.length];
		for(int i = 0; i < shorts.length; i++) {
			this.packet[i] = shorts[i];
		}
	}
	
	public ShortArray(Short... shorts) {
		this.packet = shorts;
	}
	
	@Override
	public ShortArray add(Short object) {
		Short[] copy = this.packet;
		this.packet = new Short[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public ShortArray remove(int index) {
		Short[] copy = this.packet;
		this.packet = new Short[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public ShortArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public ShortArray unshift(Short object) {
		Short[] copy = this.packet;
		this.packet = new Short[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public ShortArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public ShortArray move(int s, int t) {
		Short[] copy = this.packet;
		this.packet = new Short[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public ShortArray fill(Short[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public ShortArray override(Short[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public ShortArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public ShortArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public ShortArray keep(Filter<Short> filter) {
		short[] copy = new short[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Short[] result = new Short[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public ShortArray eliminate(Filter<Short> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public ShortArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public ShortArray clear(int newLength) {
		this.packet = new Short[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Short object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Short object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Short[] other) {
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
	public ShortArray modifyEach(NativeOperation<Short> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public ShortArray forEach(VoidOperation<Short> operate) {
		for(Short i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public ShortArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public ShortArray subarray(int beginIndex, int endIndex) {
		Short[] copy = new Short[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new ShortArray(copy);
	}
	
	@Override
	public ShortArray copy() {
		ShortArray copy = new ShortArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public ShortArray insert(Short object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public ShortArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public ShortArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public ShortArray sort() {
		ShortArray copy = this.copy();
		Short[] sorted = new Short[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			short lowest = (short) MathUtils.getSmallestOf(ArrayCaster.convertToLong(ArrayCaster.cast(copy.packet)));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Short get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Short[] toArray() {
		return this.packet;
	}

	public short[] toNativeArray() {
		short[] nat = new short[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}
	
	@Override
	public List<Short> toList() {
		return Arrays.asList(this.packet);
	}

}
