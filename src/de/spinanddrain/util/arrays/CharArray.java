package de.spinanddrain.util.arrays;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public class CharArray implements Array<Character> {

	private Character[] packet;
	
	public CharArray() {
		this.packet = new Character[0];
	}
	
	public CharArray(char... chars) {
		this.packet = new Character[chars.length];
		for(int i = 0; i < chars.length; i++) {
			this.packet[i] = chars[i];
		}
	}
	
	public CharArray(Character... chars) {
		this.packet = chars;
	}
	
	@Override
	public CharArray add(Character object) {
		Character[] copy = this.packet;
		this.packet = new Character[copy.length + 1];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = copy[i];
		}
		this.packet[copy.length] = object;
		return this;
	}

	@Override
	public CharArray remove(int index) {
		Character[] copy = this.packet;
		this.packet = new Character[copy.length - 1];
		for(int i = 0, c = 0; i < copy.length; i++) {
			if(i == index) continue;
			this.packet[c++] = copy[i];
		}
		return this;
	}

	@Override
	public CharArray shift(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(0);
		}
		return this;
	}

	@Override
	public CharArray unshift(Character object) {
		Character[] copy = this.packet;
		this.packet = new Character[copy.length + 1];
		this.packet[0] = object;
		for(int i = 1; i < this.packet.length; i++) {
			this.packet[i] = copy[i - 1];
		}
		return this;
	}

	@Override
	public CharArray pop(int amount) {
		for(int i = 0; i < amount; i++) {
			this.remove(this.packet.length - 1);
		}
		return this;
	}

	@Override
	public CharArray move(int s, int t) {
		Character[] copy = this.packet;
		this.packet = new Character[copy.length];
		for(int i = 0; i < copy.length; i++) {
			this.packet[i] = (i == s ? copy[t] : i == t ? copy[s] : copy[i]);
		}
		return this;
	}

	@Override
	public CharArray fill(Character[] content) {
		for(int i = 0; i < content.length; i++) {
			this.add(content[i]);
		}
		return this;
	}

	@Override
	public CharArray override(Character[] content) {
		this.packet = content;
		return this;
	}

	@Override
	public CharArray push(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(0, i);
			}
		}
		return this;
	}

	@Override
	public CharArray pull(int count) {
		for(int c = 0; c < count; c++) {
			for(int i = 0; i < this.packet.length; i++) {
				this.move(this.packet.length - 1, this.packet.length - 1 - i);
			}
		}
		return this;
	}

	@Override
	public CharArray keep(Filter<Character> filter) {
		char[] copy = new char[this.packet.length];
		int newLength = 0;
		for(int i = 0; i < copy.length; i++) {
			if(filter.keep(this.packet[i])) {
				copy[newLength++] = this.packet[i];
			}
		}
		Character[] result = new Character[newLength];
		for(int i = 0; i < result.length; i++) {
			result[i] = copy[i];
		}
		this.packet = result;
		return this;
	}
	
	@Override
	public CharArray eliminate(Filter<Character> filter) {
		this.keep(filter.negate());
		return this;
	}
	
	@Override
	public CharArray clear() {
		this.clear(0);
		return this;
	}
	
	@Override
	public CharArray clear(int newLength) {
		this.packet = new Character[newLength];
		return this;
	}
	
	@Override
	public int firstIndexOf(Character object) {
		for(int i = 0; i < this.packet.length; i++) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Character object) {
		for(int i = this.packet.length - 1; i >= 0; i--) {
			if(this.packet[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean matches(Character[] other) {
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
	public CharArray modifyEach(NativeOperation<Character> operate) {
		for(int i = 0; i < this.packet.length; i++) {
			this.packet[i] = operate.operate(this.packet[i]);
		}
		return this;
	}
	
	@Override
	public CharArray forEach(VoidOperation<Character> operate) {
		for(Character i : this.packet) {
			operate.operate(i);
		}
		return this;
	}
	
	@Override
	public CharArray subarray(int splitIndex) {
		return this.subarray(splitIndex, this.packet.length);
	}
	
	@Override
	public CharArray subarray(int beginIndex, int endIndex) {
		Character[] copy = new Character[this.packet.length - beginIndex - (this.packet.length - endIndex)];
		for(int i = beginIndex, c = 0; i < endIndex; i++, c++) {
			copy[c] = this.packet[beginIndex + c];
		}
		return new CharArray(copy);
	}
	
	@Override
	public CharArray copy() {
		CharArray copy = new CharArray();
		this.forEach(element -> copy.add(element));
		return copy;
	}
	
	@Override
	public CharArray insert(Character object, int index) {
		this.packet = this.subarray(0, index).add(object).fill(this.subarray(index).packet).toArray();
		return this;
	}
	
	@Override
	public int length() {
		return this.packet.length;
	}
	
	@Override
	public CharArray reverse() {
		for(int i = 0; i < this.packet.length / 2; i++) {
			this.move(i, this.packet.length - i - 1);
		}
		return this;
	}
	
	@Override
	public CharArray shuffle() {
		for(int i = 0; i < this.packet.length; i++)
			this.move(i, MathUtils.random(this.packet.length - 1, 0));
		return this;
	}
	
	@Override
	public CharArray sort() {
		IntArray sorted = this.toInts().sort();
		this.clear();
		sorted.forEach(element -> this.add((char) ((int) element)));
		return this;
	}
	
	// TODO Insert new functions above this line
	
	@Override
	public Character get(int index) {
		return this.packet[index];
	}
	
	@Override
	public Character[] toArray() {
		return this.packet;
	}
	
	/**
	 * For ASCII values.
	 * 
	 * @return this character array to a new <code>IntArray</code>
	 */
	public IntArray toInts() {
		IntArray res = new IntArray();
		this.forEach(element -> res.add((int) element));
		return res;
	}
	
	public char[] toNativeArray() {
		char[] nat = new char[this.packet.length];
		for(int i = 0; i < nat.length; i++) {
			nat[i] = this.packet[i];
		}
		return nat;
	}

	@Override
	public List<Character> toList() {
		return Arrays.asList(this.packet);
	}

}
