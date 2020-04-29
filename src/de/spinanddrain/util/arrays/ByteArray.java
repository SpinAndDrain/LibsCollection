package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class ByteArray implements Array<Byte> {

	private Byte[] packet;
	
	public ByteArray() {
		this.packet = new Byte[0];
	}
	
	public ByteArray(byte... bytes) {
		this.packet = new Byte[bytes.length];
		for(int i = 0; i < bytes.length; i++) {
			this.packet[i] = bytes[i];
		}
	}
	
	public ByteArray(Byte... bytes) {
		this.packet = bytes;
	}
	
	@Override
	public ByteArray add(Byte object) {
		Byte[] copy = this.packet;
		this.packet = new Byte[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public ByteArray remove(int index) {
		Byte[] copy = this.packet;
		this.packet = new Byte[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public ByteArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public ByteArray unshift(Byte object) {
		Byte[] copy = this.packet;
		this.packet = new Byte[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public ByteArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public ByteArray move(int s, int t) {
		Byte[] copy = this.packet;
		this.packet = new Byte[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public ByteArray fill(Byte[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public ByteArray override(Byte[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public ByteArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public ByteArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public ByteArray keep(Filter<Byte> filter) {
		byte[] copy = new byte[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Byte[] result = new Byte[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public ByteArray eliminate(Filter<Byte> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public ByteArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public ByteArray clear(int newLength) {
		this.packet = new Byte[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Byte object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Byte object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Byte[] other) {
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
	public ByteArray modifyEach(NativeOperation<Byte> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public ByteArray forEach(VoidOperation<Byte> operate) {
		for(Byte i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public ByteArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public ByteArray subarray(int beginIndex, int endIndex) {
		Byte[] copy = new Byte[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new ByteArray(copy);
	}
	
	@Override
	public ByteArray copy() {
		ByteArray copy = new ByteArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public ByteArray insert(Byte object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public ByteArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public ByteArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public ByteArray sort() {
		ByteArray copy = this.copy();
		Byte[] sorted = new Byte[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			byte lowest = (byte) MathUtils.getSmallestOf(ArrayCaster.convertToLong(ArrayCaster.cast(copy.packet)));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Byte get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Byte[] toArray() {
		return this.packet;
	}
	
	public byte[] toNativeArray() {
		byte[] nat = new byte[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}

	@Override
	public List<Byte> toList() {
		return Arrays.asList(this.packet);
	}

}
