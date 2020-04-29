package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class FloatArray implements Array<Float> {

	private Float[] packet;
	
	public FloatArray() {
		this.packet = new Float[0];
	}
	
	public FloatArray(float... floats) {
		this.packet = new Float[floats.length];
		for(int i = 0; i < floats.length; i++) {
			this.packet[i] = floats[i];
		}
	}
	
	public FloatArray(Float... floats) {
		this.packet = floats;
	}
	
	@Override
	public FloatArray add(Float object) {
		Float[] copy = this.packet;
		this.packet = new Float[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public FloatArray remove(int index) {
		Float[] copy = this.packet;
		this.packet = new Float[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public FloatArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public FloatArray unshift(Float object) {
		Float[] copy = this.packet;
		this.packet = new Float[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public FloatArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public FloatArray move(int s, int t) {
		Float[] copy = this.packet;
		this.packet = new Float[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public FloatArray fill(Float[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public FloatArray override(Float[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public FloatArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public FloatArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public FloatArray keep(Filter<Float> filter) {
		float[] copy = new float[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Float[] result = new Float[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public FloatArray eliminate(Filter<Float> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public FloatArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public FloatArray clear(int newLength) {
		this.packet = new Float[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Float object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Float object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Float[] other) {
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
	public FloatArray modifyEach(NativeOperation<Float> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public FloatArray forEach(VoidOperation<Float> operate) {
		for(Float i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public FloatArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public FloatArray subarray(int beginIndex, int endIndex) {
		Float[] copy = new Float[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new FloatArray(copy);
	}
	
	@Override
	public FloatArray copy() {
		FloatArray copy = new FloatArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public FloatArray insert(Float object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public FloatArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public FloatArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public FloatArray sort() {
		FloatArray copy = this.copy();
		Float[] sorted = new Float[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			float lowest = (float) MathUtils.getSmallestOf(ArrayCaster.convertToDouble(ArrayCaster.cast(copy.packet)));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Float get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Float[] toArray() {
		return this.packet;
	}
	
	public float[] toNativeArray() {
		float[] nat = new float[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}

	@Override
	public List<Float> toList() {
		return Arrays.asList(this.packet);
	}

}
