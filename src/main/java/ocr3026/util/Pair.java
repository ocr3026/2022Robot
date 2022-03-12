package ocr3026.util;

public class Pair<K, V> {
	public K first;
	public V second;

	public Pair(K newFirst, V newSecond) {
		first = newFirst;
		second = newSecond;
	}

	public K getFirst() {
		return first;
	}

	public V getSecond() {
		return second;
	}

	public void setFirst(K newFirst) {
		first = newFirst;
	}

	public void setSecond(V newSecond) {
		second = newSecond;
	}

	public void setPair(K newFirst, V newSecond) {
		first = newFirst;
		second = newSecond;
	}

	public void copyPair(Pair<K, V> newPair) {
		first = newPair.first;
		second = newPair.second;
	}
}
