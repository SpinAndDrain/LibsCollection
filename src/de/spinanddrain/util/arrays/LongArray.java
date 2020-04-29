package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class LongArray implements Array<Long> {

	private Long[] packet;
	
	public LongArray() {
		this.packet = new Long[0];
	}
	
	public LongArray(long... longs) {
		this.packet = new Long[longs.length];
		for(int i = 0; i < longs.length; i++) {
			this.packet[i] = longs[i];
		}
	}
	
	public LongArray(Long... longs) {
		this.packet = longs;
	}
	
	@Override
	public LongArray add(Long object) {
		Long[] copy = this.packet;
		this.packet = new Long[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public LongArray remove(int index) {
		Long[] copy = this.packet;
		this.packet = new Long[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public LongArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public LongArray unshift(Long object) {
		Long[] copy = this.packet;
		this.packet = new Long[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public LongArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public LongArray move(int s, int t) {
		Long[] copy = this.packet;
		this.packet = new Long[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public LongArray fill(Long[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public LongArray override(Long[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public LongArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public LongArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public LongArray keep(Filter<Long> filter) {
		long[] copy = new long[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Long[] result = new Long[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public LongArray eliminate(Filter<Long> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public LongArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public LongArray clear(int newLength) {
		this.packet = new Long[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Long object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Long object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Long[] other) {
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
	public LongArray modifyEach(NativeOperation<Long> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public LongArray forEach(VoidOperation<Long> operate) {
		for(Long i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public LongArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public LongArray subarray(int beginIndex, int endIndex) {
		Long[] copy = new Long[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new LongArray(copy);
	}
	
	@Override
	public LongArray copy() {
		LongArray copy = new LongArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public LongArray insert(Long object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public LongArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public LongArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public LongArray sort() {
		LongArray copy = this.copy();
		Long[] sorted = new Long[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			long lowest = MathUtils.getSmallestOf(ArrayCaster.cast(copy.packet));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Long get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Long[] toArray() {
		return this.packet;
	}
	
	public long[] toNativeArray() {
		long[] nat = new long[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}

	@Override
	public List<Long> toList() {
		return Arrays.asList(this.packet);
	}

}
