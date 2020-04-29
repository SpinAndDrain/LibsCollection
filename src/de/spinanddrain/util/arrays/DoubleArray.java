package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class DoubleArray implements Array<Double> {

	private Double[] packet;
	
	public DoubleArray() {
		this.packet = new Double[0];
	}
	
	public DoubleArray(double... doubles) {
		this.packet = new Double[doubles.length];
		for(int i = 0; i < doubles.length; i++) {
			this.packet[i] = doubles[i];
		}
	}
	
	public DoubleArray(Double... doubles) {
		this.packet = doubles;
	}
	
	@Override
	public DoubleArray add(Double object) {
		Double[] copy = this.packet;
		this.packet = new Double[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public DoubleArray remove(int index) {
		Double[] copy = this.packet;
		this.packet = new Double[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public DoubleArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public DoubleArray unshift(Double object) {
		Double[] copy = this.packet;
		this.packet = new Double[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public DoubleArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public DoubleArray move(int s, int t) {
		Double[] copy = this.packet;
		this.packet = new Double[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public DoubleArray fill(Double[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public DoubleArray override(Double[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public DoubleArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public DoubleArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public DoubleArray keep(Filter<Double> filter) {
		double[] copy = new double[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Double[] result = new Double[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public DoubleArray eliminate(Filter<Double> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public DoubleArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public DoubleArray clear(int newLength) {
		this.packet = new Double[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Double object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Double object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Double[] other) {
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
	public DoubleArray modifyEach(NativeOperation<Double> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public DoubleArray forEach(VoidOperation<Double> operate) {
		for(Double i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public DoubleArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public DoubleArray subarray(int beginIndex, int endIndex) {
		Double[] copy = new Double[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new DoubleArray(copy);
	}
	
	@Override
	public DoubleArray copy() {
		DoubleArray copy = new DoubleArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public DoubleArray insert(Double object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public DoubleArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public DoubleArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public DoubleArray sort() {
		DoubleArray copy = this.copy();
		Double[] sorted = new Double[copy.length()];
		for(int i = 0; i < this.packet.length; i++) {
			double lowest = MathUtils.getSmallestOf(ArrayCaster.cast(copy.packet));
			copy.remove(copy.firstIndexOf(lowest));
			sorted[i] = lowest;
		}
		this.packet = sorted;
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Double get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Double[] toArray() {
		return this.packet;
	}

	public double[] toNativeArray() {
		double[] nat = new double[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}
	
	@Override
	public List<Double> toList() {
		return Arrays.asList(this.packet);
	}

}
